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

    // HOME
    data class BottomSelection(val section: String) : AnalyticsEvent("bottom_selected")

    data object FloatingHomeClick : AnalyticsEvent("floating_home_click")

    data object HomeOptionBottomShow : AnalyticsEvent("home_option_bottom_show")

    data object HomeOptionBottomHide : AnalyticsEvent("home_option_bottom_hide")

    data object AddServiceShow : AnalyticsEvent("add_service_show")

    data object AddServiceHide : AnalyticsEvent("add_service_hide")

    data object AddProductShow : AnalyticsEvent("add_product_show")

    data object AddProductHide : AnalyticsEvent("add_product_hide")

    data object IconHeaderClick : AnalyticsEvent("icon_header_click")

    data object IconSearchClick : AnalyticsEvent("icon_search_click")

    data class CourseClick(val course: String) : AnalyticsEvent("course_click")

    data class ProductClick(val product: String) : AnalyticsEvent("product_click")

    data class CategorySelected(val category: String) : AnalyticsEvent("category_selected")

    data class AddToCartClick(val product: String) : AnalyticsEvent("add_to_cart_click")

    data class FavoriteClick(val type: String) : AnalyticsEvent("add_to_favorite_click")

    // GENERAL

    data class SectionSelected(val section: String) : AnalyticsEvent("section_selected")

    data class ScreenView(val screen: String) : AnalyticsEvent("screen_view")
}