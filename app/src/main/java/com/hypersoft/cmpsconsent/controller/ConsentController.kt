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

    fun initConsent(
        @Debug("Device Id is only use for DEBUG") deviceId: String,
        callback: ConsentCallback?
    ) {
        this.consentCallback = callback

        val isDebug = BuildConfig.DEBUG

        val debugSettings = ConsentDebugSettings.Builder(activity)
            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
            .addTestDeviceHashedId(deviceId)
            .build()

        val params = if (isDebug) {
            Log.d("consentFormTAG", "Debug parameters setConsentDebugSettings")
            ConsentRequestParameters.Builder().setConsentDebugSettings(debugSettings).build()
        } else {
            Log.d("consentFormTAG", "Release parameters setTagForUnderAgeOfConsent")
            ConsentRequestParameters.Builder().setTagForUnderAgeOfConsent(false).build()
        }

        consentInformation = UserMessagingPlatform.getConsentInformation(activity).also {
            if (isDebug) {
                Log.d("consentFormTAG", "Consent Form reset() in Debug")
                it.reset()
            }else{
                Log.d("consentFormTAG", "All is OK not Reset in release")
            }

            Log.d("consentFormTAG", "consent ready for initialization")
            it.requestConsentInfoUpdate(activity, params, {
                Log.d("consentFormTAG", "consent successfully initialized")
                Log.d("consentFormTAG", "is Consent Form Available: ${it.isConsentFormAvailable}")
                if (it.isConsentFormAvailable) {
                    when(consentInformation?.consentStatus){
                        ConsentStatus.REQUIRED -> {
                            Log.d("consentFormTAG", "consentStatus: REQUIRED")
                            loadConsentForm()
                        }
                        ConsentStatus.NOT_REQUIRED -> {
                            consentCallback?.onAdsLoad(canRequestAds)
                            Log.d("consentFormTAG", "consentStatus: NOT_REQUIRED")
                        }
                        ConsentStatus.OBTAINED -> {
                            consentCallback?.onAdsLoad(canRequestAds)
                            Log.d("consentFormTAG", "consentStatus: OBTAINED")
                        }
                        ConsentStatus.UNKNOWN -> {
                            consentCallback?.onAdsLoad(canRequestAds)
                            Log.d("consentFormTAG", "consentStatus: UNKNOWN")
                        }
                        else -> {
                            Log.d("consentFormTAG", "consentInformation is null")
                        }
                    }
                    when(consentInformation?.privacyOptionsRequirementStatus){
                        ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED -> {
                            consentCallback?.onPolicyStatus(true)
                            Log.d("consentFormTAG", "privacyOptionsRequirementStatus: REQUIRED")
                        }
                        ConsentInformation.PrivacyOptionsRequirementStatus.NOT_REQUIRED -> {
                            consentCallback?.onPolicyStatus(false)
                            Log.d("consentFormTAG", "privacyOptionsRequirementStatus: NOT_REQUIRED")
                        }
                        ConsentInformation.PrivacyOptionsRequirementStatus.UNKNOWN -> {
                            consentCallback?.onPolicyStatus(false)
                            Log.d("consentFormTAG", "privacyOptionsRequirementStatus: UNKNOWN")
                        }
                        else -> {
                            consentCallback?.onPolicyStatus(false)
                            Log.d("consentFormTAG", "consentInformation is null")
                        }
                    }

                }else{
                    consentCallback?.onAdsLoad(canRequestAds)
                }
            }, { error ->
                consentCallback?.onAdsLoad(canRequestAds)
                Log.e("consentFormTAG", "initializationError: ${error.message}")
            })
        }
    }

    private fun loadConsentForm() {
        UserMessagingPlatform.loadConsentForm(activity, { consentForm ->
            Log.d("consentFormTAG", "Consent Form Load Successfully")
            showConsentForm(consentForm)
        }) { formError ->
            consentCallback?.onAdsLoad(canRequestAds)
            Log.e("consentFormTAG", "Consent Form Load to Fail: ${formError.message}")
        }
    }

    private fun showConsentForm(consentForm: ConsentForm) {
        consentCallback?.onConsentFormShow()
        Log.i("consentFormTAG", "consent form show")
        consentForm.show(activity) { formError ->
            consentCallback?.onConsentFormDismissed()
            consentCallback?.onAdsLoad(canRequestAds)
            Log.i("consentFormTAG", "consent Form Dismissed")
            formError?.let {
                Log.e("consentFormTAG", "Consent Form Show to fail: ${it.message}")
            } ?: run {
                checkConsentAndPrivacyStatus()
            }
        }
    }

    private fun checkConsentAndPrivacyStatus() {
        Log.d("consentFormTAG", "check Consent And Privacy Status After Form Dismissed")
        when(consentInformation?.consentStatus){
            ConsentStatus.REQUIRED -> {
                Log.d("consentFormTAG", "consentStatus: REQUIRED")
            }
            ConsentStatus.NOT_REQUIRED -> {
                Log.d("consentFormTAG", "consentStatus: NOT_REQUIRED")
            }
            ConsentStatus.OBTAINED -> {
                Log.d("consentFormTAG", "consentStatus: OBTAINED")
            }
            ConsentStatus.UNKNOWN -> {
                Log.d("consentFormTAG", "consentStatus: UNKNOWN")
            }
            else -> {
                Log.d("consentFormTAG", "consentInformation is null")
            }
        }
        when(consentInformation?.privacyOptionsRequirementStatus){
            ConsentInformation.PrivacyOptionsRequirementStatus.REQUIRED -> {
                Log.d("consentFormTAG", "privacyOptionsRequirementStatus: REQUIRED")
            }
            ConsentInformation.PrivacyOptionsRequirementStatus.NOT_REQUIRED -> {
                Log.d("consentFormTAG", "privacyOptionsRequirementStatus: NOT_REQUIRED")
            }
            ConsentInformation.PrivacyOptionsRequirementStatus.UNKNOWN -> {
                Log.d("consentFormTAG", "privacyOptionsRequirementStatus: UNKNOWN")
            }
            else -> {
                Log.d("consentFormTAG", "consentInformation is null")
            }
        }
    }

    annotation class Debug(val message: String = "For Debug Feature")
}