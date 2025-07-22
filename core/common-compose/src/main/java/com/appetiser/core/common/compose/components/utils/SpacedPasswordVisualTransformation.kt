package com.appetiser.core.common.compose.components.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class SpacedPasswordVisualTransformation(private val mask: Char = '•', private val spacing: String = " ") : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text

        val transformedText = buildString {
            originalText.forEachIndexed { index, _ ->
                append(mask)
                if (index != originalText.lastIndex) append(spacing)
            }
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                (offset * (1 + spacing.length)).coerceAtMost(transformedText.length)

            override fun transformedToOriginal(offset: Int): Int =
                (offset / (1 + spacing.length)).coerceAtMost(originalText.length)
        }

        return TransformedText(AnnotatedString(transformedText), offsetMapping)
    }
}
