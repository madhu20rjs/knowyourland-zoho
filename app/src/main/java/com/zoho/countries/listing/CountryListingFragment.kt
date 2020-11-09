package com.zoho.countries.listing

import android.content.Context
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.zoho.countries.R
import com.zoho.countries.data.Country
import com.zoho.countries.detail.CountryDetailFragment
import com.zoho.countries.detail.CountryDetailFragment.Companion.ARG_PARAM1
import kotlinx.android.synthetic.main.fragment_country_listing.*
import kotlinx.android.synthetic.main.list_item_grid_countries.view.*
import java.util.*


class CountryListingFragment : Fragment() {

    lateinit var viewModel: CountryListingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_country_listing, container, false)
        setHasOptionsMenu(true)
        return rootView
    }

    companion object {
        @JvmStatic
        fun newInstance() = CountryListingFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProviders.of(this).get(CountryListingViewModel::class.java)

        if (verifyAvailableNetwork(activity as AppCompatActivity)) {
            viewModel.getData()
        } else {

            Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
        }
        (activity as AppCompatActivity).supportActionBar?.title = "Countries Listing"
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

            adapter = CountryAdapter(arrayListOf()) { item ->

                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(windowToken, 0)
                if (countrySearch.query.isNotEmpty()) {
                    viewModel.setString(countrySearch.query)
                } else {
                    viewModel.setString("")

                }
                countrySearch.setQuery("", false)

                val bundle = Bundle()
                bundle.putSerializable(ARG_PARAM1, item)

                val transaction = activity?.supportFragmentManager?.beginTransaction()
                val countryDetailFragment =
                    CountryDetailFragment()
                countryDetailFragment.arguments = bundle

                transaction?.replace(R.id.root_layout, countryDetailFragment)
                transaction?.addToBackStack(null)
                transaction?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                transaction?.commit()
            }
        }

        countrySearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                (recyclerView.adapter as CountryAdapter).filter.filter(newText)
                return false
            }
        })

        observerViewModel()

        swipeLayout.setOnRefreshListener {
            swipeLayout.isRefreshing = false
            if (verifyAvailableNetwork(activity as AppCompatActivity)) {
                viewModel.getData()
            } else {
                viewModel.countriesList.observe(this, Observer {
                    if (it.isEmpty()) {
                        recyclerView.visibility = View.GONE
                    } else {
                        recyclerView.visibility = View.VISIBLE
                    }
                    (recyclerView.adapter as CountryAdapter).updateAdapter(it)
                    (recyclerView.adapter as CountryAdapter).filter.filter(countrySearch.query)
                })
                Toast.makeText(context, "No Internet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun verifyAvailableNetwork(activity: AppCompatActivity): Boolean {
        val connectivityManager =
            activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    override fun onResume() {
        super.onResume()
        var savedString: String = viewModel.getString()
        if (!savedString.isNullOrEmpty()) {
            countrySearch.setQuery(savedString, false)
            (recyclerView.adapter as CountryAdapter).filter.filter(countrySearch.query)
        }
    }

    override fun onPause() {
        super.onPause()
        if (countrySearch.query.isNotEmpty()) {
            viewModel.setString(countrySearch.query)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observerViewModel() {

        viewModel.countriesList.observe(this, Observer {
            if (it.isEmpty()) {
                recyclerView.visibility = View.GONE
                textEmpty.visibility = View.VISIBLE
            } else {
                recyclerView.visibility = View.VISIBLE
                textEmpty.visibility = View.GONE
            }
            (recyclerView.adapter as CountryAdapter).updateAdapter(it)
            (recyclerView.adapter as CountryAdapter).filter.filter(countrySearch.query)
        })

        viewModel.countries.observe(this, Observer {
            it?.let {
                (recyclerView.adapter as CountryAdapter).updateAdapter(it)
                (recyclerView.adapter as CountryAdapter).filter.filter(countrySearch.query)
                recyclerView.visibility = View.VISIBLE
                textEmpty.visibility = View.GONE
            }
        })

        viewModel.loading.observe(this, Observer {
            loadingBar.visibility = if (it) View.VISIBLE else View.GONE
            if (it) {
                recyclerView.visibility = View.GONE
                textError.visibility = View.GONE
                textEmpty.visibility = View.GONE
            }
        })

        viewModel.textError.observe(this, Observer {
            textError.visibility = if (it) View.VISIBLE else View.GONE
        })
    }

    inner class CountryAdapter(
        val countries: ArrayList<Country>,
        private val listener: (Country) -> Unit
    ) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>(), Filterable {

        var countryFilterList = ArrayList<Country>()

        init {
            countryFilterList = countries
        }

        inner class CountryViewHolder(view: View) : RecyclerView.ViewHolder(view) {

            private val countryName = view.textCountryName
            private val flagImg = view.imageFlag

            fun bind(country: Country) {
                countryName.text = country.countryName;

                val uri = Uri.parse(country.flag)
                //GlideToVectorYou.justLoadImage(activity,uri ,flagImg)
                GlideToVectorYou
                    .init()
                    .with(activity)
                    .setPlaceHolder(R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round)
                    .load(uri, flagImg)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CountryViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_grid_countries, parent, false)
        )

        override fun getItemCount() = countryFilterList.size

        override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
            holder.bind(countryFilterList[position])
            holder.itemView.setOnClickListener { listener(countryFilterList[position]) }
        }

        fun updateAdapter(it: List<Country>) {
            countryFilterList.clear()
            countryFilterList.addAll(it)
            notifyDataSetChanged()
        }

        override fun getFilter(): Filter {
            return object : Filter() {
                override fun performFiltering(constraint: CharSequence?): FilterResults {
                    val charSearch = constraint.toString()
                    if (charSearch.isEmpty()) {
                        countryFilterList = countries
                    } else {
                        val resultList = ArrayList<Country>()
                        for (row in countries) {
                            if (row.countryName?.toLowerCase(Locale.ROOT)
                                    ?.contains(charSearch.toLowerCase(Locale.ROOT))!!
                            ) {
                                resultList.add(row)
                            }
                        }
                        countryFilterList = resultList
                    }
                    val filterResults = FilterResults()
                    filterResults.values = countryFilterList
                    return filterResults
                }

                @Suppress("UNCHECKED_CAST")
                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                    countryFilterList = results?.values as ArrayList<Country>
                    notifyDataSetChanged()
                }
            }
        }
    }


}