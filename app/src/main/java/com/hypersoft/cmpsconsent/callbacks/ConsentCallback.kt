package com.hypersoft.cmpsconsent.callbacks

/**
 * @Author: Muhammad Yaqoob
 * @Date: 29,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */

interface ConsentCallback {
    fun onAdsLoad(canRequestAd: Boolean) {}
    fun onConsentFormShow() {}
    fun onConsentFormDismissed() {}
    fun onPolicyStatus(required: Boolean) {}
}