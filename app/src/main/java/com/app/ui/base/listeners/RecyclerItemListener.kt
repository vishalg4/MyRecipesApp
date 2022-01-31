package com.app.ui.base.listeners

import com.app.data.dto.recipes.RecipesItem


interface RecyclerItemListener {
    fun onItemSelected(recipe : RecipesItem)
}
