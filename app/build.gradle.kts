plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
    id("com.google.dagger.hilt.android") // Hilt plugin
    kotlin("kapt")
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.mevi.lasheslam"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mevi.lasheslam"
        minSdk = 24
        targetSdk = 36
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.google.material)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.database.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //Nav Host
    implementation(libs.androidx.navigation.compose)
    //Icons extends
    implementation(libs.androidx.material.icons.extended)
    // Para Compose
    implementation(libs.androidx.runtime.livedata)
    // Para LiveData
    implementation(libs.androidx.lifecycle.livedata.ktx)

    // Inyeccion de dependecias
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    // dataStore
    implementation(libs.androidx.datastore.preferences)
    // Glide (cambiado a kapt)
    implementation(libs.glide.v4151)
    kapt(libs.compiler.v4151)

    implementation(platform(libs.firebase.bom))

    // Firebase libraries gestionadas por el BOM
    implementation(libs.firebase.firestore.ktx)
    // Firebase
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-config")
    implementation("com.google.firebase:firebase-analytics")


    //coil
    implementation ("io.coil-kt:coil-compose:2.2.0")
    implementation("com.tbuonomo:dotsindicator:5.1.0")
}