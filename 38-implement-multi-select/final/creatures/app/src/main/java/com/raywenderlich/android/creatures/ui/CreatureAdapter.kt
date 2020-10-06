package com.raywenderlich.android.creatures.ui

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.Creature
import kotlinx.android.synthetic.main.list_item_creature.view.*

class CreatureAdapter(private val creatures: MutableList<Creature>): RecyclerView.Adapter<CreatureAdapter.ViewHolder>() {

    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    class ViewHolder(itemView: View) : View.OnClickListener, RecyclerView.ViewHolder(itemView) {
        private lateinit var creature: Creature

        init {
            itemView.setOnClickListener(this)
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
                object: ItemDetailsLookup.ItemDetails<Long>() {
                    override fun getPosition(): Int = adapterPosition
                    override fun getSelectionKey(): Long? = itemId
                }

        fun bind(creature: Creature, isSelected: Boolean) {
            this.creature = creature
            val context = itemView.context
            itemView.creatureImage.setImageResource(
                    context.resources.getIdentifier(creature.uri, null, context.packageName))
            itemView.fullName.text = creature.fullName
            itemView.nickname.text = creature.nickname
            if (isSelected) {
                itemView.setBackgroundColor(
                        ContextCompat.getColor(context, R.color.selectedItem)
                )
            } else {
                itemView.setBackgroundColor(0)
            }

        }

        override fun onClick(view: View?) {
            view?.let {
                val context = it.context
                val intent = CreatureActivity.newIntent(context, creature.id)
                context.startActivity(intent)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.list_item_creature))
    }

    override fun getItemCount() = creatures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val isSelected = tracker?.isSelected(position.toLong()) ?: false
        holder.bind(creatures[position], isSelected)
    }

    override fun getItemId(position: Int): Long = position.toLong()

    fun updateCreatures(creatures: List<Creature>) {
        this.creatures.clear()
        this.creatures.addAll(creatures)
        notifyDataSetChanged()
    }

}