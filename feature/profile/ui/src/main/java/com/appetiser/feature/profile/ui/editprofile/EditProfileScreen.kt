package com.appetiser.feature.profile.ui.editprofile

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.android.util.FileUtils
import com.appetiser.core.common.android.util.FileUtils.getFileFromUri
import com.appetiser.core.common.compose.components.BaseplateButton
import com.appetiser.core.common.compose.components.BaseplateTextButton
import com.appetiser.core.common.compose.components.BaseplateTextField
import com.appetiser.core.common.compose.components.CustomAsyncImage
import com.appetiser.core.common.compose.components.utils.ImagePickerOption
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.feature.profile.ui.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import java.io.File
import com.appetiser.core.common.compose.R as commonComposeR

@Preview
@Composable
private fun EditProfileScreenPreview() {
    BaseplateTheme {
        EditProfileScreen(
            viewState = EditProfileViewModel.ViewState(),
            toPreviousScreen = {},
            onPhotoSelect = {},
            onFirstNameChange = {},
            onLastNameChange = {},
            onBirthdateSelect = {},
            onSaveClick = {}
        )
    }
}

@Composable
fun EditProfileScreen(
    modifier: Modifier = Modifier,
    toPreviousScreen: () -> Unit = {},
) {
    val viewModel = hiltViewModel<EditProfileViewModel>()
    val viewState by viewModel.collectAsState()

    ConsumableSideEffect(viewState.profileSaved) { profileSaved ->
        if (profileSaved) {
            toPreviousScreen()
        }
    }

    EditProfileScreen(
        viewState = viewState,
        toPreviousScreen = toPreviousScreen,
        onPhotoSelect = {
            viewModel.action(
                EditProfileViewModel.ActionState.SelectProfilePhoto(
                    photoFile = it
                )
            )
        },
        onFirstNameChange = {
            viewModel.action(
                EditProfileViewModel.ActionState.ChangeFirstName(
                    firstName = it
                )
            )
        },
        onLastNameChange = {
            viewModel.action(
                EditProfileViewModel.ActionState.ChangeLastName(
                    lastName = it
                )
            )
        },
        onBirthdateSelect = {
            viewModel.action(
                EditProfileViewModel.ActionState.SelectBirthdate(
                    birthdateMillis = it
                )
            )
        },
        onSaveClick = {
            viewModel.action(EditProfileViewModel.ActionState.SaveEditedProfile)
        },
        modifier = modifier
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
fun EditProfileScreen(
    viewState: EditProfileViewModel.ViewState,
    toPreviousScreen: () -> Unit,
    onPhotoSelect: (File?) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onBirthdateSelect: (Long?) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        topBar = {
            EditProfileBar(
                title = stringResource(commonComposeR.string.edit_profile),
                toPreviousScreen = toPreviousScreen
            )
        }
    ) { padding ->
        EditProfileContent(
            photoUrl = viewState.photoUrl,
            onPhotoSelect = onPhotoSelect,
            firstName = viewState.firstName,
            onFirstNameChange = onFirstNameChange,
            lastName = viewState.lastName,
            onLastNameChange = onLastNameChange,
            birthdate = viewState.birthdate,
            onBirthdateSelect = onBirthdateSelect,
            onSaveClick = onSaveClick,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding),
            enableSave = viewState.enableSave
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileBar(
    title: String,
    modifier: Modifier = Modifier,
    toPreviousScreen: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = BaseplateTheme.typography.bodySemiBold
            )
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = toPreviousScreen) {
                Icon(
                    imageVector = ImageVector.vectorResource(com.appetiser.core.common.compose.R.drawable.ic_back),
                    contentDescription = stringResource(com.appetiser.core.common.compose.R.string.navigate_up)
                )
            }
        }
    )
}

@Composable
fun EditProfileContent(
    photoUrl: String,
    onPhotoSelect: (File?) -> Unit,
    firstName: String,
    onFirstNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    birthdate: LocalDate?,
    onBirthdateSelect: (Long?) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    enableSave: Boolean = false
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EditPhoto(
            photoUrl = photoUrl,
            modifier = Modifier.padding(
                top = Paddings.LARGE,
                bottom = Paddings.XXX_LARGE
            ),
            onPhotoSelect = onPhotoSelect
        )
        BaseplateTextField(
            value = firstName,
            onValueChange = onFirstNameChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Paddings.LARGE),
            label = {
                Text(
                    text = stringResource(R.string.first_name),
                    style = BaseplateTheme.typography.subHeadRegular
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            singleLine = true,
            unfocusedLabelColor = BaseplateTheme.colors.textSecondary
        )
        Spacer(modifier = Modifier.height(Paddings.LARGE))
        BaseplateTextField(
            value = lastName,
            onValueChange = onLastNameChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Paddings.LARGE),
            label = {
                Text(
                    text = stringResource(R.string.last_name),
                    style = BaseplateTheme.typography.subHeadRegular
                )
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
            singleLine = true,
            unfocusedLabelColor = BaseplateTheme.colors.textSecondary
        )
        Spacer(modifier = Modifier.height(Paddings.LARGE))
        EditBirthdate(
            birthdate = birthdate,
            onBirthdateSelect = onBirthdateSelect,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Paddings.LARGE),
        )
        Spacer(modifier = Modifier.height(Paddings.XXX_LARGE))
        Spacer(modifier = Modifier.weight(1.0F))
        BaseplateButton(
            text = stringResource(R.string.save),
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = Paddings.LARGE,
                    end = Paddings.LARGE,
                    bottom = Paddings.LARGE
                ),
            enabled = enableSave
        )
    }
}

