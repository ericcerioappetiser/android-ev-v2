package com.appetiser.core.domain.report

data class ReportRequest(
    val type: String,
    val reportId: Long,
    val reasonId: Long?,
    val description: String,
    val attachmentPathList: List<String>
)

