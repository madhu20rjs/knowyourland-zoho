package com.zoho.countries.detail

import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.zoho.countries.R
import com.zoho.countries.data.Country
import kotlinx.android.synthetic.main.fragment_country_detail.*
import org.json.JSONObject
import java.io.Serializable
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class CountryDetailFragment : Fragment() {

    private var data: Serializable? = null
    val API: String = "b091d67f6a0d8e22e3d42a32a4a36be3"
    var LAT: String = ""
    var LON: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            data = it.getSerializable(ARG_PARAM1)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country_detail, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance() = CountryDetailFragment()
        const val ARG_PARAM1 = "param1"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataLoad(data as Country)
        (activity as AppCompatActivity).supportActionBar?.title = "Country Details"
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.supportFragmentManager?.popBackStack()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun dataLoad(data: Country) {
        tv_country_name.text = data.countryName
        tv_capital.text = data.capital
        tv_region.text = data.region
        tv_area.text = data.area
        tv_population.text = data.population
        tv_native_name.text = data.nativeName
        LAT = data.latlng?.get(0)
        LON = data.latlng?.get(1)
        weatherTask().execute()

        val uri = Uri.parse(data.flag)
        //GlideToVectorYou.justLoadImage(activity,uri ,flagImg)
        GlideToVectorYou
            .init()
            .with(activity)
            .setPlaceHolder(
                R.mipmap.ic_launcher,
                R.mipmap.ic_launcher
            )
            .load(uri, iv_flag)
    }

    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            errorText.visibility = View.GONE
            layout_weather_Container.visibility=View.GONE
            loader.visibility=View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?lat=$LAT&lon=$LON&units=metric&appid=$API").readText(
                        Charsets.UTF_8
                    )
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)

                val updatedAt: Long = jsonObj.getLong("dt")
                val updatedAtText =
                    "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(
                        Date(updatedAt * 1000)
                    )
                val temp = main.getString("temp") + "°C"
                val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
                val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")

                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name") + ", " + sys.getString("country")

                updated_at.text = updatedAtText
                status_result.text = weatherDescription.capitalize()
                temp_main.text = temp
                temp_min.text = tempMin
                temp_max.text = tempMax
                sunrise_time.text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunrise * 1000))
                sunset_time.text =
                    SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(sunset * 1000))

                wind_result.text = windSpeed
                pressure_result.text = pressure
                humidity_result.text = humidity

                loader.visibility=View.GONE
                layout_weather_Container.visibility=View.VISIBLE

            } catch (e: Exception) {
                errorText.visibility = View.VISIBLE
                loader.visibility=View.GONE
            }

        }
    }

}