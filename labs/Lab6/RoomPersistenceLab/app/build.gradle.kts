plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

android {
    namespace = "mobiledev.unb.ca.roompersistencelab"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "mobiledev.unb.ca.roompersistencelab"
        minSdk = 27
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// Configure the Room Gradle Plugin extension
room3 {
    // Specifies the folder where database schema files (.json) will be generated
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)

    // Room Core Components
    implementation(libs.androidx.room.runtime)

    // KSP Annotation Processor
    ksp(libs.androidx.room.compiler)
}
