package pt.ulusofona.deisi.cm2122.g21700980_21906966.extra

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pt.ulusofona.deisi.cm2122.g21700980_21906966.fire.FireUI
import pt.ulusofona.deisi.cm2122.g21700980_21906966.databinding.ListItemFireBinding

class ExtraAdapter(
    private var items: List<FireUI> = listOf(),
    private val onClick: (FireUI) -> Unit,
    private val onLongClick: (FireUI) -> Boolean
) : RecyclerView.Adapter<ExtraAdapter.ExtraViewHolder>() {
//FragmentExtraBinding
    class ExtraViewHolder(val binding: ListItemFireBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtraViewHolder {
        return ExtraViewHolder(
            ListItemFireBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExtraViewHolder, position: Int) {
        val item = items[position]
        holder.binding.fireId.text = "Fogo em ${item.parish} [${item.uuid.take(8)}]"
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
