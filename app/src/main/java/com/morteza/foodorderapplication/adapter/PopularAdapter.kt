package com.morteza.foodorderapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.morteza.foodorderapplication.R
import com.morteza.foodorderapplication.databinding.ItemPopularBinding
import javax.inject.Inject
import com.morteza.foodorderapplication.models.ResponseRecipes.Result
import com.morteza.foodorderapplication.utils.Constants


class PopularAdapter @Inject constructor():RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    private lateinit var binding: ItemPopularBinding
    private var items = emptyList<Result>()



    inner class ViewHolder():RecyclerView.ViewHolder(binding.root) {
        fun bind(item:Result) {
            binding.apply {
                //Text
                popularNameTxt.text = item.title
                popularPriceTxt.text = "${item.pricePerServing} $"
                //Image
                val imageSplit = item.image!!.split("-")
                val imageSize = imageSplit[1].replace(Constants.OLD_IMAGE_SIZE, Constants.NEW_IMAGE_SIZE)
                popularImg.load("${imageSplit[0]}-$imageSize") {
                    crossfade(true)
                    crossfade(800)
                    memoryCachePolicy(CachePolicy.ENABLED)
                    error(R.drawable.ic_placeholder)
                }
                //Click
                root.setOnClickListener {
                    onItemClickListener?.let { it(item.id!!) }
                }
            }
        }
    }

    private var onItemClickListener: ((Int) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemPopularBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(items[position])
    }
}