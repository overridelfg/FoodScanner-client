package kirillrychkov.foodscanner_client.app.presentation.mainpage.products

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.entity.ProductList
import kirillrychkov.foodscanner_client.app.domain.entity.Restriction
import javax.inject.Inject

class ProductsListAdapter(): RecyclerView.Adapter<ProductViewHolder>() {

    var productsList = listOf<Product>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onProductSelectListener: ((Product) -> Unit)? = null
    var onProductFavoriteClickListener: ((Long) -> Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_list, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = productsList[position]
        holder.tvName.text = currentItem.Name
        if(currentItem.isFavorite){
            holder.ivFavorite.setBackgroundResource(R.drawable.ic_baseline_bookmark_24)
            holder.ivFavorite.isSelected = true
        }else{
            holder.ivFavorite.setBackgroundResource(R.drawable.ic_bookmark)
            holder.ivFavorite.isSelected = false
        }
        if(currentItem.isValid){
            holder.ivProductIsValid.setBackgroundResource(R.drawable.ic_check)
        }else{
            holder.ivProductIsValid.setBackgroundResource(R.drawable.ic_cross)
        }

        if(currentItem.Jpg.isBlank()){
            Picasso.get().load(R.drawable.no_pictures).into(holder.ivProduct);
        }else{
            try{
                Picasso.get().isLoggingEnabled = true
                Picasso.get().load(currentItem.Jpg)
                    .placeholder(R.color.white)
                    .error(R.drawable.no_pictures)
                    .fit()
                    .centerInside().
                    into(holder.ivProduct);
            }catch (e: Exception){
                Picasso.get().load(R.drawable.no_pictures).into(holder.ivProduct);
            }

        }

        holder.ivFavorite.setOnClickListener {
            if(it.isSelected){
                holder.ivFavorite.isSelected = false
                holder.ivFavorite.setBackgroundResource(R.drawable.ic_bookmark)
            }else{
                holder.ivFavorite.isSelected = true
                holder.ivFavorite.setBackgroundResource(R.drawable.ic_baseline_bookmark_24)
            }

            onProductFavoriteClickListener?.invoke(currentItem.id)
        }

        holder.itemView.setOnClickListener {
            onProductSelectListener?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }
}