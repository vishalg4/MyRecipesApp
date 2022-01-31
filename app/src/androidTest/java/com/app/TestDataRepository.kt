package com.app

import com.app.TestUtil.dataStatus
import com.app.TestUtil.initData
import com.app.data.DataRepositorySource
import com.app.data.Resource
import com.app.data.dto.recipes.Recipes
import com.app.data.error.NETWORK_ERROR
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TestDataRepository @Inject constructor() : DataRepositorySource {

    override suspend fun requestRecipes(): Flow<Resource<Recipes>> {
        return when (dataStatus) {
            DataStatus.Success -> {
                flow { emit(Resource.Success(initData())) }
            }
            DataStatus.Fail -> {
                flow { emit(Resource.DataError<Recipes>(errorCode = NETWORK_ERROR)) }
            }
            DataStatus.EmptyResponse -> {
                flow { emit(Resource.Success(Recipes(arrayListOf()))) }
            }
        }
    }

    override suspend fun addToFavourite(id: String): Flow<Resource<Boolean>> {
        return flow { emit(Resource.Success(true)) }
    }

    override suspend fun removeFromFavourite(id: String): Flow<Resource<Boolean>> {
        return flow { emit(Resource.Success(true)) }
    }

    override suspend fun isFavourite(id: String): Flow<Resource<Boolean>> {
        return flow { emit(Resource.Success(true)) }
    }
}
