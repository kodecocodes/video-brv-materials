/*
 * Copyright (c) 2018 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.raywenderlich.android.creatures.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.model.CreatureStore
import kotlinx.android.synthetic.main.fragment_favorites.*


class FavoritesFragment : Fragment() {

  private val adapter = CreatureAdapter(mutableListOf())
  private var tracker: SelectionTracker<Long>? = null

  companion object {
    fun newInstance(): FavoritesFragment {
      return FavoritesFragment()
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    favoritesRecyclerView.layoutManager = LinearLayoutManager(activity)
    favoritesRecyclerView.adapter = adapter

    tracker = SelectionTracker.Builder<Long>(
            "favoritesSelection",
            favoritesRecyclerView,
            StableIdKeyProvider(favoritesRecyclerView),
            CreatureDetailsLookup(favoritesRecyclerView),
            StorageStrategy.createLongStorage())
            .withSelectionPredicate(SelectionPredicates.createSelectAnything())
            .build()
    adapter.tracker = tracker


    val heightInPixels = resources.getDimensionPixelSize(R.dimen.list_item_divider_height)
    context?.let {
      favoritesRecyclerView.addItemDecoration(
              DividerItemDecoration(
                      ContextCompat.getColor(it, R.color.black), heightInPixels))
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
    return inflater.inflate(R.layout.fragment_favorites, container, false)
  }

  override fun onResume() {
    super.onResume()
    activity?.let {
      CreatureStore.getFavoriteCreatures(it)?.let {
        favorites ->
          adapter.updateCreatures(favorites)
      }
    }
  }

}