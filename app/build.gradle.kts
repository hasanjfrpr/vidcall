plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.tigerteco.vidcall"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.tigerteco.vidcall"
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


    viewBinding{
        enable = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    ndkVersion = "28.0.12674087 rc2"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //webRTC
    implementation("com.mesibo.api:webrtc:1.0.5")
    implementation ("io.socket:socket.io-client:2.0.0")

    implementation ("com.google.code.gson:gson:2.10.1")

    implementation("io.insert-koin:koin-android:4.0.1")

    implementation ("com.airbnb.android:lottie:6.6.2")

}