@Composable
fun EditPhoto(
    photoUrl: String,
    modifier: Modifier = Modifier,
    onPhotoSelect: (File?) -> Unit = {}
) {
    var showOptions by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        CustomAsyncImage(
            imageUrl = photoUrl,
            contentDescription = null,
            modifier = Modifier.size(
                size = dimensionResource(R.dimen.image_edit_profile)
            ),
            placeholder = painterResource(R.drawable.img_photo_placeholder)
        )
        FilledIconButton(
            onClick = {
                showOptions = true
            },
            modifier = Modifier.align(Alignment.BottomEnd),
            shape = MaterialTheme.shapes.small
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_edit),
                contentDescription = stringResource(R.string.edit_profile_photo),
                tint = Color.White
            )
        }
    }
    EditPhotoOptions(
        showOptions = showOptions,
        onPhotoSelect = {
            onPhotoSelect(it)
            showOptions = false
        },
        onOptionsDismiss = {
            showOptions = false
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EditPhotoOptions(
    showOptions: Boolean,
    modifier: Modifier = Modifier,
    onPhotoSelect: (File?) -> Unit = {},
    onOptionsDismiss: () -> Unit = {}
) {
    val context = LocalContext.current
    val cameraUri = FileUtils.createImageUri(context)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { saved ->
        if (saved) {
            onPhotoSelect(context.getFileFromUri(cameraUri))
        }
    }
    val cameraPermission = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    ) { granted ->
        if (granted) {
            cameraLauncher.launch(input = cameraUri)
        }
    }
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { galleryUri ->
        galleryUri?.let {
            onPhotoSelect(context.getFileFromUri(galleryUri))
        }
    }
    val options = listOf(
        ImagePickerOption.TakePicture {
            cameraPermission.launchPermissionRequest()
        },
        ImagePickerOption.FromGallery {
            galleryLauncher.launch(
                input = PickVisualMediaRequest(
                    ActivityResultContracts.PickVisualMedia.ImageOnly
                )
            )
        }
    )

    if (showOptions) {
        BasicAlertDialog(onDismissRequest = onOptionsDismiss) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(BaseplateTheme.colors.surfaceBackground)
                    .padding(Paddings.X_LARGE),
                horizontalAlignment = Alignment.End
            ) {
                options.forEach { option ->
                    BaseplateTextButton(
                        onClick = option.onClick,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = stringResource(option.label),
                            modifier = Modifier.fillMaxWidth(),
                            color = BaseplateTheme.colors.textPrimary,
                            style = BaseplateTheme.typography.bodyRegular
                        )
                    }
                }
                Spacer(modifier = Modifier.height(Paddings.X_LARGE))
                BaseplateTextButton(onClick = onOptionsDismiss) {
                    Text(
                        text = stringResource(com.appetiser.core.common.compose.R.string.cancel),
                        style = BaseplateTheme.typography.bodyBold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, FormatStringsInDatetimeFormats::class)
@Composable
fun EditBirthdate(
    birthdate: LocalDate?,
    onBirthdateSelect: (Long?) -> Unit,
    modifier: Modifier = Modifier
) {
    var showPicker by remember { mutableStateOf(false) }
    val pickerState = rememberDatePickerState()
    val birthdatePattern = LocalDate.Format {
        byUnicodePattern("dd / MM / yyyy")
    }

    BaseplateTextField(
        value = birthdate?.format(birthdatePattern).orEmpty(),
        onValueChange = {},
        modifier = modifier.pointerInput(birthdate) {
            awaitEachGesture {
                awaitFirstDown(pass = PointerEventPass.Initial)
                if (waitForUpOrCancellation(pass = PointerEventPass.Initial) != null) {
                    showPicker = true
                }
            }
        },
        readOnly = true,
        label = {
            Text(
                text = stringResource(R.string.date_of_birth),
                style = BaseplateTheme.typography.subHeadRegular
            )
        },
        unfocusedLabelColor = BaseplateTheme.colors.textSecondary
    )

    if (showPicker) {
        DatePickerDialog(
            onDismissRequest = {
                showPicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showPicker = false
                        onBirthdateSelect(pickerState.selectedDateMillis)
                    }
                ) {
                    Text(
                        text = "OK",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = BaseplateTheme.colors.surfaceBackground,
            )
        ) {
            DatePicker(
                state = pickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = BaseplateTheme.colors.surfaceBackground,
                )
            )
        }
    }
}
