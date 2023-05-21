package kirillrychkov.foodscanner_client.app.presentation.mainpage.products

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import kirillrychkov.foodscanner_client.R

class ProductListLoaderStateAdapter(private val retry : () -> Unit) : LoadStateAdapter<ProductListLoaderStateAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.loader_state_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.setData(loadState)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun setData(state: LoadState){
            val pbLoadMore: ProgressBar = itemView.findViewById<ProgressBar>(R.id.pb_load_more)
            val tvError = itemView.findViewById<TextView>(R.id.error_txt)
            val buttonError = itemView.findViewById<Button>(R.id.error_button)
            buttonError.setOnClickListener {
                retry()
            }
            tvError.isVisible = state is LoadState.Error
            buttonError.isVisible = state is LoadState.Error
            pbLoadMore.isVisible = state is LoadState.Loading

        }

    }

}