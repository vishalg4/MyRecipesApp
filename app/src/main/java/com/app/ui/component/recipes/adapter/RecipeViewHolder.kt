package com.app.ui.component.recipes.adapter

import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.app.R
import com.app.data.dto.recipes.RecipesItem
import com.app.databinding.RecipeItemBinding
import com.app.ui.base.listeners.RecyclerItemListener

class RecipeViewHolder(private val itemBinding: RecipeItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(recipesItem: RecipesItem, recyclerItemListener: RecyclerItemListener) {
        itemBinding.captionText.text = recipesItem.description
        itemBinding.nameText.text = recipesItem.name
        Picasso.get().load(recipesItem.thumb)
            .placeholder(R.drawable.ic_healthy_food)
            .error(R.drawable.ic_healthy_food)
            .into(itemBinding.recipeItemImage)

        itemBinding.recipeItemCardView.setOnClickListener {
            recyclerItemListener.onItemSelected(
                recipesItem
            )
        }
    }
}

