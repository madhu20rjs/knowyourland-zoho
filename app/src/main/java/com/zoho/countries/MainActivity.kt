package com.zoho.countries


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zoho.countries.listing.CountryListingFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.apply {
            title = "Countries List"
            setDisplayHomeAsUpEnabled(true)
        }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.root_layout, CountryListingFragment.newInstance())
                .commit()
        }
    }
}
