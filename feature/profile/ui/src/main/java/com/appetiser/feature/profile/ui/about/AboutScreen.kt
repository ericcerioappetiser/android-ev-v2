package com.appetiser.feature.profile.ui.about

import android.content.pm.PackageManager
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.feature.profile.domain.WebDocument
import com.appetiser.feature.profile.ui.R
import com.appetiser.feature.profile.ui.accountsettings.AccountSettingItem
import com.appetiser.feature.profile.ui.editprofile.EditProfileBar
import com.appetiser.core.common.compose.R as CommonComposeR

@Composable
fun AboutScreen(
    toPreviousScreen: () -> Unit,
    toPrivacyPolicy: (WebDocument) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<AboutViewModel>()
    val viewState by viewModel.collectAsState()

    AboutScreen(
        viewState = viewState,
        toPreviousScreen = toPreviousScreen,
        toPrivacyPolicy = toPrivacyPolicy,
        modifier = modifier
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
fun AboutScreen(
    viewState: AboutViewModel.ViewState,
    toPreviousScreen: () -> Unit,
    toPrivacyPolicy: (WebDocument) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            EditProfileBar(
                title = stringResource(R.string.about),
                toPreviousScreen = toPreviousScreen
            )
        }
    ) { padding ->
        AboutScreenContent(
            toPrivacyPolicy = toPrivacyPolicy,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}

@Composable
fun AboutScreenContent(
    toPrivacyPolicy: (WebDocument) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(Paddings.LARGE))
        AccountSettingItem(
            value = WebDocument.SERVICE_TERMS.title,
            contentDescription = stringResource(R.string.open_terms_of_service),
            modifier = Modifier.clickable {
                toPrivacyPolicy(WebDocument.SERVICE_TERMS)
            }
        )
        AccountSettingItem(
            value = WebDocument.PRIVACY_POLICY.title,
            contentDescription = stringResource(R.string.open_privacy_policy),
            modifier = Modifier.clickable {
                toPrivacyPolicy(WebDocument.PRIVACY_POLICY)
            }
        )
        Spacer(modifier = Modifier.weight(1.0F))
        AppDetailsSection()
    }
}

@Composable
fun AppDetailsSection(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val packageInfo = remember(context) {
        context.packageManager.getPackageInfo(
            context.packageName,
            PackageManager.GET_ACTIVITIES
        )
    }

    Column(modifier = modifier) {
        Icon(
            painter = painterResource(CommonComposeR.drawable.ic_logo_placeholder),
            contentDescription = null,
            modifier =
            Modifier
                .size(80.dp)
                .clip(MaterialTheme.shapes.small)
                .background(BaseplateTheme.colors.primaryShade3)
                .padding(Paddings.LARGE),
            tint = BaseplateTheme.colors.textPrimary
        )
        Spacer(modifier = Modifier.height(Paddings.LARGE))
        Text(
            text = stringResource(
                R.string.version_x,
                packageInfo?.versionName.orEmpty()
            ),
            color = BaseplateTheme.colors.textSecondary,
            style = BaseplateTheme.typography.footnoteRegular
        )
        Spacer(modifier = Modifier.height(Paddings.LARGE))
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    BaseplateTheme {
        AboutScreen(
            viewState = AboutViewModel.ViewState(),
            toPreviousScreen = {},
            toPrivacyPolicy = {}
        )
    }
}
