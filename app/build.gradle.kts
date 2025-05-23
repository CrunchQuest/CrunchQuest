plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.crunchquest.android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.crunchquest.android"
        minSdk = 24
        //noinspection EditedTargetSdkVersion
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    java {
        toolchain {
//            languageVersion.set(JavaLanguageVersion.of(8))
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("com.google.android.gms:play-services-location:21.2.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation("io.coil-kt:coil-compose:2.2.0")

    implementation("androidx.annotation:annotation:1.1.0")

    implementation("androidx.navigation:navigation-compose:2.7.3")
    implementation("com.github.Gurupreet:FontAwesomeCompose:1.0.0")

    implementation("androidx.compose.material:material-icons-core:1.5.4")
    implementation("androidx.compose.material:material-icons-extended:1.5.4")

    implementation("com.airbnb.android:lottie-compose:4.2.0")
    implementation("com.airbnb.android:lottie:5.2.0")

    implementation("com.google.firebase:firebase-analytics:21.5.1")
    implementation("com.google.firebase:firebase-crashlytics:18.2.4")
    implementation("com.google.firebase:firebase-messaging:23.0.0")
    implementation("com.google.firebase:firebase-auth:21.0.1")
    implementation("com.google.firebase:firebase-firestore:23.0.3")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))

    //notification builder
    implementation("io.karn:notify:1.3.0")

    //Work manager
    implementation("androidx.work:work-runtime-ktx:2.7.0")

    //groupie and picasso
    implementation("com.github.lisawray.groupie:groupie:2.1.0")
    implementation("com.squareup.picasso:picasso:2.8")

    // Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    //Google pay api
    implementation("com.google.android.gms:play-services-wallet:19.2.1")

    //Circle image view
    implementation("de.hdodenhof:circleimageview:2.2.0")

    //Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")

    //image cropper
//    implementation("com.theartofdev.edmodo:android-image-cropper:2.8.0")
    implementation("com.theartofdev.edmodo:android-image-cropper:2.8.0")

    //image slider
    implementation("com.github.denzcoskun:ImageSlideshow:0.1.0")

    //Simple image popup. Used together with the slider
    implementation("com.github.chathuralakmal:AndroidImagePopup:1.2.2")

    //justify
    implementation("com.codesgood:justifiedtextview:1.1.0")

    //Dependencies for Image View. If an image view is clicked, it will show the image
    implementation("com.github.hsmnzaydn:imagezoom:1.3.0")

    // Navigation Dependency
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Viewpage
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // Maps
    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // Firebase App Check
    implementation("com.google.firebase:firebase-appcheck:16.0.0-beta02")
    implementation("com.google.firebase:firebase-appcheck-safetynet:16.0.0-beta02")

    // Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
}