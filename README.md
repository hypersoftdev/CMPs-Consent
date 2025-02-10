# Consent Management in Android

## Overview
This project demonstrates how to implement **Consent Management** in an Android application using Google's User Messaging Platform (UMP). It includes features like:

- Requesting user consent for personalized ads
- Showing a consent form when required
- Handling privacy options based on the user’s selection

## Features
- **Consent Handling**: Initializes and manages user consent status
- **Privacy Policy Display**: Allows users to review and update consent settings
- **Debug Mode**: Enables testing with test device IDs

## Project Structure
```
├── MainActivity.kt  # Handles UI and initializes consent process
├── ConsentController.kt  # Manages consent status and displays forms
├── ConsentCallback.kt  # Interface for consent state updates
```

## Dependencies
Ensure you have the following dependencies in your `build.gradle` (Module: app):

```gradle
dependencies {
    implementation 'com.google.android.ump:user-messaging-platform:3.1.0' // or latest
}
```

## How It Works
### 1. Initializing Consent in `MainActivity`
The consent process starts when the app is launched (pass test device id as an parameter):

```kotlin
private fun initConsent() {
    ConsentController(this).apply {
        initConsent("A69AF72EA9855046AD0439E4A6287ADF", object : ConsentCallback {
            override fun onAdsLoad(canRequestAd: Boolean) {
                Log.d(TAG, "Ads can be requested: $canRequestAd")
            }

            override fun onConsentFormLoaded() {
                showConsentForm()
            }
        })
    }
}
```

### 2. Implementing Consent Handling in `ConsentController.kt`
The `ConsentController` class:
- Requests consent information
- Loads and displays consent forms
- Handles user selections

```kotlin
fun initConsent(deviceId: String, callback: ConsentCallback?) {
    val debugSettings = ConsentDebugSettings.Builder(activity)
        .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
        .addTestDeviceHashedId(deviceId)
        .build()

    val params = ConsentRequestParameters.Builder()
        .setConsentDebugSettings(debugSettings)
        .build()

    consentInformation = UserMessagingPlatform.getConsentInformation(activity)
    consentInformation?.requestConsentInfoUpdate(activity, params, { status ->
        if (status.isConsentFormAvailable) {
            loadConsentForm()
        }
    }, { error ->
        Log.e(TAG, "Error initializing consent: ${error.message}")
    })
}
```

### 3. Displaying Privacy Policy Options
When the user clicks the **Privacy Policy** button, they can update their preferences:

```kotlin
private fun onPrivacyClick() {
    UserMessagingPlatform.showPrivacyOptionsForm(this) { formError ->
        formError?.let {
            Toast.makeText(this, "Operation failed, Try later", Toast.LENGTH_SHORT).show()
        }
    }
}
```

## Running the Project
1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/android-consent-management.git
   ```
2. Open the project in **Android Studio**.
3. Add your **AdMob App ID** in `AndroidManifest.xml`.
4. Run the application on a physical device or emulator.
5. Check **Logcat** for debug messages (search for `addTestDeviceHashedId` to get your test device ID).

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

