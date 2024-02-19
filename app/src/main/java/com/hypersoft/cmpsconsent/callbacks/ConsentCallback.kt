package com.hypersoft.cmpsconsent.callbacks

import com.hypersoft.cmpsconsent.enums.CMPStatus


interface ConsentCallback {
    fun onReadyForInitialization()
    fun onInitializationSuccess()
    fun onInitializationError(error: String)
    fun onConsentFormAvailability(available:Boolean)
    fun onConsentFormLoadSuccess()
    fun onConsentFormLoadFailure(error: String)
    fun onRequestShowConsentForm()
    fun onConsentFormShowFailure(error: String)
    fun onConsentFormDismissed()
    fun onConsentStatus(status: CMPStatus)
    fun onPolicyStatus(status: CMPStatus)
}