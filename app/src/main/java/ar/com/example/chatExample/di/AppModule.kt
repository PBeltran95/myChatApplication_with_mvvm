package ar.com.example.chatExample.di

import ar.com.example.chatExample.core.AppConstants
import ar.com.example.chatExample.core.FireBaseNotificationService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesFirebaseAuthInstance():FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun providesFirebaseStorageInstance():FirebaseFirestore = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun providesRetrofit() = Retrofit.Builder()
        .baseUrl(AppConstants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    @Singleton
    @Provides
    fun providesNotificationService(retrofit: Retrofit) = retrofit.create(FireBaseNotificationService::class.java)

}