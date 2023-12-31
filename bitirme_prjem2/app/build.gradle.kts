plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //firebase için aşağıda 1 satır
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.bitirme_projem2"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bitirme_projem2"
        minSdk = 24
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation ("com.google.code.gson:gson:2.10")
    implementation ("androidx.compose.foundation:foundation-layout:1.5.4")


    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    //implementation("com.google.firebase:firebase-auth-ktx:22.3.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.compose.foundation:foundation-layout-android:1.5.4")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.6")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//aşağıdakiler firebase için
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    //implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.firebaseui:firebase-ui-auth:7.2.0")
    implementation ("com.facebook.android:facebook-android-sdk:16.2.0")

   // implementation ("com.google.firebase:firebase-database-ktx:24.0.0") // Firebase Realtime Database bağımlılığı
    //implementation ("com.google.firebase:firebase-auth-ktx:24.0.0") // Eğer gerekliyse Authentication bağımlılığı

    //apply plugin: ("com.google.gms.google-services") // Firebase plugin'i
}