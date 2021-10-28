package ar.com.example.chatExample.di

import android.content.Context
import android.content.SharedPreferences
import ar.com.example.chatExample.application.AppConstants
import ar.com.example.chatExample.core.FireBaseNotificationService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun providesSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences =
        appContext.getSharedPreferences(AppConstants.KEY_MODE, Context.MODE_PRIVATE)

}