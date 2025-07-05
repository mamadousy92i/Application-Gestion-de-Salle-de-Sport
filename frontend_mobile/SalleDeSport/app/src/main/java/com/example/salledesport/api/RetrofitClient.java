package com.example.salledesport.api;

import android.content.Context;
import android.content.SharedPreferences;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Headers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://8b89-102-164-172-8.ngrok-free.app/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .cookieJar(new CookieHandler()) // Ajout du CookieHandler
                    // Intercepteur pour récupérer le cookie auth_token à la connexion
                    .addInterceptor(chain -> {
                        Response response = chain.proceed(chain.request());
                        Headers headers = response.headers();
                        for (String header : headers.values("Set-Cookie")) {
                            if (header.startsWith("auth_token")) {
                                String token = header.split("=")[1].split(";")[0];
                                SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                                prefs.edit().putString("auth_token", token).apply();
                                break;
                            }
                        }
                        return response;
                    })
                    // Intercepteur pour ajouter le cookie auth_token
                    .addInterceptor(chain -> {
                        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                        String token = prefs.getString("auth_token", null);
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder();
                        if (token != null) {
                            requestBuilder.addHeader("Cookie", "auth_token=" + token);
                            // Conserver l'en-tête Authorization si nécessaire
                            requestBuilder.addHeader("Authorization", "Token " + token);
                        }
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ProfileService getApiService(Context context) {
        return getClient(context).create(ProfileService.class);
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }
}