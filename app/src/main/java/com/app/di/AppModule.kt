package com.app.di

import android.content.Context
import com.app.SHARED_PREFERENCES_FILE_NAME
import com.app.data.local.LocalData
import com.app.utils.Network
import com.app.utils.NetworkConnectivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

/**
 * This [AppModule] defines the dependencies which are requited at App level components.
 *
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideLocalRepository(@ApplicationContext context: Context): LocalData {
        val sharedPreference = context.getSharedPreferences(SHARED_PREFERENCES_FILE_NAME, 0)
        return LocalData(sharedPreference)
    }

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideNetworkConnectivity(@ApplicationContext context: Context): NetworkConnectivity {
        return Network(context)
    }
}
