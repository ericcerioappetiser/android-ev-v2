package com.appetiser.feature.report.network.models

import com.appetiser.core.domain.report.ReportRequest
import com.appetiser.feature.report.network.extensions.convertAttachmentsToMultiPartBody
import kotlinx.serialization.Serializable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class ReportRequestDTO(
    val type: String,
    val reportId: Long,
    val reasonId: Long?,
    val description: String,
    val attachmentPathList: List<String>
) {

    constructor(reportRequest: ReportRequest) : this(
        type = reportRequest.type,
        reportId = reportRequest.reportId,
        reasonId = reportRequest.reasonId,
        description = reportRequest.description,
        attachmentPathList = reportRequest.attachmentPathList
    )

    companion object {

        const val MULTI_PART = "multipart/form-data"
    }
}

fun ReportRequestDTO.toMultipart(): Pair<Map<String, RequestBody>, MutableList<MultipartBody.Part>> {
    val partMap = hashMapOf<String, RequestBody>()
    if (reportId > 0L) {
        partMap["report_id"] =
            reportId.toString().toRequestBody(ReportRequestDTO.MULTI_PART.toMediaTypeOrNull())
    }
    if (description.isNotEmpty()) {
        partMap["description"] =
            description.toRequestBody(ReportRequestDTO.MULTI_PART.toMediaTypeOrNull())
    }
    if (reasonId != -1L && reasonId != null) {
        partMap["reason_id"] =
            reasonId.toString().toRequestBody(ReportRequestDTO.MULTI_PART.toMediaTypeOrNull())
    }
    partMap["report_type"] = type.toRequestBody(ReportRequestDTO.MULTI_PART.toMediaTypeOrNull())

    // Convert file paths to MultipartBody.Part
    val attachments = attachmentPathList.convertAttachmentsToMultiPartBody("attachments")

    return partMap to attachments
}
