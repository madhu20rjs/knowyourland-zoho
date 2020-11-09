package com.zoho.countries.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*



@Dao
interface AppDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun countriesInsert(country: List<Country>)

    @Query("Select * from countries")
    fun getAllCountries(): LiveData<List<Country>>

    @Update
    fun countriesUpdate(country: List<Country>)

    @Query("Select Count(*) from countries")
    fun getCountriesCount(): Int

}