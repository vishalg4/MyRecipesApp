package com.app.ui.component.details

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.app.data.DataRepositorySource
import com.app.data.Resource
import com.app.data.dto.recipes.RecipesItem
import com.app.ui.base.BaseViewModel
import com.app.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * This class implements [BaseViewModel], used to retrieve, add or delete recipes from Local/Remote
 * data source using [dataRepository]
 * Data --> LiveData is exposed as LiveData and locally exposed as MutableLiveData in viewModel.
 *
 * @param [dataRepository] provide access to data source(local & Remote).
 */
@HiltViewModel
open class DetailsViewModel @Inject constructor(
    private val dataRepository: DataRepositorySource
) : BaseViewModel() {
    /**
     * [recipeData] LiveData is used to expose selected recipe details.
     */
    private val recipePrivate = MutableLiveData<RecipesItem>()
    val recipeData: LiveData<RecipesItem> get() = recipePrivate

    /**
     * [isFavourite] LiveData is used to expose selected recipe details.
     */
    private val isFavouritePrivate = MutableLiveData<Resource<Boolean>>()
    val isFavourite: LiveData<Resource<Boolean>> get() = isFavouritePrivate

    /**
     * Initializing LiveData with provided [RecipesItem].
     * @param [recipe] represent recipe item contains recipe details.
     */
    fun initRecipeData(recipe: RecipesItem) {
        recipePrivate.value = recipe
    }

    /**
     * Added given Recipe Item to favourites and update [isFavouritePrivate] LiveData.
     */
    open fun addToFavourites() {
        viewModelScope.launch {
            isFavouritePrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                recipePrivate.value?.id?.let {
                    dataRepository.addToFavourite(it).collect { isAdded ->
                        isFavouritePrivate.value = isAdded
                    }
                }
            }
        }
    }

    /**
     * Remove given Recipe Item from favourites and update [isFavouritePrivate] LiveData.
     */
    fun removeFromFavourites() {
        viewModelScope.launch {
            isFavouritePrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                recipePrivate.value?.id?.let {
                    dataRepository.removeFromFavourite(it).collect { isRemoved ->
                        isRemoved.data?.let {
                            isFavouritePrivate.value = Resource.Success(!isRemoved.data)
                        }
                    }
                }
            }
        }
    }

    /**
     * Check given Recipe Item is favourites and update [isFavouritePrivate] LiveData.
     */
    fun isFavourites() {
        viewModelScope.launch {
            isFavouritePrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                recipePrivate.value?.id?.let {
                    dataRepository.isFavourite(it).collect { isFavourites ->
                        isFavouritePrivate.value = isFavourites
                    }
                }
            }
        }
    }
}
