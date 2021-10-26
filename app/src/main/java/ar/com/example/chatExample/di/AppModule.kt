package ar.com.example.chatExample.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

}