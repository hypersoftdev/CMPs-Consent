package com.hypersoft.cmpsconsent.callbacks


interface ConsentCallback {
    fun onAdsLoad(canRequestAd:Boolean){}
    fun onConsentFormShow(){}
    fun onConsentFormDismissed(){}
    fun onPolicyStatus(required:Boolean){}
}