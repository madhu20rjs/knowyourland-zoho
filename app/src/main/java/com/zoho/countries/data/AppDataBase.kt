package com.zoho.countries.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zoho.countries.StringListTypeConverter

@Database(entities = [Country::class], version = 1, exportSchema = false)
@TypeConverters(StringListTypeConverter::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getAppDao(): AppDao

    companion object {
        private val LOCK = Any()
        public const val DB_NAME = "country.db"
        private var INSTANCE: AppDataBase? = null

        fun getInstance(application: Application): AppDataBase {
            synchronized(LOCK) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(application, AppDataBase::class.java, DB_NAME)
                        .allowMainThreadQueries()
                        .addCallback(object : RoomDatabase.Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                            }

                            override fun onOpen(db: SupportSQLiteDatabase) {
                                super.onOpen(db)
                            }

                            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                                super.onDestructiveMigration(db)
                            }
                        })
                        .addMigrations()
                        .build()
                }
            }
            return INSTANCE!!
        }

        fun prePopulate(appDataBase: AppDataBase) {
        }

    }
}



