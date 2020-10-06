package com.raywenderlich.android.creatures.ui

import androidx.recyclerview.widget.RecyclerView

interface ItemDragListener {
    fun onItemDrag(viewHolder: RecyclerView.ViewHolder)
}