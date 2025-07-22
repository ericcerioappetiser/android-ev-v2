package com.appetiser.androidecv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.appetiser.androidecv.utils.RootNavigation
import com.appetiser.core.common.compose.ui.BaseplateTheme
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import com.appetiser.core.common.compose.utils.navigateUpToRoot
import com.appetiser.feature.auth.ui.GetStartedRoute
import com.appetiser.feature.auth.ui.VerifyOtpRoute
import com.appetiser.feature.walkthrough.ui.WalkthroughRoute
import com.appetiser.onboarding.ui.InputBirthdayRoute
import com.appetiser.onboarding.ui.InputNameRoute
import com.appetiser.onboarding.ui.YourProfileRoute
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            !viewModel.state.isReady
        }

        setContent {
            BaseplateTheme {
                val navController = rememberNavController()
                val state by viewModel.collectAsState()

                ConsumableSideEffect(state.initialNavigation) {
                    when (it) {
                        InitialNavigation.WALKTHROUGH -> {
                            navController.navigateUpToRoot(WalkthroughRoute)
                        }

                        InitialNavigation.LOGIN -> {
                            navController.navigateUpToRoot(GetStartedRoute)
                        }

                        InitialNavigation.VERIFICATION -> {
                            navController.navigateUpToRoot(VerifyOtpRoute(username = state.user?.username.orEmpty()))
                        }

                        InitialNavigation.INPUT_NAME -> {
                            navController.navigateUpToRoot(InputNameRoute)
                        }

                        InitialNavigation.INPUT_BIRTH_DATE -> {
                            navController.navigateUpToRoot(InputBirthdayRoute)
                        }

                        InitialNavigation.INPUT_PROFILE -> {
                            navController.navigateUpToRoot(YourProfileRoute)
                        }
                    }
                }

                RootNavigation(
                    navController = navController
                )
            }
        }
    }
}
