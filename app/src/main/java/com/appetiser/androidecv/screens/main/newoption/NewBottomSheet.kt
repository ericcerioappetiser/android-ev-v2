package com.appetiser.androidecv.screens.main.newoption

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.appetiser.core.common.compose.components.BaseplateButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewBottomSheet(
    modifier: Modifier = Modifier,
    onBack: (String?) -> Unit,
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            onBack(null)
        }
    ) {
        Column {
            var result by remember { mutableStateOf("") }
            TextField(value = result, onValueChange = { result = it })
            BaseplateButton(
                text = "Set Result",
                onClick = {
                    onBack(result)
                }
            )
        }
    }
}
