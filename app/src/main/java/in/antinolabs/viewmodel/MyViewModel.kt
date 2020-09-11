package `in`.antinolabs.viewmodel

import `in`.antinolabs.response.SearchResponse
import `in`.antinolabs.rest.Coroutines
import `in`.antinolabs.rest.RetrofitClient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {

    fun searchRestaurants(latitude : Double, longitude : Double) : LiveData<SearchResponse>{
        val data : MutableLiveData<SearchResponse> = MutableLiveData()
        Coroutines.io {
            val response = RetrofitClient.getInstance().api.getRestaurants(latitude.toString(),longitude.toString())
            if (response.isSuccessful){
                data.postValue(response.body())
            }else{
                data.postValue(null)
            }
        }
        return data
    }
}