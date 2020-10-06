package com.raywenderlich.android.creatures.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.app.Constants
import com.raywenderlich.android.creatures.app.inflate
import com.raywenderlich.android.creatures.model.Creature
import kotlinx.android.synthetic.main.list_item_creature_card.view.*
import kotlinx.android.synthetic.main.list_item_creature_card.view.creatureCard
import kotlinx.android.synthetic.main.list_item_creature_card.view.creatureImage
import kotlinx.android.synthetic.main.list_item_creature_card.view.fullName
import kotlinx.android.synthetic.main.list_item_creature_card.view.nameHolder
import kotlinx.android.synthetic.main.list_item_creature_card_jupiter.view.*
import java.lang.IllegalArgumentException

class CreatureCardAdapter(private val creatures: MutableList<Creature>): RecyclerView.Adapter<CreatureCardAdapter.ViewHolder>() {

    enum class ScrollDirection {
        UP, DOWN
    }
    enum class ViewType {
        JUPITER, MARS, OTHER
    }
    var scrollDirection = ScrollDirection.DOWN
    var jupiterSpanSize = 2

    inner class ViewHolder(itemView: View) : View.OnClickListener, RecyclerView.ViewHolder(itemView) {
        private lateinit var creature: Creature

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(creature: Creature) {
            this.creature = creature
            val context = itemView.context
            val imageResource = context.resources.getIdentifier(creature.uri, null, context.packageName)
            itemView.creatureImage.setImageResource(imageResource)
            itemView.fullName.text = creature.fullName
            setBackgroundColors(context, imageResource)
            animateView(itemView)
        }

        override fun onClick(view: View?) {
            view?.let {
                val context = it.context
                val intent = CreatureActivity.newIntent(context, creature.id)
                context.startActivity(intent)
            }
        }

        private fun setBackgroundColors(context: Context, imageResource: Int) {
            val image = BitmapFactory.decodeResource(context.resources, imageResource)
            Palette.from(image).generate { palette ->
                palette?.let {
                    val backgroundColor = it.getDominantColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))
                    itemView.creatureCard.setBackgroundColor(backgroundColor)
                    itemView.nameHolder.setBackgroundColor(backgroundColor)
                    val textColor = if (isColorDark(backgroundColor)) Color.WHITE else Color.BLACK
                    itemView.fullName.setTextColor(textColor)
                    if (itemView.slogan != null) {
                        itemView.slogan.setTextColor(textColor)
                    }
                }
            }
        }

        private fun animateView(viewToAnimate: View) {
            if (viewToAnimate.animation == null) {
                val animation = AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.scale_xy)
                viewToAnimate.animation = animation
            }
        }

        private fun isColorDark(color: Int): Boolean {
            val darkness = 1 - (0.299 * Color.red(color) +
                                0.587 * Color.green(color) +
                                0.114 * Color.blue(color)) / 255
            return darkness >= 0.5
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            ViewType.OTHER.ordinal -> ViewHolder(parent.inflate(R.layout.list_item_creature_card))
            ViewType.JUPITER.ordinal -> ViewHolder(parent.inflate(R.layout.list_item_creature_card_jupiter))
            ViewType.MARS.ordinal -> ViewHolder(parent.inflate(R.layout.list_item_creature_card_mars))
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemCount() = creatures.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(creatures[position])
    }

    override fun getItemViewType(position: Int) =
        when (creatures[position].planet) {
            Constants.JUPITER -> ViewType.JUPITER.ordinal
            Constants.MARS -> ViewType.MARS.ordinal
            else -> ViewType.OTHER.ordinal
        }

    fun spanSizeAtPosition(position: Int): Int {
        return if (creatures[position].planet == Constants.JUPITER) {
            jupiterSpanSize
        } else {
            1
        }
    }

}