package com.appetiser.feature.profile.ui.about.privacypolicy

import android.webkit.WebView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.network.BuildConfig
import com.appetiser.core.network.utils.Constant
import com.appetiser.feature.profile.domain.WebDocument
import com.appetiser.feature.profile.ui.editprofile.EditProfileBar

@Composable
fun PrivacyPolicyScreen(
    document: WebDocument,
    toPreviousScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            EditProfileBar(
                title = document.title,
                toPreviousScreen = toPreviousScreen
            )
        }
    ) { padding ->
        PrivacyPolicyContent(
            document = document,
            modifier =
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = Paddings.LARGE)
        )
    }
}

@Composable
fun PrivacyPolicyContent(
    document: WebDocument,
    modifier: Modifier = Modifier
) {
    val url = String.format(
        Constant.ENDPOINT_FORMAT,
        BuildConfig.BASE_URL,
        Constant.API,
        Constant.VERSION
    ) + document.path

    AndroidView(
        factory = { context ->
            WebView(context)
        },
        modifier = modifier,
        update = { view ->
            view.loadUrl(url)
        }
    )
}

@Preview
@Composable
private fun PrivacyPolicyScreenPreview() {
    BaseplateTheme {
        PrivacyPolicyScreen(
            document = WebDocument.PRIVACY_POLICY,
            toPreviousScreen = {}
        )
    }
}
