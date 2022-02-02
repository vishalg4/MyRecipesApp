package com.app.ui.component.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.data.DataRepositorySource
import com.app.data.Resource
import com.app.data.dto.recipes.Recipes
import com.app.data.dto.recipes.RecipesItem
import com.app.ui.base.BaseViewModel
import com.app.utils.SingleEvent
import com.app.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Locale.ROOT
import javax.inject.Inject

/**
 * This ViewModel implements [BaseViewModel], used to retrieve and search recipe list
 * using [dataRepository]
 * Data --> LiveData is exposed as LiveData and locally exposed as MutableLiveData in viewModel.
 *
 * @param [dataRepository] provide access to data source(local & Remote).
 */
@HiltViewModel
class RecipesListViewModel @Inject constructor(
    private val dataRepositoryRepository: DataRepositorySource) : BaseViewModel() {

    /**
     * [recipesLiveData] LiveData is used to expose Recipes data.
     */
    private val recipesLiveDataPrivate = MutableLiveData<Resource<Recipes>>()
    val recipesLiveData: LiveData<Resource<Recipes>> get() = recipesLiveDataPrivate

    /**
     * [recipeSearchFound] LiveData is used to expose searched recipe item [RecipesItem]
     */
    private val recipeSearchFoundPrivate: MutableLiveData<RecipesItem> = MutableLiveData()
    val recipeSearchFound: LiveData<RecipesItem> get() = recipeSearchFoundPrivate

    /**
     * [noSearchFound] LiveData is used to expose when search result not found.
     */
    private val noSearchFoundPrivate: MutableLiveData<Unit> = MutableLiveData()
    val noSearchFound: LiveData<Unit> get() = noSearchFoundPrivate

    /**
     * UI actions as event, user action when selected any recipe from list.
     */
    private val openRecipeDetailsPrivate = MutableLiveData<SingleEvent<RecipesItem>>()
    val openRecipeDetails: LiveData<SingleEvent<RecipesItem>> get() = openRecipeDetailsPrivate

    /**
     * Live data to handle Error in UI
     */
    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar: LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast: LiveData<SingleEvent<Any>> get() = showToastPrivate

    /**
     * Used to retrieve recipes details from data Repository
     */
    fun getRecipes() {
        viewModelScope.launch {
            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestRecipes().collect {
                    recipesLiveDataPrivate.value = it
                }
            }
        }
    }

    fun openRecipeDetails(recipe: RecipesItem) {
        openRecipeDetailsPrivate.value = SingleEvent(recipe)
    }

    fun showToastMessage(errorCode: Int) {
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

    fun onSearchClick(recipeName: String) {
        val searchRecipe = recipesLiveDataPrivate.value?.data?.recipesList?.find {
            it.name.toLowerCase(ROOT).contains(recipeName.toLowerCase(ROOT))
        }

        if (searchRecipe != null) recipeSearchFoundPrivate.value = searchRecipe
        else noSearchFoundPrivate.postValue(Unit)
    }
}
