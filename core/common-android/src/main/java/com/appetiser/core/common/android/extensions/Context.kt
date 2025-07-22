package com.appetiser.core.common.android.extensions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Patterns
import android.widget.Toast

fun Context.sendEmail(email: String, subject: String = "", body: String = "") {
    val isValidEmail = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    if (isValidEmail) {
        try {
            val uri = Uri.parse(
                "mailto:$email?subject=${Uri.encode(subject)}&body=${Uri.encode(body)}"
            )
            val emailIntent = Intent(Intent.ACTION_SENDTO, uri)
            startActivity(emailIntent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    } else {
        Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
    }
}
