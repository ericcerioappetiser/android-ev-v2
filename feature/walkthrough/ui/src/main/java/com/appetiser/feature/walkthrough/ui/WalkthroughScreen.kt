package com.appetiser.feature.walkthrough.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.appetiser.core.common.compose.components.BaseplateButton
import com.appetiser.core.common.compose.components.BaseplateTextButton
import com.appetiser.core.common.compose.components.CustomAsyncImage
import com.appetiser.core.common.compose.dimensions.Margins
import com.appetiser.core.common.compose.dimensions.Paddings
import com.appetiser.core.common.compose.dimensions.Radius
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appteriser.feature.auth.ui.get_started.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.launch


@OptIn(ExperimentalPagerApi::class)
@Composable
fun WalkthroughScreen(
    onDone: () -> Unit
) {
    val context = LocalContext.current

    val features = remember(context) {
        listOf(
            Feature(
                context.getString(R.string.feature_01),
                context.getString(R.string.feature_01_desc),
                "https://picsum.photos/200"
            ),
            Feature(
                context.getString(R.string.feature_02),
                context.getString(R.string.feature_02_desc),
                "https://picsum.photos/200"
            ),
            Feature(
                context.getString(R.string.feature_03),
                context.getString(R.string.feature_03_desc),
                "https://picsum.photos/200"
            )
        )
    }

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState { features.size }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = Paddings.XX_LARGE)
            .background(BaseplateTheme.colors.surfaceBackground)
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(Paddings.LARGE)
        ) {
            HorizontalPager(
                pageSize = PageSize.Fill,
                state = pagerState
            ) { currentStep ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = Paddings.LARGE)
                ) {
                    CustomAsyncImage(
                        placeholder = painterResource(R.drawable.onboarding_image),
                        contentDescription = stringResource(R.string.get_started),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 4f)
                            .padding(Paddings.LARGE)
                            .clip(RoundedCornerShape(Radius.LARGE)),
                        imageUrl = features[currentStep].imageUrl,
                        contentScale = ContentScale.Crop,
                        shape = RoundedCornerShape(Radius.LARGE)
                    )

                    Spacer(modifier = Modifier.height(Margins.MEDIUM))

                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = Paddings.LARGE),
                        text = features[currentStep].title,
                        color = BaseplateTheme.colors.textPrimary,
                        style = BaseplateTheme.typography.title3,
                    )

                    Spacer(modifier = Modifier.height(Margins.MEDIUM))

                    Text(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = Paddings.LARGE),
                        text = features[currentStep].description,
                        color = BaseplateTheme.colors.textSecondary,
                        style = BaseplateTheme.typography.bodyRegular
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(Margins.LARGE)
        ) {
            // Left - Skip button
            if (pagerState.currentPage != features.lastIndex) {
                BaseplateTextButton(
                    text = stringResource(R.string.skip),
                    onClick = onDone,
                    textStyle = BaseplateTheme.typography.subHeadSemiBold,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                pageCount = features.size,
                activeColor = BaseplateTheme.colors.primaryDefault,
                inactiveColor = Color.LightGray,
                spacing = Paddings.MICRO,
                indicatorShape = CircleShape,
                modifier = Modifier.align(Alignment.Center)
            )

            // Right - Next button
            BaseplateButton(
                contentPadding = PaddingValues(Paddings.LARGE, Paddings.SMALL),
                text = stringResource(R.string.next),
                textStyle = BaseplateTheme.typography.subHeadSemiBold,
                onClick = {
                    if (pagerState.currentPage == features.lastIndex) {
                        onDone()
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

data class Feature(val title: String, val description: String, val imageUrl: String)

@Preview(showBackground = true)
@Composable
fun WalkthroughScreenPreview() {
    WalkthroughScreen(
        onDone = {}
    )
}
