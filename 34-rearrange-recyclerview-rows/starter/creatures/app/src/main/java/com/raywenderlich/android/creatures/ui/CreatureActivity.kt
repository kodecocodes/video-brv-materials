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

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.raywenderlich.android.creatures.R
import com.raywenderlich.android.creatures.model.Favorites
import com.raywenderlich.android.creatures.model.Creature
import com.raywenderlich.android.creatures.model.CreatureStore
import kotlinx.android.synthetic.main.activity_creature.*

class CreatureActivity : AppCompatActivity() {

  private lateinit var creature: Creature
  private val adapter = FoodAdapter(mutableListOf())

  companion object {
    private const val EXTRA_CREATURE_ID = "EXTRA_CREATURE_ID"

    fun newIntent(context: Context, creatureId: Int): Intent {
      val intent = Intent(context, CreatureActivity::class.java)
      intent.putExtra(EXTRA_CREATURE_ID, creatureId)
      return intent
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_creature)

    setupCreature()
    setupTitle()
    setupViews()
    setupFavoriteButton()
    setupFoods()
  }

  private fun setupCreature() {
    val creatureById = CreatureStore.getCreatureById(intent.getIntExtra(EXTRA_CREATURE_ID, 1))
    if (creatureById == null) {
      Toast.makeText(this, getString(R.string.invalid_creature), Toast.LENGTH_SHORT).show()
      finish()
    } else {
      creature = creatureById
    }
  }

  private fun setupTitle() {
    title = String.format(getString(R.string.detail_title_format), creature.nickname)
    supportActionBar?.setDisplayHomeAsUpEnabled(true)
  }

  private fun setupViews() {
    headerImage.setImageResource(resources.getIdentifier(creature.uri, null, packageName))
    fullName.text = creature.fullName
    planet.text = creature.planet
  }

  private fun setupFavoriteButton() {
    setupFavoriteButtonImage(creature)
    setupFavoriteButtonClickListener(creature)
  }

  private fun setupFavoriteButtonImage(creature: Creature) {
    if (creature.isFavorite) {
      favoriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_black_24dp))
    } else {
      favoriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_black_24dp))
    }
  }

  private fun setupFavoriteButtonClickListener(creature: Creature) {
    favoriteButton.setOnClickListener { _ ->
      if (creature.isFavorite) {
        favoriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_border_black_24dp))
        Favorites.removeFavorite(creature, this)
      } else {
        favoriteButton.setImageDrawable(getDrawable(R.drawable.ic_favorite_black_24dp))
        Favorites.addFavorite(creature, this)
      }
    }
  }

  private fun setupFoods(){
    foodRecyclerView.layoutManager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
    foodRecyclerView.adapter = adapter
    val foods = CreatureStore.getCreatureFoods(creature)
    adapter.updateFoods(foods)
    val dividerWidthInPixels = resources.getDimensionPixelSize(R.dimen.list_item_divider_height)
    foodRecyclerView.addItemDecoration(
            FoodItemDecoration(
                    ContextCompat.getColor(this, R.color.black), dividerWidthInPixels
            ))
  }

}
