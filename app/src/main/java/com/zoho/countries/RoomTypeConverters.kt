package com.zoho.countries

import androidx.room.TypeConverter

class StringListTypeConverter {

    @TypeConverter
    fun fromList(stringList: ArrayList<String>): String {
        return stringList.joinToString(",")
    }

    @TypeConverter
    fun toList(baseString: String): ArrayList<String> {
        val uuidList = ArrayList<String>()
        uuidList.addAll(baseString.split(","))
        return uuidList
    }

}