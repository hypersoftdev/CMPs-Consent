package com.hypersoft.cmpsconsent

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.android.ump.UserMessagingPlatform
import com.hypersoft.cmpsconsent.callbacks.ConsentCallback
import com.hypersoft.cmpsconsent.controller.ConsentController
import com.hypersoft.cmpsconsent.enums.CMPStatus

class MainActivity : AppCompatActivity() {

    lateinit var btnPrivacyPolicy: Button

    val ADS_TAG = "adsTestingTAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPrivacyPolicy = findViewById(R.id.btn_privacy_policy)

        btnPrivacyPolicy.setOnClickListener {
            UserMessagingPlatform.showPrivacyOptionsForm(this) { formError ->
                formError?.let {
                    Log.d(ADS_TAG, "showPrivacyOptionsForm, ${formError.message}")
                    Toast.makeText(this, "Operation failed, Try later", Toast.LENGTH_SHORT).show()
                }
            }
        }

        /**
         * Search "addTestDeviceHashedId" in logcat after running the application
         * and past that device id for debug
         */

        ConsentController(this).apply {
            initConsent("22476B333967469A818262B8DE3FBF56",object: ConsentCallback {
                override fun onReadyForInitialization() {
                    Log.d(ADS_TAG, "onReadyForInitialization, canRequestAds: $canRequestAds")
                }
                override fun onInitializationSuccess() {
                    Log.d(ADS_TAG, "onInitializationSuccess, canRequestAds: $canRequestAds")
                }
                override fun onInitializationError(error: String) {
                    Log.d(ADS_TAG, "onInitializationError, canRequestAds: $canRequestAds")
                }
                override fun onConsentFormAvailability(available: Boolean) {
                    Log.d(ADS_TAG, "onConsentFormAvailability, canRequestAds: $canRequestAds")
                }
                override fun onConsentFormLoadSuccess() {
                    Log.d(ADS_TAG, "onConsentFormLoadSuccess, canRequestAds: $canRequestAds")
                }
                override fun onConsentFormLoadFailure(error: String) {
                    Log.d(ADS_TAG, "onConsentFormLoadFailure, canRequestAds: $canRequestAds")
                }
                override fun onRequestShowConsentForm() {
                    Log.d(ADS_TAG, "onRequestShowConsentForm, canRequestAds: $canRequestAds")
                }
                override fun onConsentFormShowFailure(error: String) {
                    Log.d(ADS_TAG, "onConsentFormShowFailure, canRequestAds: $canRequestAds")
                }
                override fun onConsentFormDismissed() {
                    Log.d(ADS_TAG, "onConsentFormDismissed, canRequestAds: $canRequestAds")
                }

                override fun onConsentStatus(status: CMPStatus) {
                    Log.d(ADS_TAG, "onConsentStatus: $status, canRequestAds: $canRequestAds")
                    when(status){
                        CMPStatus.REQUIRED -> {
                            // Do Your Work Here
                        }
                        CMPStatus.NOT_REQUIRED -> {
                            // Do Your Work Here
                        }
                        CMPStatus.OBTAINED -> {
                            // Do Your Work Here
                        }
                        CMPStatus.UNKNOWN -> {
                            // Do Your Work Here
                        }
                    }

                }

                override fun onPolicyStatus(status: CMPStatus) {
                    Log.d(ADS_TAG, "onPolicyStatus: $status, canRequestAds: $canRequestAds")
                    when(status){
                        CMPStatus.REQUIRED -> {
                            // Show Consent privacy policy option in setting or
                            // where you want to show privacy policy
                            try {
                                btnPrivacyPolicy.visibility = View.VISIBLE
                            }catch (ignore:Exception){}
                        }
                        CMPStatus.NOT_REQUIRED -> {
                            // no need to show or visible Consent privacy policy
                        }
                        CMPStatus.UNKNOWN -> {
                            // no need to show or visible Consent privacy policy
                        }
                        else -> {
                            // no need to show or visible Consent privacy policy
                        }
                    }
                }
            })
        }

    }
}