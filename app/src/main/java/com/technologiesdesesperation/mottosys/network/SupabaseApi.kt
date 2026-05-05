package com.technologiesdesesperation.mottosys.network

import com.technologiesdesesperation.mottosys.models.HardwareUpdateRequest
import com.technologiesdesesperation.mottosys.models.LicenseResponse
import com.technologiesdesesperation.mottosys.models.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface SupabaseApi {

    @GET("rest/v1/User")
    @Headers(
        "apikey: sb_publishable_L7NlFlyB1x83oc_dZe4PcQ_29IqvxXQ",
        "Authorization: Bearer sb_publishable_L7NlFlyB1x83oc_dZe4PcQ_29IqvxXQ"
    )
    suspend fun getUser(
        @Query("email") email: String
    ): Response<List<UserResponse>>

    @GET("rest/v1/Licencia")
    @Headers(
        "apikey: sb_publishable_L7NlFlyB1x83oc_dZe4PcQ_29IqvxXQ",
        "Authorization: Bearer sb_publishable_L7NlFlyB1x83oc_dZe4PcQ_29IqvxXQ"
    )
    suspend fun checkSecurityStatus(
        @Query("email") email: String
    ): Response<List<LicenseResponse>>

    @POST("rest/v1/User")
    @Headers("apikey: sb_publishable_L7NlFlyB1x83oc_dZe4PcQ_29IqvxXQ",
        "Authorization: Bearer sb_publishable_L7NlFlyB1x83oc_dZe4PcQ_29IqvxXQ",
        "Prefer: return=minimal")
    suspend fun createUser(
        @Body user: UserResponse,
    ): Response<Unit>

    @POST("rest/v1/Licencia")
    @Headers("apikey: sb_publishable_L7NlFlyB1x83oc_dZe4PcQ_29IqvxXQ",
        "Authorization: Bearer sb_publishable_L7NlFlyB1x83oc_dZe4PcQ_29IqvxXQ",
        "Prefer: return=minimal")
    suspend fun createLicense(
        @Body license: LicenseResponse,
    ): Response<Unit>

    @PATCH("rest/v1/User")
    @Headers("apikey: sb_publishable_L7NlFlyB1x83oc_dZe4PcQ_29IqvxXQ",
        "Authorization: Bearer sb_publishable_L7NlFlyB1x83oc_dZe4PcQ_29IqvxXQ",
        "Content-Type: application/json")
    suspend fun updateUserHardware(
        @Query("email") email: String,
        @Body preference: HardwareUpdateRequest
    ): Response<Unit>
}