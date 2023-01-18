package kirillrychkov.foodscanner_client.presentation.presentation.restrictions

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.presentation.domain.entity.Diet
import kirillrychkov.foodscanner_client.presentation.domain.entity.Restriction

class ChooseRestrictionsAdapter : RecyclerView.Adapter<RestrictionViewHolder>() {

    var restrictionsList = listOf<Restriction>()
    set(value) {
        field = value
    }

    var onRestrictionCheckListener: ((Restriction) -> Unit)? = null
    var onRestrictionUncheckListener: ((Restriction) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestrictionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restriction, parent, false)
        return RestrictionViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestrictionViewHolder, position: Int) {
        val currentItem = restrictionsList[position]
        holder.tvName.text = currentItem.title
        holder.cbRestriction.setOnClickListener {
            if((it as CheckBox).isChecked){
                onRestrictionCheckListener?.invoke(currentItem)
            }else{
                onRestrictionUncheckListener?.invoke(currentItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return restrictionsList.size
    }
}