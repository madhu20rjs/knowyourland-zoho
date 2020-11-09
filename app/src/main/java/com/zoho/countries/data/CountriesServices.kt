package com.zoho.countries.data

import com.zoho.countries.di.DaggerAppComponent
import io.reactivex.Single
import javax.inject.Inject

class CountriesServices {


   @Inject
   lateinit var api: CountryApi

    init {
        DaggerAppComponent.create().inject(this)
    }

    fun getCountries():Single<List<Country>>{
        return api.getCountries()
    }

}