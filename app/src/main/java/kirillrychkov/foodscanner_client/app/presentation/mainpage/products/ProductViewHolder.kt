package kirillrychkov.foodscanner_client.app.presentation.mainpage.products

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Product

class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvName: TextView = itemView.findViewById(R.id.tv_product_title)
    val ivProduct: ImageView = itemView.findViewById(R.id.iv_product)
    val ivProductIsValid: ImageView = itemView.findViewById(R.id.iv_product_is_valid)
    val ivFavorite: ImageView = itemView.findViewById(R.id.iv_product_favorites)

    fun bind(
        product: Product,
        onProductFavoriteClickListener: ((Long) -> Unit)?,
        onProductSelectListener: ((Product) -> Unit)?
    ) {
        tvName.text = product.Name
        if (product.isFavorite) {
            ivFavorite.setBackgroundResource(R.drawable.ic_baseline_bookmark_24)
            ivFavorite.isSelected = true
        } else {
            ivFavorite.setBackgroundResource(R.drawable.ic_bookmark)
            ivFavorite.isSelected = false
        }
        if (product.isValid) {
            ivProductIsValid.setBackgroundResource(R.drawable.ic_check)
        } else {
            ivProductIsValid.setBackgroundResource(R.drawable.ic_cross)
        }

        if (product.Jpg.isBlank()) {
            Picasso.get().load(R.drawable.no_pictures).into(ivProduct);
        } else {
            try {
                Picasso.get().isLoggingEnabled = true
                Picasso.get().load(product.Jpg)
                    .placeholder(R.color.white)
                    .error(R.drawable.no_pictures)
                    .fit()
                    .centerInside().into(ivProduct);
            } catch (e: Exception) {
                Picasso.get().load(R.drawable.no_pictures).into(ivProduct);
            }

        }

        ivFavorite.setOnClickListener {
            if (it.isSelected) {
                ivFavorite.isSelected = false
                ivFavorite.setBackgroundResource(R.drawable.ic_bookmark)
            } else {
                ivFavorite.isSelected = true
                ivFavorite.setBackgroundResource(R.drawable.ic_baseline_bookmark_24)
            }

            onProductFavoriteClickListener?.invoke(product.id)
        }

        itemView.setOnClickListener {
            onProductSelectListener?.invoke(product)
        }
    }
}