package com.example.siloli.repository

import com.example.siloli.data.TokenStore
import com.example.siloli.model.*
import com.example.siloli.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LaporanRepository @Inject constructor(
    private val apiService: ApiService,
    private val tokenStore: TokenStore
) {

    fun isLoggedIn(): Boolean = tokenStore.getToken() != null

    fun getUserData(): Pair<Int, String>? = tokenStore.getUser()

    fun logout() {
        tokenStore.clearToken()
    }

    suspend fun register(request: RegisterRequest): Result<GenericResponse> {
        return safeApiCall { apiService.register(request) }
    }

    suspend fun login(request: LoginRequest): Result<LoginResponse> {
        val result = safeApiCall { apiService.login(request) }
        result.onSuccess { response ->
             tokenStore.saveToken(response.token)
             tokenStore.saveUser(response.user.id, response.user.username)
        }
        return result
    }

    suspend fun getLaporanSaya(): Result<LaporanResponse> {
        return safeApiCall { apiService.getLaporanSaya() }
    }

    suspend fun buatLaporan(request: BuatLaporanRequest): Result<GenericResponse> {
        return safeApiCall { apiService.buatLaporan(request) }
    }

    suspend fun editLaporan(request: EditLaporanRequest): Result<GenericResponse> {
        return safeApiCall { apiService.editLaporan(request) }
    }

    suspend fun hapusLaporan(id: Int): Result<GenericResponse> {
        return safeApiCall { apiService.hapusLaporan(id) }
    }

    private suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): Result<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                // Try to parse error body if needed, otherwise generic error
                val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                 Result.failure(Exception("Error ${response.code()}: $errorMsg"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
