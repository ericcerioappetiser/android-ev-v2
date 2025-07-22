package com.appetiser.feature.report.ui


import android.Manifest
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.android.util.PathUtils
import com.appetiser.core.common.compose.components.BaseplateButton
import com.appetiser.core.common.compose.components.BaseplateSnackBarContainer
import com.appetiser.core.common.compose.components.BaseplateTextField
import com.appetiser.core.common.compose.components.BottomSheetOption
import com.appetiser.core.common.compose.components.CenterTopBar
import com.appetiser.core.common.compose.components.CustomAsyncImage
import com.appetiser.core.common.compose.components.utils.ImagePickerOption
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Radius
import com.appetiser.core.common.compose.dimensions.Sizes
import com.appetiser.core.common.compose.extensions.dottedBorder
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.core.mediapicker.MediaPickerHelper
import com.appetiser.core.mediapicker.models.MediaPickerOptions
import com.appetiser.core.mediapicker.rememberMediaPickerLaunchers
import com.appetiser.core.network.utils.getThrowableError
import com.appetiser.feature.permission.provider.ui.PermissionRationaleDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch
import com.appetiser.core.common.android.R as commonAndroidR
import com.appetiser.feature.onboarding.ui.R as onboardingUiR

@Composable
fun ReportAProblemScreen(
    modifier: Modifier,
    viewModel: ReportAProblemViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
) {
    val state by viewModel.collectAsState()

    ReportAProblemScreen(
        onNavigateBack = onNavigateBack,
        modifier = modifier,
        state = state,
        onSubjectValueChange = {
            viewModel.action(ReportAProblemViewModel.ActionState.OnSubjectChange(it))
        },
        onDescriptionValueChange = {
            viewModel.action(ReportAProblemViewModel.ActionState.OnDescriptionChange(it))
        },
        onAddImage = {
            viewModel.action(ReportAProblemViewModel.ActionState.OnAddImage(it))
        },
        onRemovePhoto = {
            viewModel.action(ReportAProblemViewModel.ActionState.OnRemoveImage(it))
        },
        onSubmitReport = {
            viewModel.action(ReportAProblemViewModel.ActionState.OnReportAProblem)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ReportAProblemScreen(
    modifier: Modifier,
    onNavigateBack: () -> Unit = {},
    state: ReportAProblemViewModel.ReportAProblemViewState,
    onSubjectValueChange: (String) -> Unit = {},
    onDescriptionValueChange: (String) -> Unit = {},
    onAddImage: (String) -> Unit = {},
    onRemovePhoto: (String) -> Unit = {},
    onSubmitReport: () -> Unit = {},
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden, skipHiddenState = false
        )
    )
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val mediaPickerHelper = remember(context) {
        MediaPickerHelper(
            context = context,
            options = MediaPickerOptions(
                authority = "${context.packageName}.fileprovider",
                providerFilePath = ".",
            )
        )
    }

    val isBottomSheetVisible by rememberUpdatedState(scaffoldState.bottomSheetState.isVisible)

    var shouldShowCallPhoneRationaleDialog by remember { mutableStateOf(false) }
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val mediaPickerLaunchers = rememberMediaPickerLaunchers(
        mediaPickerHelper = mediaPickerHelper,
        onError = {},
        onResult = { result ->
            result.onSuccess {
                when (it) {
                    is Uri -> {
                        val path = PathUtils.getFilePathFromUri(context, it as Uri)
                        if (path != null) {
                            onAddImage.invoke(path)
                        }
                    }

                    is List<*> -> {
                        it.forEach { uri ->
                            val path = PathUtils.getFilePathFromUri(context, uri as Uri)
                            if (path != null) {
                                onAddImage.invoke(path)
                            }
                        }
                    }
                }
            }
        },
        onGranted = {
//            if (scaffoldState.bottomSheetState.isVisible.not()) {
//                coroutineScope.launch {
//                    scaffoldState.bottomSheetState.expand()
//                }
//            }
        }, onDenied = { shouldShowRationale ->
            shouldShowCallPhoneRationaleDialog = shouldShowRationale
            if (shouldShowCallPhoneRationaleDialog) {
                PermissionRationaleDialog(
                    title = stringResource(id = onboardingUiR.string.camera_permission_title),
                    body = stringResource(id = onboardingUiR.string.camera_permission_message),
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

    val options = listOf(
        ImagePickerOption.TakePicture {
            coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
            mediaPickerLaunchers.takePictureAction()
        },
        ImagePickerOption.FromGallery {
            coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
            mediaPickerLaunchers.pickMultipleMediaAction()
        },
        ImagePickerOption.Cancel {
            coroutineScope.launch { scaffoldState.bottomSheetState.hide() }
        }
    )

    val snackbarHostState = remember { SnackbarHostState() }

    ConsumableSideEffect(state.successReport) {
        snackbarHostState.showSnackbar(
            message = context.getString(R.string.success_report_message),
            actionLabel = context.getString(R.string.hide),
            duration = SnackbarDuration.Short
        )
        onNavigateBack.invoke()
    }

    ConsumableSideEffect(state.error) {
        snackbarHostState.showSnackbar(
            message = it.getThrowableError(),
            actionLabel = context.getString(R.string.hide),
            duration = SnackbarDuration.Short
        )
    }

    BaseplateSnackBarContainer(
        modifier = Modifier.fillMaxSize(),
        snackbarHostState = snackbarHostState,
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = modifier
                    .size(Sizes.XX_LARGE),
                color = BaseplateTheme.colors.iconBrand
            )
        }

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = Paddings.LARGE)
                .imePadding(),
            verticalArrangement = Arrangement.Top
        ) {
            CenterTopBar(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.report_problem),
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(commonAndroidR.string.back),
                            tint = BaseplateTheme.colors.iconDefault,
                            modifier = Modifier.padding(horizontal = Paddings.SMALL),
                        )
                    }
                }
            )

            Box(
                modifier = modifier.height(Margins.LARGE)
            )

            BaseplateTextField(
                modifier = modifier.fillMaxWidth(),
                value = state.subject,
                singleLine = true,
                label = {
                    Text(
                        text = stringResource(R.string.subject),
                        style = BaseplateTheme.typography.bodyRegular
                    )
                },
                onValueChange = onSubjectValueChange,
                enabled = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            Box(
                modifier.height(
                    height = Margins.LARGE
                )
            )
            BaseplateTextField(
                value = state.description,
                label = {
                    Text(
                        text = stringResource(id = R.string.description),
                        style = BaseplateTheme.typography.bodyRegular
                    )
                },
                onValueChange = onDescriptionValueChange,
                enabled = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                contentHeight = 150.dp,
                maxLength = integerResource(id = R.integer.description_max_length),
                showMaxCounter = true
            )
            Box(
                modifier.height(
                    height = Margins.LARGE
                )
            )
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(Sizes.SMALL)
            ) {
                item {
                    IconButton(
                        onClick = {
                            keyboardController?.hide()
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.show()
                            }
                        },
                        modifier = modifier
                            .size(dimensionResource(R.dimen.attachment_image_size))
                            .dottedBorder(
                                width = 1.dp,
                                color = BaseplateTheme.colors.borderBrand,
                                radius = Radius.LARGE,
                                interval = Sizes.MICRO
                            )
                    ) {
                        Icon(
                            painter = rememberVectorPainter(Icons.Default.Add),
                            contentDescription = context.getString(R.string.add_image),
                            tint = BaseplateTheme.colors.borderBrand,
                            modifier = modifier.size(Sizes.XX_LARGE)
                        )
                    }
                }

                items(state.images) { image ->
                    Box(Modifier.size(dimensionResource(R.dimen.attachment_image_size))) {
                        CustomAsyncImage(
                            contentDescription = context.getString(R.string.image_attachment),
                            imageUrl = image,
                            shape = RoundedCornerShape(Radius.LARGE),
                            placeholder = painterResource(commonAndroidR.drawable.image_photo_placeholder),
                            modifier = Modifier.fillMaxSize() // Fill the Box
                        )

                        Icon(
                            painter = rememberVectorPainter(Icons.Default.Close),
                            contentDescription = context.getString(R.string.remove_image),
                            tint = BaseplateTheme.colors.iconBrand, // Or a suitable color
                            modifier = modifier
                                .padding(Paddings.SMALL)
                                .align(Alignment.TopEnd)
                                .size(Sizes.X_LARGE)
                                .clickable {
                                    onRemovePhoto.invoke(image)
                                }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))

            BaseplateButton(
                shape = RoundedCornerShape(Paddings.MEDIUM),
                modifier = Modifier.fillMaxWidth(),
                onClick = onSubmitReport,
                text = stringResource(id = R.string.report_problem),
                enabled = state.isContinueButton && !state.isLoading
            )
        }
        if (isBottomSheetVisible) {
            ModalBottomSheet(
                onDismissRequest = {
                    coroutineScope.launch { scaffoldState.bottomSheetState.hide() } // Hide the sheet when dismissed
                },
                sheetState = scaffoldState.bottomSheetState
            ) {
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
        }
    }
}

@Preview
@Composable
fun ReportAProblemScreenPreview() {
    ReportAProblemScreen(
        Modifier,
        onNavigateBack = {},
        onAddImage = {},
        onSubjectValueChange = {},
        state = ReportAProblemViewModel.ReportAProblemViewState()
    )
}
