package com.example.siloli.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Laporan(
    val id: Int,
    val wilayah: String?,
    val aduan: String?,
    @SerialName("created_at") val createdAt: String?,
    @SerialName("updated_at") val updatedAt: String?
)

@Serializable
data class LaporanResponse(
    val jumlah: Int,
    val laporan: List<Laporan>?
)

@Serializable
data class BuatLaporanRequest(
    val wilayah: String,
    val aduan: String
)

@Serializable
data class EditLaporanRequest(
    val id: Int,
    val wilayah: String,
    val aduan: String
)
