package com.morteza.foodorderapplication.di.bodyModule

import com.morteza.foodorderapplication.models.BodyRegister
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object BodyModule {

    @Provides
    fun bodyRegister() = BodyRegister()
}