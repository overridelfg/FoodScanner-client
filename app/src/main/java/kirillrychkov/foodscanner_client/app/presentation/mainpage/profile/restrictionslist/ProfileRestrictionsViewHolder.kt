package kirillrychkov.foodscanner_client.app.presentation.mainpage.profile.restrictionslist

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kirillrychkov.foodscanner_client.R

class ProfileRestrictionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvTitle: TextView = itemView.findViewById<TextView>(R.id.tv_title)
    val buttonInfo: ImageView = itemView.findViewById(R.id.button_info_restriction)
}