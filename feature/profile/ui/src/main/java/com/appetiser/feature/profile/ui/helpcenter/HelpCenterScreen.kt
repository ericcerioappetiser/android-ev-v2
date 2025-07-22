package com.appetiser.feature.profile.ui.helpcenter

import android.os.Build
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appetiser.core.common.android.extensions.sendEmail
import com.appetiser.core.common.compose.components.BaseplateCompoundItemNavigationItem
import com.appetiser.core.common.compose.components.CenterTopBar
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Radius
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.feature.profile.ui.R
import com.appetiser.core.common.android.R as commonAndroidR

@Composable
fun HelpCenterScreen(
    modifier: Modifier,
    onBackClick: () -> Unit = {},
    onNavigateToReport: () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(Paddings.LARGE),
                verticalArrangement = Arrangement.Top
            ) {
                CenterTopBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = stringResource(R.string.help_centre),
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
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

                HelpCentreOptions(modifier, onNavigateToReport = onNavigateToReport)
            }
        }
    }
}

@Composable
fun HelpCentreOptions(modifier: Modifier, onNavigateToReport: () -> Unit = {}) {
    val context = LocalContext.current
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .border(
                width = 1.dp,
                color = BaseplateTheme.colors.borderDefault,
                shape = RoundedCornerShape(Radius.LARGE)
            )
            .padding(Paddings.MEDIUM)
    ) {
        BaseplateCompoundItemNavigationItem(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val recipient = "test@email.com" // Todo project admin email
                    val subject = "" // Todo base on Project implementation
                    val body =
                        getDeviceInfo()
                    context.sendEmail(recipient, subject, body)
                }
                .padding(
                    top = Paddings.X_LARGE
                ),
            title = {
                Text(
                    text = stringResource(R.string.contact_us),
                    style = BaseplateTheme.typography.bodyRegular,
                    color = BaseplateTheme.colors.textSecondary
                )
            },
            divider = {
                HorizontalDivider(
                    color = BaseplateTheme.colors.borderDefault,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = Paddings.X_LARGE
                        )
                )
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.report_problem),
                    Modifier.size(18.dp),
                    tint = BaseplateTheme.colors.textPrimary
                )
            }
        )

        BaseplateCompoundItemNavigationItem(
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    onNavigateToReport.invoke()
                }
                .padding(
                    top = Paddings.X_LARGE,
                    bottom = Paddings.X_LARGE
                ),
            title = {
                Text(
                    text = stringResource(R.string.report_problem),
                    style = BaseplateTheme.typography.bodyRegular,
                    color = BaseplateTheme.colors.textPrimary
                )
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.contact_us),
                    modifier.size(18.dp),
                    tint = BaseplateTheme.colors.textPrimary
                )
            }
        )
    }
}

private fun getDeviceInfo(): String {
    val manufacturer = Build.MANUFACTURER // Device manufacturer
    val model = Build.MODEL // Device model
    val osVersion = Build.VERSION.RELEASE // Android version
    val sdkVersion = Build.VERSION.SDK_INT // API level
    // Todo version should be later
    return """
        App Version:
        Device Information
        Manufacturer: $manufacturer
        Model: $model
        Android Version: $osVersion
        API Level: $sdkVersion
    """.trimIndent()
}

@Preview
@Composable
fun HelpCenterScreenPreview() {
    HelpCenterScreen(Modifier)
}
