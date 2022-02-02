package com.app.ui.base.listeners

import androidx.recyclerview.widget.RecyclerView
import com.app.data.dto.recipes.RecipesItem

/**
 * This interface define method used to handle [RecyclerView] item events.
 */
interface RecyclerItemListener {
    /**
     * This function is define to handle item select even.
     * @param [recipe] selected RecipesItem
     */
    fun onItemSelected(recipe: RecipesItem)
}
