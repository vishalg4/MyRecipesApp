package com.app.ui.component.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.squareup.picasso.Picasso
import com.app.R
import com.app.RECIPE_ITEM_KEY
import com.app.data.Resource
import com.app.data.dto.recipes.RecipesItem
import com.app.databinding.DetailsLayoutBinding
import com.app.ui.base.BaseActivity
import com.app.ui.component.recipes.RecipesListActivity
import com.app.utils.observe
import com.app.utils.toGone
import com.app.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

/**
 * This Activity is used to show details of selected Recipe.
 */
@AndroidEntryPoint
class DetailsActivity : BaseActivity() {
    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var binding: DetailsLayoutBinding
    private var menu: Menu? = null

    /**
     * Provide instance of this activity.
     */
    companion object {
        fun getInstance(context: Context, recipesItem: RecipesItem): Intent =
            Intent(context, DetailsActivity::class.java).apply {
                putExtra(RECIPE_ITEM_KEY, recipesItem)
            }
    }

    /**
     * Initializing binding object to bind View with Activity.
     */
    override fun initViewBinding() {
        binding = DetailsLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initRecipeData(intent.getParcelableExtra(RECIPE_ITEM_KEY) ?: RecipesItem())
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        this.menu = menu
        viewModel.isFavourites()
        return true
    }

    /**
     * This method will be called when user will click menu item to add recipe in favorites.
     * @param [menuItem] selected menu item.
     */
    fun onClickFavorite(menuItem: MenuItem) {
        menuItem.isCheckable = false
        if (viewModel.isFavourite.value?.data == true) {
            viewModel.removeFromFavourites()
        } else {
            viewModel.addToFavourites()
        }
    }

    /**
     * View Models observed by [DetailsActivity]
     */
    override fun observeViewModel() {
        observe(viewModel.recipeData, ::initializeView)
        observe(viewModel.isFavourite, ::handleIsFavourite)
    }

    /**
     * Private method use to update progressbar accordingly to the recipe
     * whether it's a favourite recipe or not.
     * @param [isFavourite] is Resource<Boolean>, define recipe's
     * favourite status(true/false).
     */
    private fun handleIsFavourite(isFavourite: Resource<Boolean>) {
        when (isFavourite) {
            is Resource.Loading -> {
                binding.loadingProgressBar.toVisible()
            }
            is Resource.Success -> {
                isFavourite.data?.let {
                    handleIsFavouriteUI(it)
                    menu?.findItem(R.id.add_to_favorite)?.isCheckable = true
                    binding.loadingProgressBar.toGone()
                }
            }
            is Resource.DataError -> {
                menu?.findItem(R.id.add_to_favorite)?.isCheckable = true
                binding.loadingProgressBar.toGone()
            }
        }
    }

    /**
     * Private method use to update favorite Icon based on recipe's
     * favourite status(true/false)
     * @param [isFavourite] is Resource<Boolean>, define recipe's
     * favourite status(true/false).
     */
    private fun handleIsFavouriteUI(isFavourite: Boolean) {
        menu?.let {
            it.findItem(R.id.add_to_favorite)?.icon =
                if (isFavourite) {
                    ContextCompat.getDrawable(this, R.drawable.ic_star_24)
                } else {
                    ContextCompat.getDrawable(this, R.drawable.ic_outline_star_border_24)
                }
        }
    }

    /**
     * Initializing View for given [RecipesItem]
     */
    private fun initializeView(recipesItem: RecipesItem) {
        binding.nameText.text = recipesItem.name
        binding.headlineText.text = recipesItem.headline
        binding.descriptionText.text = recipesItem.description
        Picasso.get().load(recipesItem.image).placeholder(R.drawable.ic_healthy_food_small)
            .into(binding.recipeImage)
    }
}
