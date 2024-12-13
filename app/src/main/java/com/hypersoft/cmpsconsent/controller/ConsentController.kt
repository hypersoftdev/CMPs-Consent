package com.hypersoft.cmpsconsent.controller

import android.app.Activity
import android.util.Log
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentInformation.ConsentStatus
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.hypersoft.cmpsconsent.BuildConfig
import com.hypersoft.cmpsconsent.MainActivity.Companion.TAG
import com.hypersoft.cmpsconsent.callbacks.ConsentCallback

/**
 * @Author: Muhammad Yaqoob
 * @Date: 29,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */

class ConsentController(private val activity: Activity) {

    private var consentInformation: ConsentInformation? = null
    private var consentCallback: ConsentCallback? = null

    val canRequestAds: Boolean get() = consentInformation?.canRequestAds() ?: false

    fun initConsent(@Debug("Device Id is only use for DEBUG") deviceId: String, callback: ConsentCallback?) {
        this.consentCallback = callback

        val isDebug = BuildConfig.DEBUG

        val debugSettings = ConsentDebugSettings.Builder(activity)
            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
            .addTestDeviceHashedId(deviceId)
            .build()

        val params = if (isDebug) {
            Log.d(TAG, "Debug parameters setConsentDebugSettings")
            ConsentRequestParameters.Builder().setConsentDebugSettings(debugSettings).build()
        } else {
            Log.d(TAG, "Release parameters setTagForUnderAgeOfConsent")
            ConsentRequestParameters.Builder().setTagForUnderAgeOfConsent(false).build()
        }

        consentInformation = UserMessagingPlatform.getConsentInformation(activity).also {
            if (isDebug) {
                Log.d(TAG, "Consent Form reset() in Debug")
                it.reset()
            } else {
                Log.d(TAG, "All is OK not Reset in release")
            }

            Log.d(TAG, "Consent ready for initialization")
            it.requestConsentInfoUpdate(activity, params, {
                Log.d(TAG, "Consent successfully initialized")
                Log.d(TAG, "is Consent Form Available: ${it.isConsentFormAvailable}")
                if (it.isConsentFormAvailable) {
                    when (consentInformation?.consentStatus) {
                        ConsentStatus.REQUIRED -> {
                            Log.d(TAG, "consentStatus: REQUIRED")
                            loadConsentForm()
                        }

                        ConsentStatus.NOT_REQUIRED -> {
                            consentCallback?.onAdsLoad(canRequestAds)
                            Log.d(TAG, "consentStatus: NOT_REQUIRED")
                        }

                        ConsentStatus.OBTAINED -> {
                            consentCallback?.onAdsLoad(canRequestAds)
                            Log.d(TAG, "consentStatus: OBTAINED")
                        }

                        ConsentStatus.UNKNOWN -> {
                            consentCallback?.onAdsLoad(canRequestAds)
                            Log.d(TAG, "consentStatus: UNKNOWN")
                        }

                        else -> {
                            Log.d(TAG, "consentInformation is null")
                        }
                    }
                    when (consentInformation?.privacyOptionsRequirementStatus) {
                        ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED -> {
                            consentCallback?.onPolicyStatus(true)
                            Log.d(TAG, "privacyOptionsRequirementStatus: REQUIRED")
                        }

                        ConsentInformation.PrivacyOptionsRequirementStatus.NOT_REQUIRED -> {
                            consentCallback?.onPolicyStatus(false)
                            Log.d(TAG, "privacyOptionsRequirementStatus: NOT_REQUIRED")
                        }

                        ConsentInformation.PrivacyOptionsRequirementStatus.UNKNOWN -> {
                            consentCallback?.onPolicyStatus(false)
                            Log.d(TAG, "privacyOptionsRequirementStatus: UNKNOWN")
                        }

                        else -> {
                            consentCallback?.onPolicyStatus(false)
                            Log.d(TAG, "consentInformation is null")
                        }
                    }

                } else {
                    consentCallback?.onAdsLoad(canRequestAds)
                }
            }, { error ->
                consentCallback?.onAdsLoad(canRequestAds)
                Log.e(TAG, "initializationError: ${error.message}")
            })
        }
    }

    private fun loadConsentForm() {
        UserMessagingPlatform.loadConsentForm(activity, { consentForm ->
            Log.d(TAG, "Consent Form Load Successfully")
            showConsentForm(consentForm)
        }) { formError ->
            consentCallback?.onAdsLoad(canRequestAds)
            Log.e(TAG, "Consent Form Load to Fail: ${formError.message}")
        }
    }

    private fun showConsentForm(consentForm: ConsentForm) {
        consentCallback?.onConsentFormShow()
        Log.i(TAG, "consent form show")
        consentForm.show(activity) { formError ->
            consentCallback?.onConsentFormDismissed()
            consentCallback?.onAdsLoad(canRequestAds)
            Log.i(TAG, "consent Form Dismissed")
            formError?.let {
                Log.e(TAG, "Consent Form Show to fail: ${it.message}")
            } ?: run {
                checkConsentAndPrivacyStatus()
            }
        }
    }

    private fun checkConsentAndPrivacyStatus() {
        Log.d(TAG, "check Consent And Privacy Status After Form Dismissed")
        when (consentInformation?.consentStatus) {
            ConsentStatus.REQUIRED -> {
                Log.d(TAG, "consentStatus: REQUIRED")
            }

            ConsentStatus.NOT_REQUIRED -> {
                Log.d(TAG, "consentStatus: NOT_REQUIRED")
            }

            ConsentStatus.OBTAINED -> {
                Log.d(TAG, "consentStatus: OBTAINED")
            }

            ConsentStatus.UNKNOWN -> {
                Log.d(TAG, "consentStatus: UNKNOWN")
            }

            else -> {
                Log.d(TAG, "consentInformation is null")
            }
        }
        when (consentInformation?.privacyOptionsRequirementStatus) {
            ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED -> {
                Log.d(TAG, "privacyOptionsRequirementStatus: REQUIRED")
            }

            ConsentInformation.PrivacyOptionsRequirementStatus.NOT_REQUIRED -> {
                Log.d(TAG, "privacyOptionsRequirementStatus: NOT_REQUIRED")
            }

            ConsentInformation.PrivacyOptionsRequirementStatus.UNKNOWN -> {
                Log.d(TAG, "privacyOptionsRequirementStatus: UNKNOWN")
            }

            else -> {
                Log.d(TAG, "consentInformation is null")
            }
        }
    }

    annotation class Debug(val message: String = "For Debug Feature")
}