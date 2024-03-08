package com.hypersoft.cmpsconsent

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.ump.UserMessagingPlatform
import com.hypersoft.cmpsconsent.callbacks.ConsentCallback
import com.hypersoft.cmpsconsent.controller.ConsentController

class MainActivity : AppCompatActivity() {

    lateinit var btnPrivacyPolicy: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnPrivacyPolicy = findViewById(R.id.btn_privacy_policy)

        btnPrivacyPolicy.setOnClickListener {
            UserMessagingPlatform.showPrivacyOptionsForm(this) { formError ->
                formError?.let {
                    Log.d("consentFormTAG", "showPrivacyOptionsForm, ${formError.message}")
                    Toast.makeText(this, "Operation failed, Try later", Toast.LENGTH_SHORT).show()
                }
            }
        }

        /**
         * Search "addTestDeviceHashedId" in logcat after running the application
         * and past that device id for debug
         */

        ConsentController(this).apply {
            initConsent("9156F00E3949139D3B272AF4D0173CF9",object: ConsentCallback {
                override fun onAdsLoad(canRequestAd: Boolean) {
                    super.onAdsLoad(canRequestAd)
                }

                override fun onConsentFormShow() {
                    super.onConsentFormShow()
                }

                override fun onConsentFormDismissed() {
                    super.onConsentFormDismissed()
                }

                override fun onPolicyStatus(required: Boolean) {
                    super.onPolicyStatus(required)
                }
            })
        }

    }
}