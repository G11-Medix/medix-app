import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.google.services)
}

fun String.toBuildConfigString(): String =
    "\"" + replace("\\", "\\\\").replace("\"", "\\\"") + "\""

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use(::load)
    }
}

fun resolveConfig(key: String, defaultValue: String = ""): String {
    return localProperties.getProperty(key)
        ?: (project.findProperty(key) as String?)
        ?: defaultValue
}

val medixDataApiBaseUrl =
    resolveConfig("MEDIX_DATA_API_BASE_URL", resolveConfig("MEDIX_API_BASE_URL", "http://192.168.20.10:8001/"))
val medixAiApiBaseUrl =
    resolveConfig("MEDIX_AI_API_BASE_URL", "http://192.168.20.10:8000/")
val medixWsBaseUrl =
    resolveConfig("MEDIX_WS_BASE_URL", "ws://192.168.20.10:8000")
val supabaseUrl =
    resolveConfig("SUPABASE_URL")
val supabaseAnonKey =
    resolveConfig("SUPABASE_ANON_KEY")

android {
    namespace = "com.example.medix"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.medix"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "MEDIX_API_BASE_URL", medixDataApiBaseUrl.toBuildConfigString())
        buildConfigField("String", "MEDIX_DATA_API_BASE_URL", medixDataApiBaseUrl.toBuildConfigString())
        buildConfigField("String", "MEDIX_AI_API_BASE_URL", medixAiApiBaseUrl.toBuildConfigString())
        buildConfigField("String", "MEDIX_WS_BASE_URL", medixWsBaseUrl.toBuildConfigString())
        buildConfigField("String", "SUPABASE_URL", supabaseUrl.toBuildConfigString())
        buildConfigField("String", "SUPABASE_ANON_KEY", supabaseAnonKey.toBuildConfigString())

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
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    implementation(libs.androidx.activity.compose)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)

    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.auth)
    implementation(libs.ktor.client.android)

    implementation("org.osmdroid:osmdroid-android:6.1.18")

    implementation("com.google.firebase:firebase-messaging:23.4.0")

    // Room (Actualizado para compatibilidad con Kotlin 2.2.10)
    val room_version = "2.7.0-alpha01"

    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation(libs.core.ktx)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    //HILT
    implementation("com.google.dagger:hilt-android:2.59.2")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    ksp("com.google.dagger:hilt-compiler:2.59.2")

    implementation("io.coil-kt:coil-compose:2.5.0")


    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("androidx.datastore:datastore-preferences:1.1.1")



}