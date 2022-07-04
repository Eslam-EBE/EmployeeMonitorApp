package com.ebe.employeemonitorapp.di

import com.ebe.employeemonitorapp.domain.repositories.MonitorRepository
import com.ebe.employeemonitorapp.domain.usecases.GetEmployeesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module(includes = [AppModule::class])
@InstallIn(SingletonComponent::class)
object EmployeesModule {

    @Provides
    fun provideGetEmployeesUseCase(repository: MonitorRepository): GetEmployeesUseCase {
        return GetEmployeesUseCase(repository)
    }


}