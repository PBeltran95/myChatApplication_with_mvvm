package ar.com.example.chatExample.core

import ar.com.example.chatExample.data.models.PushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FireBaseNotificationService {

    @Headers("Authorization: key=${AppConstants.SERVER_KEY}", "Content-Type:${AppConstants.CONTENT_TYPE}")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>
}