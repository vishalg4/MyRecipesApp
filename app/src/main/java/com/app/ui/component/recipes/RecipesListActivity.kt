package com.app.ui.component.recipes

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.SearchView
import android.widget.SearchView.OnQueryTextListener
import androidx.activity.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.app.R
import com.app.RECIPE_ITEM_KEY
import com.app.data.Resource
import com.app.data.dto.recipes.Recipes
import com.app.data.dto.recipes.RecipesItem
import com.app.data.error.SEARCH_ERROR
import com.app.databinding.HomeActivityBinding
import com.app.ui.base.BaseActivity
import com.app.ui.component.details.DetailsActivity
import com.app.ui.component.recipes.adapter.RecipesAdapter
import com.app.utils.*
import dagger.hilt.android.AndroidEntryPoint

/**
 * This activity is used to show list of Recipes.
 */
@AndroidEntryPoint
class RecipesListActivity : BaseActivity() {
    private lateinit var binding: HomeActivityBinding
    private lateinit var recipesAdapter: RecipesAdapter

    private val recipesListViewModel: RecipesListViewModel by viewModels()

    /**
     * Provide instance of this activity.
     */
    companion object {
        fun getInstance(context: Context): Intent =
            Intent(context, RecipesListActivity::class.java)
    }

    /**
     * Initializing binding object to bind View with Activity.
     */
    override fun initViewBinding() {
        binding = HomeActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.title = getString(R.string.activity_recipes_list_action_bar_title)
        val layoutManager = LinearLayoutManager(this)
        binding.rvRecipesList.layoutManager = layoutManager
        binding.rvRecipesList.setHasFixedSize(true)
        recipesListViewModel.getRecipes()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_actions, menu)
        // Associate searchable configuration with the SearchView
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.queryHint = getString(R.string.activity_recipe_list_search_hint)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }

        /**
         * Added listener to SearchView get search even callbacks.
         */
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                handleSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> recipesListViewModel.getRecipes()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * Private method, used to update progressbar state when search is initiated
     * @param [query] string searched in SearchView
     */
    private fun handleSearch(query: String) {
        if (query.isNotEmpty()) {
            binding.loadingProgressBar.visibility = VISIBLE
            recipesListViewModel.onSearchClick(query)
        }
    }

    /**
     * Update RecyclerView's Adapter [RecipesAdapter] to update recipe list.
     */
    private fun bindListData(recipes: Recipes) {
        if (!(recipes.recipesList.isNullOrEmpty())) {
            recipesAdapter = RecipesAdapter(recipesListViewModel, recipes.recipesList)
            binding.rvRecipesList.adapter = recipesAdapter
            showDataView(true)
        } else {
            showDataView(false)
        }
    }

    /**
     * Use to navigate to [DetailsActivity] for selected recipe.
     * @param [navigateEvent] is [SingleEvent], contains selected [RecipesItem]
     */
    private fun navigateToDetailsScreen(navigateEvent: SingleEvent<RecipesItem>) {
        navigateEvent.getContentIfNotHandled()?.let {
            val nextScreenIntent = DetailsActivity.getInstance(this, it)
            startActivity(nextScreenIntent)
        }
    }

    /**
     * Setup and observe SnackBar ViewModel.
     */
    private fun observeSnackBarMessages(event: LiveData<SingleEvent<Any>>) {
        binding.root.setupSnackbar(this, event, Snackbar.LENGTH_LONG)
    }

    /**
     * Shows SnackBar message to the user.
     */
    private fun observeToast(event: LiveData<SingleEvent<Any>>) {
        binding.root.showToast(this, event, Snackbar.LENGTH_LONG)
    }

    /**
     * Used to show search error message.
     */
    private fun showSearchError() {
        recipesListViewModel.showToastMessage(SEARCH_ERROR)
    }

    private fun showDataView(show: Boolean) {
        binding.noDataText.visibility = if (show) GONE else VISIBLE
        binding.rvRecipesList.visibility = if (show) VISIBLE else GONE
        binding.loadingProgressBar.toGone()
    }

    private fun showLoadingView() {
        binding.loadingProgressBar.toVisible()
        binding.noDataText.toGone()
        binding.rvRecipesList.toGone()
    }


    private fun showSearchResult(recipesItem: RecipesItem) {
        recipesListViewModel.openRecipeDetails(recipesItem)
        binding.loadingProgressBar.toGone()
    }

    private fun noSearchResult(unit: Unit) {
        showSearchError()
        binding.loadingProgressBar.toGone()
    }

    private fun handleRecipesList(status: Resource<Recipes>) {
        when (status) {
            is Resource.Loading -> showLoadingView()
            is Resource.Success -> status.data?.let { bindListData(recipes = it) }
            is Resource.DataError -> {
                showDataView(false)
                status.errorCode?.let { recipesListViewModel.showToastMessage(it) }
            }
        }
    }

    /**
     * View Models observe by [RecipesListActivity]
     */
    override fun observeViewModel() {
        observe(recipesListViewModel.recipesLiveData, ::handleRecipesList)
        observe(recipesListViewModel.recipeSearchFound, ::showSearchResult)
        observe(recipesListViewModel.noSearchFound, ::noSearchResult)
        observeEvent(recipesListViewModel.openRecipeDetails, ::navigateToDetailsScreen)
        observeSnackBarMessages(recipesListViewModel.showSnackBar)
        observeToast(recipesListViewModel.showToast)
    }
}
