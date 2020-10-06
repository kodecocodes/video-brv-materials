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

package com.raywenderlich.android.creatures.model

import android.content.Context
import androidx.preference.PreferenceManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


object Favorites {

  private const val KEY_FAVORITES = "KEY_FAVORITES"
  private val gson = Gson()

  private var favorites: MutableList<Int>? = null

  fun isFavorite(creature: Creature, context: Context): Boolean {
    return getFavorites(context)?.contains(creature.id) == true
  }

  fun addFavorite(creature: Creature, context: Context) {
    val favorites = getFavorites(context)
    favorites?.let {
      creature.isFavorite = true
      favorites.add(creature.id)
      saveFavorites(KEY_FAVORITES, favorites, context)
    }
  }

  fun removeFavorite(creature: Creature, context: Context) {
    val favorites = getFavorites(context)
    favorites?.let {
      creature.isFavorite = false
      favorites.remove(creature.id)
      saveFavorites(KEY_FAVORITES, favorites, context)
    }
  }

  public fun getFavorites(context: Context): MutableList<Int>? {
    if (favorites == null) {
      val json = sharedPrefs(context).getString(KEY_FAVORITES, "")
      val type = object : TypeToken<MutableList<Int>>() {}.type
      favorites = gson.fromJson<MutableList<Int>>(json, type) ?: return mutableListOf()
    }
    return favorites
  }

  private fun saveFavorites(key: String, list: List<Int>, context: Context) {
    val json = gson.toJson(list)
    sharedPrefs(context).edit().putString(key, json).apply()
  }

  private fun sharedPrefs(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)
}