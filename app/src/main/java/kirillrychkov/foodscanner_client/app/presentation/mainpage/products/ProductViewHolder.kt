package kirillrychkov.foodscanner_client.app.presentation.mainpage.products

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kirillrychkov.foodscanner_client.R

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvName: TextView = itemView.findViewById(R.id.tv_product_title)
    val ivProduct: ImageView = itemView.findViewById(R.id.iv_product)
    val ivProductIsValid: ImageView = itemView.findViewById(R.id.iv_product_is_valid)
    val ivFavorite: ImageView = itemView.findViewById(R.id.iv_product_favorites)
}