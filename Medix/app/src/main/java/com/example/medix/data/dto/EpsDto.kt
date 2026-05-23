package com.example.medix.data.dto

import com.google.gson.annotations.SerializedName

data class EpsDto(
    @SerializedName(value = "id_eps", alternate = ["id", "idEps"])
    val idEps: Long,
    @SerializedName(value = "nombre", alternate = ["nombre_eps", "eps", "nombreEps"])
    val nombre: String,
)
