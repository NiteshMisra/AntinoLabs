package `in`.antinolabs.adapter

import `in`.antinolabs.R
import `in`.antinolabs.databinding.ElementItemBinding
import `in`.antinolabs.response.Restaurant
import `in`.antinolabs.response.Restaurants
import android.content.Context
import assignment.chatapp.adapter.BaseRecyclerViewAdapter
import com.bumptech.glide.Glide

class RestaurantAdapter(var context: Context) : BaseRecyclerViewAdapter<Restaurants,ElementItemBinding>(){

    override fun getLayout() = R.layout.element_item

    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<ElementItemBinding>,
        position: Int
    ) {
        val currentItem = items[position].restaurant

        holder.binding.title.text = currentItem.name
        holder.binding.address.text = currentItem.location.address

        if (currentItem.thumb.isNotEmpty()){
            Glide.with(context).load(currentItem.thumb).into(holder.binding.image)
        }
    }

}