package com.appetiser.feature.report.network.models

import com.appetiser.core.domain.report.UserReportRequest
import com.appetiser.feature.report.network.extensions.convertAttachmentsToMultiPartBody
import com.appetiser.feature.report.network.models.UserReportRequestDTO.Companion.MULTI_PART
import com.appetiser.feature.report.network.models.UserReportRequestDTO.Companion.REPORT_TYPE
import kotlinx.serialization.Serializable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class UserReportRequestDTO(
    val description: String,
    val photos: List<String>,
    val subjectText: String?
) {

    constructor(userReportRequest: UserReportRequest) : this(
        description = userReportRequest.description,
        photos = userReportRequest.photos,
        subjectText = userReportRequest.subjectText
    )

    companion object {

        const val REPORT_TYPE = "app"
        const val MULTI_PART = "multipart/form-data"
    }
}

fun UserReportRequestDTO.toMultipart(): Pair<Map<String, RequestBody>, MutableList<MultipartBody.Part>> {
    val partMap = hashMapOf<String, RequestBody>()
    if (description.isNotEmpty() && subjectText?.isNotEmpty() == true) {
        partMap["description"] =
            description.toRequestBody(MULTI_PART.toMediaTypeOrNull())
        partMap["subject"] =
            subjectText.toRequestBody(MULTI_PART.toMediaTypeOrNull())
        partMap["report_type"] =
            REPORT_TYPE.toRequestBody(MULTI_PART.toMediaTypeOrNull())
    }
    // Convert file paths to MultipartBody.Part
    val attachments = photos.convertAttachmentsToMultiPartBody("attachments")

    return partMap to attachments
}
