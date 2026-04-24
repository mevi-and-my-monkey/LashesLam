package com.mevi.lasheslam.domain.analytics

sealed class AnalyticsEvent(val name: String) {

    data object LoginClick : AnalyticsEvent("login_click")

    data object RegisterClick : AnalyticsEvent("register_click")

    data object GoogleLoginClick : AnalyticsEvent("google_login_click")

    data class RegisterSuccess(val method: String) : AnalyticsEvent("register_success")

    data class RegisterError(val method: String) : AnalyticsEvent("register_error")

    data class LoginSuccess(val method: String) : AnalyticsEvent("login_success")

    data class LoginError(val message: String) : AnalyticsEvent("login_error")

    data class ScreenView(val screen: String) : AnalyticsEvent("screen_view")
}