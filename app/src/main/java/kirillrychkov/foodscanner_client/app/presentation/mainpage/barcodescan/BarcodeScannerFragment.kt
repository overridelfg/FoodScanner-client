package kirillrychkov.foodscanner_client.app.presentation.mainpage.barcodescan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kirillrychkov.foodscanner_client.R
import kirillrychkov.foodscanner_client.databinding.FragmentBarcodeScannerBinding


class BarcodeScannerFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FragmentBarcodeScannerBinding.inflate(layoutInflater)
        return inflater.inflate(R.layout.fragment_barcode_scanner, container, false)
    }

}