package com.appetiser.onboarding.ui.profile

import android.Manifest
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.android.util.PathUtils
import com.appetiser.core.common.compose.components.BaseplateBottomSheetOptions
import com.appetiser.core.common.compose.components.BaseplateButton
import com.appetiser.core.common.compose.components.BaseplateCircularIndicator
import com.appetiser.core.common.compose.components.BaseplateProgressBar
import com.appetiser.core.common.compose.components.BottomSheetOption
import com.appetiser.core.common.compose.components.CenterTopBar
import com.appetiser.core.common.compose.components.CustomAsyncImage
import com.appetiser.core.common.compose.components.snackbar.BaseplateSnackbarHost
import com.appetiser.core.common.compose.components.utils.ImagePickerOption
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.core.mediapicker.MediaPickerHelper
import com.appetiser.core.mediapicker.models.MediaPickerOptions
import com.appetiser.core.mediapicker.rememberMediaPickerLaunchers
import com.appetiser.core.network.utils.getThrowableError
import com.appetiser.feature.onboarding.ui.R
import com.appetiser.feature.permission.provider.ui.PermissionRationaleDialog
import com.appetiser.onboarding.ui.profile.YourProfileViewModel.ActionState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import com.appetiser.core.common.android.R as commonAndroidR

@Composable
fun YourProfileScreenRoot(
    viewModel: YourProfileViewModel = hiltViewModel(),
    onProfileInputDone: () -> Unit,
    onNavigateUp: () -> Unit,
    onNavigateToHome: () -> Unit,
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    YourProfileScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = {
            viewModel.action(it)
        },
        onNavigateUp = onNavigateUp
    )

    ConsumableSideEffect(state.onOnboardingCompleted) {
        onProfileInputDone()
    }
    ConsumableSideEffect(state.shouldShowSuccessfulUploadSnackbar) {
        snackbarHostState.showSnackbar(
            message = context.getString(R.string.upload_successful),
            actionLabel = context.getString(R.string.hide)
        )
    }
    ConsumableSideEffect(consumable = state.onOnboardingCompleted) {
        onNavigateToHome()
    }

    ConsumableSideEffect(state.error) {
        snackbarHostState.showSnackbar(
            message = it.getThrowableError(),
            actionLabel = context.getString(R.string.hide)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun YourProfileScreen(
    modifier: Modifier = Modifier,
    progress: Float = 1F,
    state: YourProfileViewModel.YourProfileViewState,
    snackbarHostState: SnackbarHostState,
    onAction: (ActionState) -> Unit,
    onNavigateUp: () -> Unit,
) {
    val context = LocalContext.current
    val mediaPickerHelper = remember(context) {
        MediaPickerHelper(
            context = context,
            options = MediaPickerOptions(
                authority = "${context.packageName}.fileprovider",
                providerFilePath = ".",
            )
        )
    }
    val coroutineScope = rememberCoroutineScope()

    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded, skipHiddenState = false
        )
    )
    var shouldShowCallPhoneRationaleDialog by remember { mutableStateOf(false) }
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val mediaPickerLaunchers = rememberMediaPickerLaunchers(
        mediaPickerHelper = mediaPickerHelper,
        onError = {},
        onResult = { result ->
            result.onSuccess { success ->
                when (success) {
                    is List<*> -> {
                        if (success.all { it is Uri }) {
                            val uris = success as List<Uri>
                            uris.forEach { uri ->
                                val file = PathUtils.getFileFromUri(context = context, uri)
                                file?.let {
                                    onAction(ActionState.OnUpdateMediaPath(it))
                                    coroutineScope.launch {
                                        scaffoldState.bottomSheetState.hide()
                                    }
                                }
                            }
                        } else {
                            coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                        }
                    }

                    is Uri -> {
                        val file = PathUtils.getFileFromUri(context = context, success)
                        file?.let {
                            onAction(ActionState.OnUpdateMediaPath(it))
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.hide()
                            }
                        }
                    }

                    else -> {
                        coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
                    }
                }
            }
            coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
        },
        onGranted = {
            if (scaffoldState.bottomSheetState.isVisible.not()) {
                coroutineScope.launch {
                    scaffoldState.bottomSheetState.expand()
                }
            }
        }, onDenied = { shouldShowRationale ->
            shouldShowCallPhoneRationaleDialog = shouldShowRationale
            if (shouldShowCallPhoneRationaleDialog) {
                PermissionRationaleDialog(
                    title = stringResource(id = R.string.camera_permission_title),
                    body = stringResource(id = R.string.camera_permission_message),
                    icon = Icons.Default.Call,
                    onDismiss = {
                        shouldShowCallPhoneRationaleDialog = false
                    }
                ) {
                    cameraPermissionState.launchPermissionRequest()
                }
            }
        }
    )
    ConsumableSideEffect(state.error) {
        snackbarHostState.showSnackbar(
            message = it.getThrowableError(),
            actionLabel = context.getString(R.string.hide)
        )
    }

    fun dismissAndAct(action: () -> Unit) {
        coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
        action()
    }


    ConsumableSideEffect(state.shouldShowSuccessfulUploadSnackbar) {
        snackbarHostState.showSnackbar(
            message = context.getString(R.string.upload_successful),
            actionLabel = context.getString(R.string.hide)
        )
    }

    ConsumableSideEffect(state.error) {
        snackbarHostState.showSnackbar(
            message = it.getThrowableError(),
            actionLabel = context.getString(R.string.hide)
        )
    }

    val options = listOf(
        ImagePickerOption.TakePicture {
            dismissAndAct {
            }
            mediaPickerLaunchers.takePictureAction()
        },
        ImagePickerOption.FromGallery {
            dismissAndAct {
            }
            mediaPickerLaunchers.pickMultipleMediaAction()
        },
        ImagePickerOption.Cancel {
            dismissAndAct {
            }
        }
    )

    Scaffold(
        snackbarHost = {
            BaseplateSnackbarHost(hostState = snackbarHostState)
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (state.isLoading) {
                BaseplateCircularIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .zIndex(1f)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CenterTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = "",
                    navigationIcon = {
                        IconButton(onClick = onNavigateUp) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                                tint = BaseplateTheme.colors.iconDefault,
                                modifier = Modifier.padding(horizontal = Paddings.SMALL),
                            )
                        }
                    },
                    actions = {
                        TextButton(onClick = { onAction(ActionState.OnClickSkip) }) {
                            Text(
                                text = stringResource(R.string.skip),
                                modifier = Modifier.padding(end = Paddings.LARGE),
                                color = BaseplateTheme.colors.textTertiary,
                                style = BaseplateTheme.typography.bodyBold
                            )
                        }
                    },
                )


                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(
                            horizontal = Paddings.LARGE
                        )
                ) {
                    Spacer(modifier = Modifier.height(Margins.MEDIUM))
                    Text(
                        text = stringResource(id = R.string.your_profile),
                        style = BaseplateTheme.typography.title1,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    val height = Modifier.height(Margins.MEDIUM)
                    Spacer(modifier = Modifier.height(Paddings.LARGE))
                    BaseplateProgressBar(
                        modifier = height,
                        progress = progress,
                    )
                    Spacer(modifier = Modifier.height(Paddings.MEDIUM))
                    Text(
                        text = stringResource(id = R.string.your_profile_description),
                        style = BaseplateTheme.typography.bodyRegular
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) {
                        CustomAsyncImage(
                            contentDescription = stringResource(id = R.string.your_profile),
                            imageUrl = state.file?.absolutePath.orEmpty(),
                            placeholder = painterResource(commonAndroidR.drawable.image_photo_placeholder),
                            modifier = Modifier.size(134.dp)
                        )
                        IconButton(
                            onClick = { coroutineScope.launch { scaffoldState.bottomSheetState.expand() } },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(32.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_plus_button),
                                contentDescription = null,
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
            }
            BaseplateButton(
                modifier = Modifier
                    .padding()
                    .fillMaxWidth()
                    .align(alignment = Alignment.BottomCenter)
                    .padding(start = Paddings.LARGE, end = Paddings.LARGE, bottom = Paddings.X_LARGE),
                enabled = state.file != null,
                onClick = {
                    onAction(ActionState.ClickContinue)
                },
                text = stringResource(id = R.string.continue_text)
            )
        }
    }
    BaseplateBottomSheetOptions(
        scaffoldState = scaffoldState,
        sheetContent = {
            options
                .forEachIndexed { index, option ->
                    BottomSheetOption(
                        text = stringResource(option.label),
                        onClick = option.onClick
                    )
                    if (index != options.lastIndex) {
                        HorizontalDivider(
                            color = Color.LightGray,
                            thickness = 1.dp
                        )
                    }
                }
        }
    ) {
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showBackground = true,
)
@Composable
fun YourProfileScreenPreview() {
    BaseplateTheme(isDarkTheme = false) {
        YourProfileScreen(
            modifier = Modifier.fillMaxSize(),
            snackbarHostState = remember { SnackbarHostState() },
            onAction = {},
            state = YourProfileViewModel.YourProfileViewState(),
            onNavigateUp = {}
        )
    }
}
