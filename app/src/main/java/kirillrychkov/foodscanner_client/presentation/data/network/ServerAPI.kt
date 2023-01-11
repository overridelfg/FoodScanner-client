package kirillrychkov.foodscanner_client.presentation.data.network

import retrofit2.http.POST


interface ServerAPI {
    @POST("login")
    suspend fun login(

    )

    @POST("register")
    suspend fun register(

    )
}