package kirillrychkov.foodscanner_client.app.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Diet(
    override val id: Int,
    override val title: String,
    override val description: String
) : Restriction(), Parcelable{
}