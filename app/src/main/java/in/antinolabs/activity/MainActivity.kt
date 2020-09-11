package `in`.antinolabs.activity

import `in`.antinolabs.R
import `in`.antinolabs.adapter.RestaurantAdapter
import `in`.antinolabs.databinding.ActivityMainBinding
import `in`.antinolabs.extras.Constants
import `in`.antinolabs.viewmodel.MyViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : AppCompatActivity() {

    private var longitude : Double = 0.0
    private var latitude : Double = 0.0
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        longitude = intent.getDoubleExtra(Constants.longitude,0.0)
        latitude = intent.getDoubleExtra(Constants.latitude,0.0)

        binding.recyclerview.layoutManager = LinearLayoutManager(this)

        val myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        myViewModel.searchRestaurants(latitude,longitude).observe(this, Observer {
            it.let {
                val restaurantAdapter = RestaurantAdapter(this)
                restaurantAdapter.addItems(it.restaurants)
                binding.recyclerview.adapter = restaurantAdapter
                restaurantAdapter.notifyDataSetChanged()
            }
        })

    }
}