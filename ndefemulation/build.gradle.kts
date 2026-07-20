plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.luigivampa92.ndefemulation"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
        buildConfigField("Boolean", "LOGS_ENABLED", "true")
        buildConfigField("String", "LOG_TAG", "\"NdefEmulation\"")
        buildConfigField("int", "LOG_LEVEL", "2")
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
}
