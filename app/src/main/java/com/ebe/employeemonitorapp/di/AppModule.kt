package com.ebe.employeemonitorapp.di

import com.ebe.employeemonitorapp.data.remote.MonitorService
import com.ebe.employeemonitorapp.data.repositories.MonitorRepositoryImpl
import com.ebe.employeemonitorapp.domain.repositories.MonitorRepository
import com.ebe.employeemonitorapp.utils.BaseUrl
import com.ebe.employeemonitorapp.utils.getUnsafeOkHttpClient
import com.skydoves.sandwich.adapters.ApiResponseCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesMonitorService(): MonitorService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = getUnsafeOkHttpClient()


        return Retrofit.Builder().baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client!!)
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build().create(MonitorService::class.java)
    }

    @Provides
    @Singleton
    fun providesMonitorRepository(monitorService: MonitorService): MonitorRepository {
        return MonitorRepositoryImpl(monitorService)
    }
}