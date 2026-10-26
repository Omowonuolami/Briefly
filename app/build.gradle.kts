import org.gradle.kotlin.dsl.support.kotlinCompilerOptions

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.serialization)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.roomcompiler)
//    alias(libs.plugins.googleService)
}

android {
    namespace = "com.briefly"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.briefly"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "BASE_URL", "\"https://newsapi.org/\"")
        buildConfigField("boolean", "DEBUG_LEVEL", "true")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug{

        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

//    kotlinOptions{
//        jvmTarget = "11"
//    }


    buildFeatures {
        compose = true
        buildConfig = true
    }
}

room{
    schemaDirectory("$projectDir/schemas")
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
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
    implementation(libs.androidx.compose.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.google.material)
    implementation(libs.splash.screen)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.serialization)
    implementation(libs.coil.compose)
    implementation(libs.icon.extended)
    implementation(libs.hilt.android)

    kapt(libs.hilt.android.compiler)
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    //    datastore
    implementation(libs.preferenceDatastore)
    implementation (libs.protodatastore)

//    roomDB
    implementation(libs.roomDatabase)
    ksp(libs.roomCompiler)
    implementation(libs.roomKtx)

    //retrofit
    implementation(libs.squareup.retrofit2.retrofit)
    implementation(libs.squareup.retrofit2.converter)

    //okhttp
    implementation(libs.okhttp3.logging)
    implementation(libs.okhttp3)

    // Firebase
//    implementation(platform(libs.firebase))

    implementation(libs.jSoup)
}