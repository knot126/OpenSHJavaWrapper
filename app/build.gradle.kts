plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.mediocre.smashhit"
    compileSdk = 26

    defaultConfig {
        applicationId = "com.mediocre.smashhit"
        minSdk = 9
        //noinspection ExpiredTargetSdkVersion
        targetSdk = 26
        versionCode = 10000
        versionName = "1.0.0"

        ndk {
            abiFilters += listOf("arm64-v8a")
        }
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

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    androidResources {
        noCompress += listOf("mp3")
    }
}

