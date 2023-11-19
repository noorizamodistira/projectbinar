plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
    id("kotlin-kapt")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.modiss.binar_challengech5"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.modiss.binar_challengech5"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    buildFeatures{
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.navigation:navigation-fragment-ktx:2.4.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.4.0")
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    implementation("androidx.room:room-ktx:2.5.2")
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("com.google.firebase:firebase-auth:22.2.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    kapt("androidx.room:room-compiler:2.5.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //GSON
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //OKHTTP
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    //load gambar online library glide
    implementation("com.github.bumptech.glide:glide:4.11.0")

    //ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")

    //livedata
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")

    // koin
    implementation("io.insert-koin:koin-android:3.5.0")

    //unit test
    testImplementation("io.mockk:mockk-android:1.13.8")
    testImplementation("io.mockk:mockk-agent:1.13.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.2")
    testImplementation("app.cash.turbine:turbine:1.0.0")
}