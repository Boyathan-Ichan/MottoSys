package com.technologiesdesesperation.mottosys.models

data class UserResponse(
    val email: String,
    val password: String,
    val has_handheld: Boolean? = false
)

data class LicenseResponse(
    val email: String,
    val licencia: Boolean?,
    val pago_date: String
)

data class UpdateLicenseRequest(
    val licencia: Boolean?
)


data class HardwareUpdateRequest(
    val has_handheld: Boolean
)