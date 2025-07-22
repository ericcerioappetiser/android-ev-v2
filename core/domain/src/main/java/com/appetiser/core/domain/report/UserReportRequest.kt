package com.appetiser.core.domain.report

data class UserReportRequest(
    val description: String,
    val photos: List<String>,
    val subjectText: String?
)
