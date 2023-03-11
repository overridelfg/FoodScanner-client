package kirillrychkov.foodscanner_client.app.presentation.mainpage.products

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.entity.Restriction
import javax.inject.Inject

class ProductsListAdapter(): RecyclerView.Adapter<ProductViewHolder>() {

    var productsList = listOf<Product>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onProductSelectListener: ((Product) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_list, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = productsList[position]
        holder.tvName.text = currentItem.Name
        if(currentItem.Jpg.isBlank()){
            Picasso.get().load(R.drawable.nopictures).into(holder.ivProduct);
        }else{
            try{
                Picasso.get().isLoggingEnabled = true
                Picasso.get().load(currentItem.Jpg)
                    .placeholder(R.color.white)
                    .error(R.drawable.nopictures)
                    .fit()
                    .centerInside().
                    into(holder.ivProduct);
            }catch (e: Exception){
                Picasso.get().load(R.drawable.nopictures).into(holder.ivProduct);
            }

        }
        holder.itemView.setOnClickListener {
            onProductSelectListener?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return productsList.size
    }
}