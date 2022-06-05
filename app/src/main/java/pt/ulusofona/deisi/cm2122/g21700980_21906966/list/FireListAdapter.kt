package pt.ulusofona.deisi.cm2122.g21700980_21906966.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ulusofona.deisi.cm2122.g21700980_21906966.R
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI
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
        if (!item.api) {
            holder.binding.fireIcon.setImageResource(R.drawable.ic_fire_blue)
        }
        holder.binding.fireId.text = "Fogo em ${item.county}"
        holder.binding.fireState.text = "Estado: ${item.status}"
        holder.binding.firePlace.text = item.location
        holder.itemView.setOnClickListener { onClick(item) }
        holder.itemView.setOnLongClickListener { onLongClick(item) }
    }

    override fun getItemCount() = items.size

    fun updateItems(items: List<FireUI>) {
        this.items = items
        notifyDataSetChanged()
    }
}
