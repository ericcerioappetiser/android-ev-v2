package com.appetiser.feature.permission.provider.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import com.appetiser.feature.permission.provider.R

/**
 * A helper class that encapsulates the logic for requesting multiple Android runtime permissions
 * using [ActivityResultLauncher] and [ActivityResultContracts.RequestMultiplePermissions].
 *
 * Usage:
 * 1. Construct or remember an instance of [PermissionProvider], passing the launcher and the list of permissions.
 * 2. Call [requestPermission] to launch the system dialog for all specified permissions.
 *
 * This class does not automatically handle callback events such as "all permissions granted" or
 * "some permissions denied." Instead, you should set up the [ActivityResultLauncher] with an
 * `onResult` callback that checks which permissions are granted or denied and then performs
 * the appropriate actions (e.g., showing a rationale dialog or navigating to app settings).
 *
 * @property launcher An [ActivityResultLauncher] used to request multiple permissions at once.
 * This launcher must be configured with [ActivityResultContracts.RequestMultiplePermissions].
 *
 * @property permissions A list of Android permission strings (e.g., [android.Manifest.permission.CAMERA])
 * that you want to request as a group.
 *
 * @constructor Creates a [PermissionProvider] instance using the provided [launcher] and [permissions].
 */
class PermissionProvider(
    private val launcher: ActivityResultLauncher<Array<String>>,
    private val permissions: List<String>
) {

    /**
     * Initiates the system dialog for requesting all specified [permissions].
     *
     * Call this method at the moment you wish to prompt the user for runtime permissions.
     * Make sure that you have configured the [launcher] to handle the results,
     * typically by checking granted/denied states in its callback.
     */
    fun requestPermission() {
        launcher.launch(permissions.toTypedArray())
    }
}

/**
 * Creates and remembers a [PermissionProvider] for requesting multiple permissions.
 * - [onGranted]: Called if all permissions are granted.
 * - [onDenied]: Called if any are denied, passing [DeniedPermission] details.
 *
 * Example:
 * ```
 * val provider = rememberPermissionProvider(
 *     permissions = listOf(
 *         Manifest.permission.CAMERA,
 *         Manifest.permission.READ_EXTERNAL_STORAGE
 *     ),
 *     onDenied = { deniedList ->
 *         deniedList.forEach { denied ->
 *             if (denied.state == DenialState.DENIED) {
 *                 // Show rationale
 *             } else {
 *                 // Show a toast
 *             }
 *         }
 *     },
 *     onGranted = { }
 * )
 * ```
 *
 * @param permissions Permissions to request (e.g., [android.Manifest.permission.CAMERA]).
 * @param onGranted Called if all permissions are granted.
 * @param onDenied Called with a list of [DeniedPermission] for any denied permissions.
 * @return A [PermissionProvider] to launch the request via [PermissionProvider.requestPermission].
 */
@Composable
fun rememberPermissionProvider(
    permissions: List<String>,
    onDenied: (List<DeniedPermission>) -> Unit,
    onGranted: () -> Unit
): PermissionProvider {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { resultMap: Map<String, Boolean> ->
            val deniedPermissions = resultMap
                .filterValues { isGranted -> !isGranted }
                .keys

            if (deniedPermissions.isEmpty()) {
                onGranted()
            } else {
                val deniedList = deniedPermissions.map { permission ->
                    val shouldShowRationale = ActivityCompat
                        .shouldShowRequestPermissionRationale(context.getActivity(), permission)
                    DeniedPermission(
                        permission = permission,
                        shouldShowRationale = shouldShowRationale
                    )
                }
                onDenied(deniedList)
            }
        }
    )

    return remember(launcher, permissions) {
        PermissionProvider(launcher, permissions)
    }
}

private fun Context.getActivity(): Activity {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    throw IllegalStateException(getString(R.string.permissions_should_be_called_in_the_context_of_an_activity))
}
