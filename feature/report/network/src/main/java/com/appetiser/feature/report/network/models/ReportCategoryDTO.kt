package com.appetiser.feature.report.network.models

import com.appetiser.core.domain.report.ReportCategory
import kotlinx.serialization.Serializable

@Serializable
data class ReportCategoryDTO(
    val id: Int,
    val label: String
) {

    companion object {

        fun ReportCategoryDTO.toDomain(): ReportCategory = ReportCategory(
            id = id,
            label = label
        )
    }
}
