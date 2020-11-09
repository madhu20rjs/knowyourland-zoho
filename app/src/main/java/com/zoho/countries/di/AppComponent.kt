package com.zoho.countries.di

import com.zoho.countries.data.CountriesServices
import com.zoho.countries.listing.CountryListingViewModel
import dagger.Component
import dagger.Module

@Component(modules = [ApiModule::class])
interface AppComponent {
    fun inject(countriesServices: CountriesServices)
    fun inject(viewModel: CountryListingViewModel)

}