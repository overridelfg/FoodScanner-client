package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.restrictionslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.app.domain.entity.Restriction

class ProfileRestrictionsAdapter : RecyclerView.Adapter<ProfileRestrictionsViewHolder>() {

    var restrictionsList = listOf<Restriction>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var onRestrictionInfoListener: ((Restriction) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProfileRestrictionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_restrictions, parent, false)
        return ProfileRestrictionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfileRestrictionsViewHolder, position: Int) {
        val currentItem = restrictionsList[position]
        holder.tvTitle.text = currentItem.title
        holder.buttonInfo.setOnClickListener {
            onRestrictionInfoListener?.invoke(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return restrictionsList.size
    }
}