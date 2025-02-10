package com.hypersoft.cmpsconsent

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.ump.UserMessagingPlatform
import com.hypersoft.cmpsconsent.callbacks.ConsentCallback
import com.hypersoft.cmpsconsent.controller.ConsentController

/**
 * @Author: Muhammad Yaqoob
 * @Date: 29,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */

class MainActivity : AppCompatActivity() {

    private lateinit var btnPrivacyPolicy: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        initConsent()

        btnPrivacyPolicy.setOnClickListener { onPrivacyClick() }
    }

    private fun initViews() {
        btnPrivacyPolicy = findViewById(R.id.btn_privacy_policy)
    }

    /**
     * Search "addTestDeviceHashedId" in logcat after running the application
     * and past that device id for debug
     */
    private fun initConsent() {
        ConsentController(this).apply {
            initConsent("A69AF72EA9855046AD0439E4A6287ADF", object : ConsentCallback {
                override fun onAdsLoad(canRequestAd: Boolean) {
                    Log.d(TAG, "initConsent: onAdsLoad: canRequestAd:$canRequestAd")
                }

                override fun onConsentFormLoaded() {
                    Log.d(TAG, "onConsentFormLoaded: called")
                    this@apply.showConsentForm()
                }

                override fun onConsentFormDismissed() {
                    Log.d(TAG, "onConsentFormDismissed: called")
                }

                override fun onPolicyStatus(required: Boolean) {
                    Log.d(TAG, "onPolicyStatus: required:$required")
                }
            })
        }
    }

    private fun onPrivacyClick() {
        UserMessagingPlatform.showPrivacyOptionsForm(this) { formError ->
            formError?.let {
                Log.e(TAG, "onPrivacyClick: showPrivacyOptionsForm: ${formError.message}")
                Toast.makeText(this, "Operation failed, Try later", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}