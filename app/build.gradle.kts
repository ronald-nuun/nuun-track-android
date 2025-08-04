plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = libs.versions.appId.get()
    compileSdk = libs.versions.compileSdkVersion.get().toInt()

    defaultConfig {
        applicationId = libs.versions.appId.get()
        minSdk = libs.versions.minSdkVersion.get().toInt()
        targetSdk = libs.versions.targetSdkVersion.get().toInt()
        versionCode = libs.versions.versionCode.get().toInt()
        versionName = libs.versions.versionName.get()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
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
        viewBinding = true
    }
}

dependencies {
    with(libs) {
        implementation(androidx.core.ktx)

        // Compose
        implementation(bundles.compose)
        implementation(platform(compose.bom))
        debugImplementation(compose.ui.tooling)

        // Testing
        testImplementation(junit)
        androidTestImplementation(androidx.junit)
        androidTestImplementation(androidx.espresso.core)
        androidTestImplementation(compose.ui.test.junit4)
        debugImplementation(compose.ui.test.manifest)
        androidTestImplementation(platform(compose.bom))

        // Intuit SDP SSP
        implementation(bundles.intuit)

        // Hilt
        implementation(hilt.android)
        ksp(hilt.android.compiler)

        // Hilt for UI Tests
        androidTestImplementation(hilt.android.test)
        kspAndroidTest(hilt.android.compiler)

        // Coroutines
        implementation(bundles.coroutine)
        testImplementation(coroutines.test)

        // OkHttp
        implementation(bundles.okhttp)

        // Retrofit
        implementation(bundles.retrofit)

        // Moshi
        implementation(moshi.kotlin)
        ksp(moshi.kotlin.codegen)

        // Chucker
        debugImplementation(chucker)
        releaseImplementation(chucker.no.op)

        // Lifecycle
        implementation(bundles.lifecycle)

        // DataStore
        implementation(bundles.datastore)

        // Navigation
        implementation(bundles.navigation)

        // Accompanies
        implementation(bundles.accompanies)

        // Crypto
        implementation(androidx.security.crypto)

        // Coil
        implementation(coil)

        // ExoPlayer
        implementation(bundles.exoPlayer)

    }
}