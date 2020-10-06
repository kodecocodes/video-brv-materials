package com.raywenderlich.android.creatures.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration(private val spanCount: Int, private val spacing: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)

        outRect.top = spacing / 2
        outRect.bottom = spacing / 2
        outRect.left = spacing / 2
        outRect.right = spacing / 2

        if (position < spanCount) {
            outRect.top = spacing
        }

        if (position % spanCount == 0) {
            outRect.left = spacing
        }

        if ((position + 1) % spanCount == 0) {
            outRect.right = spacing
        }

    }

}