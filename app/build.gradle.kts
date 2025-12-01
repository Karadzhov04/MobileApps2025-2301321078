plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.IvanKaradzhov.expensemate"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.IvanKaradzhov.expensemate"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            keyAlias = "androiddebugkey"
            keyPassword = "android"
            storeFile = file("${System.getProperty("user.home")}/.android/debug.keystore")
            storePassword = "android"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
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
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity-ktx:1.7.2")
    implementation("androidx.transition:transition:1.4.1")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    // Room
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

    // Charts
    implementation("com.github.PhilJay:MPAndroidChart:3.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // Unit тестове
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // UI тестове (Espresso + JUnit)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Допълнително за Espresso Intents (нужно за нашия тест с FAB → AddExpenseActivity)
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")

    // ActivityScenario (за стартиране на Activity в тестове)
    androidTestImplementation("androidx.test:core:1.5.0")

    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")


    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")

}

