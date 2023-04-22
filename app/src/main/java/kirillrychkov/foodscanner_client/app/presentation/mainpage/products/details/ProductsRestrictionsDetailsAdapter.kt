package kirillrychkov.foodscanner_client.app.presentation.mainpage.products.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.ProductRestriction

class ProductsRestrictionsDetailsAdapter(): RecyclerView.Adapter<ProductsRestrictionsDetailsAdapter.ProductRestrictionDetailsViewHolder>() {

    var productsList = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductRestrictionDetailsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_restrictions_details, parent, false)
        return ProductRestrictionDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductRestrictionDetailsViewHolder, position: Int) {
        val currentItem = productsList[position]
        val listOfRestrictions = currentItem.split(':')
        holder.tvTitle.text = listOfRestrictions[0]
        val setOfIngredients = mutableSetOf<String>()
        for(i in 0 until listOfRestrictions[1].split(',').size){
            setOfIngredients.add(listOfRestrictions[1].split(',')[i])
        }
        holder.tvIngredients.text = "Запрещенные игрендиенты: " + setOfIngredients.joinToString(", ")
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    class ProductRestrictionDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_restricted_ingredients_details_title)
        val tvIngredients: TextView = itemView.findViewById(R.id.tv_restricted_ingredients_details)
    }

}