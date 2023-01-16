package kirillrychkov.foodscanner_client.presentation.presentation.restrictions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kirillrychkov.foodscanner_client.R

class ChooseRestrictionsAdapter : RecyclerView.Adapter<RestrictionViewHolder>() {

    var restrictionsList = mutableListOf<String>()
    set(value) {
        field = value
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestrictionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restriction, parent, false)
        return RestrictionViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestrictionViewHolder, position: Int) {
        holder.tvName.text = restrictionsList[position]
    }

    override fun getItemCount(): Int {
        return restrictionsList.size
    }
}