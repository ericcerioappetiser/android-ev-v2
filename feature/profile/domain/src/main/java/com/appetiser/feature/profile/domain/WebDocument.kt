package com.appetiser.feature.profile.domain

enum class WebDocument(
    val title: String,
    val path: String
) {

    SERVICE_TERMS(
        title = "Terms of Service",
        path = "pages/terms_of_service"
    ),
    PRIVACY_POLICY(
        title = "Privacy Policy",
        path = "pages/privacy"
    )
}
