package com.raywenderlich.android.creatures.ui

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.CompositeItem
import com.raywenderlich.android.creatures.model.Creature
import kotlinx.android.synthetic.main.list_item_creature.view.*
import kotlinx.android.synthetic.main.list_item_planet_header.view.*

class CreatureAdapter(private val compositeItems: MutableList<CompositeItem>): RecyclerView.Adapter<CreatureAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : View.OnClickListener, RecyclerView.ViewHolder(itemView) {
        private lateinit var creature: Creature

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(composite: CompositeItem) {
            if (composite.isHeader) {
                itemView.headerName.text = composite.header.name
            } else {
                this.creature = composite.creature
                val context = itemView.context
                itemView.creatureImage.setImageResource(
                        context.resources.getIdentifier(creature.uri, null, context.packageName))
                itemView.fullName.text = creature.fullName
                itemView.nickname.text = creature.nickname
                animateView(itemView)
            }

        }

        override fun onClick(view: View?) {
            view?.let {
                val context = it.context
                val intent = CreatureActivity.newIntent(context, creature.id)
                context.startActivity(intent)
            }
        }

        private fun animateView(viewToAnimate: View) {
            if (viewToAnimate.animation == null) {
                val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.scale_xy)
                viewToAnimate.animation = animation
            }
        }

    }

    enum class ViewType {
        HEADER, CREATURE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            ViewType.HEADER.ordinal -> ViewHolder(parent.inflate(R.layout.list_item_planet_header))
            ViewType.CREATURE.ordinal -> ViewHolder(parent.inflate(R.layout.list_item_creature))
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount() = compositeItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(compositeItems[position])
    }

    override fun getItemViewType(position: Int): Int {
        return if (compositeItems[position].isHeader) {
            ViewType.HEADER.ordinal
        } else {
            ViewType.CREATURE.ordinal
        }
    }

    fun updateCreatures(creatures: List<CompositeItem>) {
        this.compositeItems.clear()
        this.compositeItems.addAll(creatures)
        notifyDataSetChanged()
    }

}