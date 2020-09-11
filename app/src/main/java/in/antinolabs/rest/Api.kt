package `in`.antinolabs.rest

import `in`.antinolabs.response.SearchResponse
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @Headers("user-key: 11158f452637205aa6645570b0aa1f32")
    @GET("search")
    suspend fun getRestaurants(
        @Query("lat") latitude : String,
        @Query("lon") longitude : String
    ) : Response<SearchResponse>

}