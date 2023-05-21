package kirillrychkov.foodscanner_client.app.presentation.mainpage.products

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Product
import kirillrychkov.foodscanner_client.app.domain.entity.ProductList
import kirillrychkov.foodscanner_client.app.domain.entity.Restriction
import javax.inject.Inject

class ProductsListAdapter(): PagingDataAdapter<Product, ProductViewHolder>(REPO_COMPARATOR) {

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
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem, onProductFavoriteClickListener, onProductSelectListener)
        }
    }


    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
                return oldItem == newItem
            }
        }
    }
}