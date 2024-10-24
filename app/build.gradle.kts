plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.patitasvivas"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.patitasvivas"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    packagingOptions {
        exclude ("META-INF/DEPENDENCIES") // Ignorar el archivo duplicado
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.espresso.core)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.messaging.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    implementation("io.coil-kt:coil-compose:2.4.0")
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation(libs.firebase.auth)
    implementation(libs.androidx.navigation.compose)
    implementation("com.google.auth:google-auth-library-oauth2-http:1.9.0")
    implementation ("androidx.compose.foundation:foundation:1.4.0-alpha02") // Verifica que sea la última versión disponible

    dependencies {
        implementation ("androidx.compose.ui:ui:<version>")
        implementation ("androidx.compose.material3:material3:<version>")
        implementation ("androidx.compose.foundation:foundation:<version>")
        // Agrega esta línea para LazyRow
        implementation ("androidx.compose.foundation:foundation-layout:<version>")
        implementation ("androidx.compose.ui:ui:1.2.0")
        implementation ("androidx.compose.material3:material3:1.0.0")
        implementation ("io.coil-kt:coil-compose:2.2.2" )// Para las imágenes
            // Otras dependencias...


    }

}