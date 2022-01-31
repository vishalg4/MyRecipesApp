package com.app.ui.component.details

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
import com.app.utils.observe
import com.app.utils.toGone
import com.app.utils.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsActivity : BaseActivity() {

    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var binding: DetailsLayoutBinding
    private var menu: Menu? = null

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

    fun onClickFavorite(mi: MenuItem) {
        mi.isCheckable = false
        if (viewModel.isFavourite.value?.data == true) {
            viewModel.removeFromFavourites()
        } else {
            viewModel.addToFavourites()
        }
    }

    override fun observeViewModel() {
        observe(viewModel.recipeData, ::initializeView)
        observe(viewModel.isFavourite, ::handleIsFavourite)
    }

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

    private fun initializeView(recipesItem: RecipesItem) {
        binding.nameText.text = recipesItem.name
        binding.headlineText.text = recipesItem.headline
        binding.descriptionText.text = recipesItem.description
        Picasso.get().load(recipesItem.image).placeholder(R.drawable.ic_healthy_food_small)
                .into(binding.recipeImage)
    }
}
