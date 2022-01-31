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

@HiltViewModel
open class DetailsViewModel @Inject constructor(
    private val dataRepository: DataRepositorySource) : BaseViewModel() {

    private val recipePrivate = MutableLiveData<RecipesItem>()
    val recipeData: LiveData<RecipesItem> get() = recipePrivate

    private val isFavouritePrivate = MutableLiveData<Resource<Boolean>>()
    val isFavourite: LiveData<Resource<Boolean>> get() = isFavouritePrivate

    fun initRecipeData(recipe: RecipesItem) {
        recipePrivate.value = recipe
    }

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
