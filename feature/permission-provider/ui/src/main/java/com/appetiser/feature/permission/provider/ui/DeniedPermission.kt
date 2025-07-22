package com.appetiser.feature.permission.provider.ui

/**
 * Represents a denied permission along with whether a rationale should be shown.
 *
 * @property permission The denied permission string (e.g., [android.Manifest.permission.CAMERA]).
 * @property shouldShowRationale Indicates if a rationale should be displayed for this permission
 * (i.e., if the user did not select "Don't ask again").
 */
data class DeniedPermission(
    val permission: String,
    val shouldShowRationale: Boolean
)

/**
 * Checks if at least one [DeniedPermission] in the list indicates that a rationale should be shown.
 *
 * @return `true` if any permission requires a rationale; `false` otherwise.
 */
fun List<DeniedPermission>.hasRationale(): Boolean = this.any { it.shouldShowRationale }
