package com.example.siloli.network

import com.example.siloli.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("register.php")
    suspend fun register(@Body request: RegisterRequest): Response<GenericResponse>

    @POST("login.php")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("buatLaporan.php")
    suspend fun buatLaporan(@Body request: BuatLaporanRequest): Response<GenericResponse>

    @GET("laporanSaya.php")
    suspend fun getLaporanSaya(): Response<LaporanResponse>

    @PUT("editLaporan.php")
    suspend fun editLaporan(@Body request: EditLaporanRequest): Response<GenericResponse>

    @DELETE("hapusLaporan.php")
    suspend fun hapusLaporan(@Query("id") id: Int): Response<GenericResponse>
}
