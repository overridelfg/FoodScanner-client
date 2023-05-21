package kirillrychkov.foodscanner_client.app.presentation.mainpage.feedbacks.nonexistent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kirillrychkov.foodscanner_client.R

class SecondProductPhotoFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second_photo_fragment, container, false)
    }
}