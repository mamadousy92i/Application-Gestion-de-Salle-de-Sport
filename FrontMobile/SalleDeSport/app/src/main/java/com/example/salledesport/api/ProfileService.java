package com.example.salledesport.api;

import com.example.salledesport.model.Goal;
import com.example.salledesport.model.GoalUpdateRequest;
import com.example.salledesport.model.UserUpdateRequest;
import com.example.salledesport.model.Utilisateur;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.Part;

public interface ProfileService {
    @GET("api/accounts/users/me/")
    Call<Utilisateur> getProfile();

    @PATCH("api/accounts/users/me/")
    Call<Utilisateur> updateProfile(@Body UserUpdateRequest user);

    @Multipart
    @PATCH("api/accounts/users/me/")
    Call<Utilisateur> updateProfilePicture(@Part MultipartBody.Part file);

    @DELETE("api/accounts/users/me/")
    Call<Utilisateur> deleteProfile();

    @PATCH("api/programs/goals/change-goal/")
    Call<Goal> changeGoal(@Body GoalUpdateRequest goal);
}