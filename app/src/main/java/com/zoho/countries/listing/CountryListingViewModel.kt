package com.zoho.countries.listing

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zoho.countries.data.AppDataBase
import com.zoho.countries.data.CountriesServices
import com.zoho.countries.data.Country
import com.zoho.countries.di.DaggerAppComponent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CountryListingViewModel(application: Application) : AndroidViewModel(application) {
    @Inject
    lateinit var countryService: CountriesServices
    val disposable = CompositeDisposable()
    private val database = AppDataBase.getInstance(application).getAppDao()
    var countriesList: LiveData<List<Country>> = database.getAllCountries()

    val countries = MutableLiveData<List<Country>>()
    val loading = MutableLiveData<Boolean>()
    val textError = MutableLiveData<Boolean>()


    init {
        DaggerAppComponent.create().inject(this)
    }

    fun getData() {

        loading.value = true
        disposable.add(
            countryService.getCountries()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Country>>() {
                    override fun onSuccess(value: List<Country>) {
                        loading.value = false
                        countries.value = value
                        textError.value = false
                        if (!value.isNullOrEmpty()) {
                            addCountriesDB(value)
                        }
                    }
                    override fun onError(e: Throwable) {
                        loading.value = false
                        textError.value = true
                    }
                })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    private fun addCountriesDB(countryList: List<Country>) {
        if (database.getCountriesCount() <= 0) {
            database.countriesInsert(countryList)
        } else {
            database.countriesUpdate(countryList)
        }
    }

    var searchStringValue: String = ""
    fun setString(query: CharSequence) {
        searchStringValue = query.toString()
    }

    fun getString(): String {
        return searchStringValue
    }

}