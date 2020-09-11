package `in`.antinolabs.response

data class SearchResponse(
    val results_found: Long,
    val results_start: Int,
    val results_shown: Int,
    val restaurants: List<Restaurants>
)

data class Restaurants(
    val restaurant: Restaurant
)

data class Restaurant(
    val id: String,
    val name: String,
    val url: String,
    val timings: String,
    val location: Location,
    val average_cost_for_two: Int,
    val currency: String,
    val thumb: String,
    val phone_numbers: String
)

data class Location(
    val address: String
)