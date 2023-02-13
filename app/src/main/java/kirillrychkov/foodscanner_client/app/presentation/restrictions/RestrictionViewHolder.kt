package kirillrychkov.foodscanner_client.app.presentation.restrictions

import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kirillrychkov.foodscanner_client.R

class RestrictionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvName: TextView = itemView.findViewById<TextView>(R.id.tv_name)
    val cbRestriction: CheckBox = itemView.findViewById<CheckBox>(R.id.cb_restriction)
}