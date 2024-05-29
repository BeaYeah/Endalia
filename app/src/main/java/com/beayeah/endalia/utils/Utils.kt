package com.beayeah.endalia.utils

object Utils {
    @JvmStatic
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    @JvmStatic
    fun isValidPassword(password: String): Boolean {
        val passwordPattern =
            Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$")
        return passwordPattern.matches(password)
    }
}