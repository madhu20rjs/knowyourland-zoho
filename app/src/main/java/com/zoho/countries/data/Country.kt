package com.zoho.countries.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "countries")
data class Country(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "name")
    @SerializedName(value = "name")
    val countryName: String?,

    @ColumnInfo(name = "capital")
    @SerializedName("capital")
    val capital: String?,

    @ColumnInfo(name = "region")
    @SerializedName("region")
    val region: String?,

    @ColumnInfo(name = "population")
    @SerializedName("population")
    val population: String?,

    @ColumnInfo(name = "nativeName")
    @SerializedName("nativeName")
    val nativeName: String?,

    @ColumnInfo(name = "area")
    @SerializedName("area")
    val area: String?,

    @ColumnInfo(name = "latlng")
    @SerializedName("latlng")
    val latlng:ArrayList<String> = ArrayList(),

    @ColumnInfo(name = "flag")
    @SerializedName("flag")
    val flag: String?

) : Serializable



