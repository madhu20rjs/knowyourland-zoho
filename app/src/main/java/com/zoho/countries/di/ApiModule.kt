package com.zoho.countries.di

import com.zoho.countries.data.CountriesServices
import com.zoho.countries.data.CountryApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
@Module
class ApiModule {

    private val BASE_URL="https://restcountries.eu/"

    @Provides
    fun provideApiModules(): CountryApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CountryApi::class.java)
    }

    @Provides
    fun provideCountryServices(): CountriesServices {
        return CountriesServices()
    }
}