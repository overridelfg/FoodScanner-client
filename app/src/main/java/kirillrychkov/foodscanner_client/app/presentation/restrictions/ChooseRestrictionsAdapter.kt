package kirillrychkov.foodscanner_client.app.presentation.restrictions

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Allergen
import kirillrychkov.foodscanner_client.app.domain.entity.Diet
import kirillrychkov.foodscanner_client.app.domain.entity.Restriction

class ChooseRestrictionsAdapter : RecyclerView.Adapter<RestrictionViewHolder>() {

    var restrictionsList = listOf<Restriction>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var onRestrictionCheckListener: ((Restriction) -> Unit)? = null
    var onRestrictionUncheckListener: ((Restriction) -> Unit)? = null

    var selectedDiets = mutableListOf<Diet>()

    var selectedAllergens = mutableListOf<Allergen>()

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

        if(currentItem is Diet){
            for(i in 0 until selectedDiets.size){
                if(restrictionsList[position].id == selectedDiets[i].id){
                    holder.cbRestriction.isChecked = true
                    break
                }else{
                    holder.cbRestriction.isChecked = false
                }
            }
        }
        if(currentItem is Allergen){
            for(i in 0 until selectedAllergens.size){
                if(restrictionsList[position].id == selectedAllergens[i].id){
                    holder.cbRestriction.isChecked = true
                    break
                }else{
                    holder.cbRestriction.isChecked = false
                }
            }
        }

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