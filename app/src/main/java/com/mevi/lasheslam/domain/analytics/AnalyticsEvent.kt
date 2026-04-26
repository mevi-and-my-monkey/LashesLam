package com.mevi.lasheslam.domain.analytics

sealed class AnalyticsEvent(val name: String) {

    data object LoginClick : AnalyticsEvent("login_click")

    data object RegisterClick : AnalyticsEvent("register_click")

    data object GoogleLoginClick : AnalyticsEvent("google_login_click")

    data class RegisterSuccess(val method: String) : AnalyticsEvent("register_success")

    data class RegisterError(val method: String) : AnalyticsEvent("register_error")

    data class LoginSuccess(val method: String) : AnalyticsEvent("login_success")

    data class LoginError(val message: String) : AnalyticsEvent("login_error")

    data object RegisterBottomShow : AnalyticsEvent("register_bottom_show")

    data object RegisterBottomHide: AnalyticsEvent("register_bottom_hide")

    data object LoginBottomShow : AnalyticsEvent("login_bottom_show")

    data object LoginBottomHide : AnalyticsEvent("login_bottom_hide")

    data class ScreenView(val screen: String) : AnalyticsEvent("screen_view")
}