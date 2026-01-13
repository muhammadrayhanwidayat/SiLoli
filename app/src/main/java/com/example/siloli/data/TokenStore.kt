package com.example.siloli.data

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenStore @Inject constructor(
    @ApplicationContext context: Context
) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    fun saveUser(id: Int, username: String) {
        sharedPreferences.edit()
            .putInt("user_id", id)
            .putString("username", username)
            .apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    fun getUser(): Pair<Int, String>? {
        val id = sharedPreferences.getInt("user_id", -1)
        val username = sharedPreferences.getString("username", null)
        return if (id != -1 && username != null) {
            Pair(id, username)
        } else {
            null
        }
    }

    fun clearToken() {
        sharedPreferences.edit()
            .remove("jwt_token")
            .remove("user_id")
            .remove("username")
            .apply()
    }
}
