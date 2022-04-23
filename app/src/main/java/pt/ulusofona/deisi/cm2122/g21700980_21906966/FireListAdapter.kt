package pt.ulusofona.deisi.cm2122.g21700980_21906966

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.ListItemFireBinding

class FireListAdapter(
    private var items: List<FireUI> = listOf(),
    private val onClick: (FireUI) -> Unit,
    private val onLongClick: (FireUI) -> Boolean
) : RecyclerView.Adapter<FireListAdapter.FireListViewHolder>() {

    class FireListViewHolder(val binding: ListItemFireBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FireListViewHolder {
        return FireListViewHolder(
            ListItemFireBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FireListViewHolder, position: Int) {
        val item = items[position]
        holder.binding.fireId.text = "Fogo em ${item.parish} [${item.uuid.take(8)}]"
        holder.binding.fireState.text = "Estado: ${item.state}"
        holder.binding.firePlace.text = item.place
        holder.itemView.setOnClickListener { onClick(item) }
        holder.itemView.setOnLongClickListener { onLongClick(item) }
    }

    override fun getItemCount() = items.size

    fun updateItems(items: List<FireUI>) {
        this.items = items
        notifyDataSetChanged()
    }
}
