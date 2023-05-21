package kirillrychkov.foodscanner_client.app.presentation.mainpage.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Product

class ProductsListRecyclerAdapter(): RecyclerView.Adapter<ProductViewHolder>() {

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
        holder.bind(currentItem, onProductFavoriteClickListener, onProductSelectListener)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }
}