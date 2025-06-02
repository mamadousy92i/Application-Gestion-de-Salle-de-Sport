plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.salledesport"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.salledesport"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // MPAndroidChart pour les graphiques
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // pour le rafraichissement de la page
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    // OkHttp pour le log
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")



    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.core:core-ktx:1.10.1")

    // Material Components - pour tous les composants Material Design
    implementation ("com.google.android.material:material:1.11.0")

    implementation ("com.github.f0ris.sweetalert:library:1.6.2")


    // ConstraintLayout - pour le positionnement avancé des éléments
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")

    // Lottie - pour les animations vectorielles
    implementation ("com.airbnb.android:lottie:6.1.0")
    implementation ("androidx.cardview:cardview:1.0.0")
    // Pour les animations avancées et transitions
    implementation ("androidx.transition:transition:1.4.1")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")
    // Support pour les polices téléchargeables (Montserrat)
    implementation ("androidx.core:core:1.10.1")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.annotation)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}