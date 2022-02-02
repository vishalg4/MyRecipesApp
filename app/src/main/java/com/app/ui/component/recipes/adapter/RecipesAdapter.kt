package com.app.ui.component.recipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.data.dto.recipes.RecipesItem
import com.app.databinding.RecipeItemBinding
import com.app.ui.base.listeners.RecyclerItemListener
import com.app.ui.component.recipes.RecipesListViewModel

/**
 * Recipe Adapter used to update [RecyclerView]
 * @param [recipesListViewModel] use to perform operation with recipe list.
 * @param [recipes] list of [RecipesItem]
 */
class RecipesAdapter(
    private val recipesListViewModel: RecipesListViewModel,
    private val recipes: List<RecipesItem>
) : RecyclerView.Adapter<RecipeViewHolder>() {

    private val onItemClickListener: RecyclerItemListener = object : RecyclerItemListener {
        override fun onItemSelected(recipe: RecipesItem) {
            recipesListViewModel.openRecipeDetails(recipe)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemBinding = RecipeItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RecipeViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position], onItemClickListener)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}

