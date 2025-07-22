package com.appetiser.core.common.android.util

import android.content.Context
import com.appetiser.core.common.android.R

object PasswordRulesUtil {
    private val specialChars = "!@#\$%^&*()-_+=<>?/\\[]{}|~`"


    fun getPasswordRules(context: Context): List<Pair<String, (String) -> Boolean>> = listOf(
        context.getString(R.string.password_rules_1) to { password: String ->
            password.length >= 8
        },
        context.getString(R.string.password_rules_2) to { password: String ->
            password.any { it.isUpperCase() } && password.any { it.isLowerCase() }
        },
        context.getString(R.string.password_rules_3) to { password: String ->
            password.any { it.isDigit() }
        },
        context.getString(R.string.password_rules_4) to { password: String ->
            password.any { specialChars.contains(it) }
        }
    )


    fun validatePassword(
        password: String,
        context: Context
    ) = getPasswordRules(context).all { (ruleText, validator) ->
        validator(password)
    }
}
