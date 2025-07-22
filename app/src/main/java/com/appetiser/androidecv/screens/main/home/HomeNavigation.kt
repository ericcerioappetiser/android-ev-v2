package com.appetiser.androidecv.screens.main.home


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.appetiser.androidecv.screens.main.newoption.NewDialog
import com.appetiser.core.common.compose.components.BaseplateButton
import com.appetiser.core.common.compose.result.getOr
import com.appetiser.core.common.compose.result.resultRecipient
import com.appetiser.core.common.compose.utils.ConsumableSideEffect
import com.appetiser.core.common.compose.utils.collectAsState
import kotlinx.serialization.Serializable

@Serializable
data object HomeRoute

@Serializable
data object NestedRoute // represents the next screen under Home

fun NavGraphBuilder.homeDestination(
    onNested: () -> Unit,
    onHome: () -> Unit
) {
    composable<HomeRoute> {
        val viewModel = hiltViewModel<HomeViewModel>()

        val recipient = resultRecipient<NewDialog, String>(it)

        // TODO: Replace with actual screen composable like the above, this is just for navigation demo
        Scaffold(
            // TODO remove, view the container clearly, for checking if insets are properly set
            containerColor = Color.Yellow,
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .padding(contentPadding)
                    .background(Color.Yellow) // view the body clearly
                    .fillMaxSize()

            ) {
                BaseplateButton(
                    text = "Go to nested",
                    onClick = onNested
                )

                var result by remember { mutableStateOf("") }

                recipient.NavResultEffect { navResult ->
                    result = navResult.getOr { "Cancelled" }
                }

                val state by viewModel.collectAsState()

                val context = LocalContext.current

                ConsumableSideEffect(state.oneTime) {
                    Toast.makeText(context, "One time", Toast.LENGTH_SHORT).show()
                }

                TextField(
                    value = state.text,
                    onValueChange = { value ->
                        viewModel.action(HomeViewModel.Action.Type(value))
                    }
                )

                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = state.count.toString()
                )

                Row {
                    BaseplateButton(
                        text = "Increment",
                        onClick = {
                            viewModel.action(HomeViewModel.Action.Increment)
                        }
                    )

                    BaseplateButton(
                        text = "Onetime",
                        onClick = {
                            viewModel.action(HomeViewModel.Action.OneTime)
                        }
                    )
                }

                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = result
                )

                Text(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    text = "You will see this is insets are properly set"
                )
            }
        }
    }

    composable<NestedRoute> {
        // TODO: Replace with actual screen composable, this is just for navigation demo
        Scaffold(
            // TODO remove, view the container clearly, for checking if insets are properly set
            containerColor = Color.Red,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onHome
                ) {
                    Icon(Icons.Default.Home, contentDescription = "Home")
                }
            },
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()

            ) {
                Text(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    text = "Nested: You will see this is insets are properly set"
                )
            }
        }
    }
}
