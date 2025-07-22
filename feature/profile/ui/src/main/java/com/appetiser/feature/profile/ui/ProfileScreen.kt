package com.appetiser.feature.profile.ui

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.compose.components.BaseplateCompoundItemNavigationItem
import com.appetiser.core.common.compose.components.CustomAsyncImage
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Radius
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.collectAsState

@Preview
@Composable
private fun ProfileScreenPreview() {
    BaseplateTheme {
        ProfileScreen(
            viewState = ProfileViewModel.ViewState(),
            toEditProfile = {},
            toAccountSettings = {},
            toAbout = {},
            toHelpCentre = {}
        )
    }
}

@Composable
fun ProfileScreen(
    toHelpCentre: () -> Unit,
    toEditProfile: () -> Unit,
    toAccountSettings: () -> Unit,
    toAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<ProfileViewModel>()
    val viewState by viewModel.collectAsState()

    ProfileScreen(
        viewState = viewState,
        toEditProfile = toEditProfile,
        toAccountSettings = toAccountSettings,
        toAbout = toAbout,
        modifier = modifier,
        toHelpCentre = toHelpCentre
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
fun ProfileScreen(
    viewState: ProfileViewModel.ViewState,
    toEditProfile: () -> Unit,
    toAccountSettings: () -> Unit,
    toAbout: () -> Unit,
    modifier: Modifier = Modifier,
    toHelpCentre: () -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { padding ->
        ProfileContent(
            photoUrl = viewState.photoUrl,
            fullName = viewState.fullName,
            username = viewState.username,
            toEditProfile = toEditProfile,
            toAccountSettings = toAccountSettings,
            toAbout = toAbout,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            toHelpCentre = toHelpCentre
        )
    }
}

@Composable
fun ProfileContent(
    photoUrl: String,
    fullName: String,
    username: String,
    toEditProfile: () -> Unit,
    toAccountSettings: () -> Unit,
    toAbout: () -> Unit,
    toHelpCentre: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(64.dp))
        CustomAsyncImage(
            imageUrl = photoUrl,
            contentDescription = null,
            modifier = Modifier.size(
                size = dimensionResource(R.dimen.image_profile)
            ),
            placeholder = painterResource(R.drawable.img_photo_placeholder)
        )
        Spacer(modifier = Modifier.height(Paddings.LARGE))
        Text(
            text = fullName,
            color = BaseplateTheme.colors.textPrimary,
            textAlign = TextAlign.Center,
            maxLines = 1,
            style = BaseplateTheme.typography.title3Bold
        )
        Spacer(modifier = Modifier.height(Paddings.MICRO))
        Text(
            text = username,
            color = BaseplateTheme.colors.textSecondary,
            style = BaseplateTheme.typography.subHeadRegular
        )
        Spacer(modifier = Modifier.height(Margins.XXXX_LARGE))
        ProfilePreferenceList(
            toEditProfile = toEditProfile,
            toAccountSettings = toAccountSettings,
            toAbout = toAbout,
            modifier = Modifier.padding(Paddings.LARGE),
            toHelpCentre = toHelpCentre
        )
    }
}

@Composable
fun ProfilePreferenceList(
    toEditProfile: () -> Unit,
    toAccountSettings: () -> Unit,
    toAbout: () -> Unit,
    modifier: Modifier = Modifier,
    toHelpCentre: () -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Radius.MEDIUM),
        color = BaseplateTheme.colors.surfaceBackground,
        border = BorderStroke(
            width = 1.dp,
            color = BaseplateTheme.colors.borderDefault
        )
    ) {
        Column {
            ProfilePreferenceItem(
                icon = ImageVector.vectorResource(R.drawable.ic_edit),
                title = stringResource(com.appetiser.core.common.compose.R.string.edit_profile),
                description = stringResource(R.string.update_personal_info_and_picture),
                modifier = Modifier.clickable(onClick = toEditProfile)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Paddings.LARGE),
                color = BaseplateTheme.colors.borderDefault
            )
            ProfilePreferenceItem(
                icon = ImageVector.vectorResource(R.drawable.ic_settings),
                title = stringResource(com.appetiser.core.common.compose.R.string.account_settings),
                description = stringResource(R.string.manage_account_preferences),
                modifier = Modifier.clickable(onClick = toAccountSettings)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Paddings.LARGE),
                color = BaseplateTheme.colors.borderDefault
            )
            ProfilePreferenceItem(
                icon = ImageVector.vectorResource(R.drawable.ic_card),
                title = stringResource(R.string.membership),
                description = stringResource(R.string.change_your_plan_and_payment),
                modifier = Modifier.clickable {}
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Paddings.LARGE),
                color = BaseplateTheme.colors.borderDefault
            )
            ProfilePreferenceItem(
                icon = ImageVector.vectorResource(R.drawable.ic_help),
                title = stringResource(R.string.help_centre),
                description = stringResource(R.string.share_your_feedback_or_questions),
                modifier = Modifier.clickable(onClick = toHelpCentre)
            )
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = Paddings.LARGE),
                color = BaseplateTheme.colors.borderDefault
            )
            ProfilePreferenceItem(
                icon = ImageVector.vectorResource(R.drawable.ic_about),
                title = stringResource(R.string.about),
                description = stringResource(R.string.terms_of_service_and_privacy),
                modifier = Modifier.clickable(onClick = toAbout)
            )
        }
    }
}

@Composable
fun ProfilePreferenceItem(
    icon: ImageVector,
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    BaseplateCompoundItemNavigationItem(
        modifier = modifier.padding(Paddings.LARGE),
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        leadingContentPadding = Paddings.LARGE,
        title = {
            Text(
                text = title,
                style = BaseplateTheme.typography.bodyRegular
            )
        },
        description = {
            Text(
                text = description,
                color = BaseplateTheme.colors.textSecondary,
                style = BaseplateTheme.typography.footnoteRegular
            )
        },
        trailingContent = {
            Icon(
                imageVector = ImageVector.vectorResource(com.appetiser.core.common.compose.R.drawable.ic_chevron_right),
                contentDescription = stringResource(R.string.go_to_x, title)
            )
        }
    )
}
