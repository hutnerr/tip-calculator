plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.hbtipcalc.tipcalculator"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.hbtipcalc.tipcalculator"
        minSdk = 24
        targetSdk = 36
        versionCode = 3
        versionName = "1.03"

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
}

dependencies {
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("androidx.datastore:datastore-preferences-rxjava2:1.1.7")
    implementation("androidx.datastore:datastore-preferences-rxjava3:1.1.7")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.datastore.rxjava3)
    implementation(libs.datastore.preferences.rxjava3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}