package com.app.data

import com.app.data.dto.recipes.Recipes
import com.app.data.local.LocalData
import com.app.data.remote.RemoteData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * DataRepository class implements DataRepositorySource which is use to retrieve data
 * from local [LocalData] and Remote [RemoteData] data source.
 */
class DataRepository @Inject constructor(
    private val remoteData: RemoteData,
    private val localData: LocalData,
    private val ioDispatcher: CoroutineContext
) : DataRepositorySource {
    /**
     * Suspended function use to request recipes details from Remote data source.
     * @return [Flow] of Resource<Recipes>
     */
    override suspend fun requestRecipes(): Flow<Resource<Recipes>> {
        return flow {
            emit(remoteData.requestRecipes())
        }.flowOn(ioDispatcher)
    }

    /**
     * Suspended function use to add recipe in locally saved favourites.
     * @param [id] given recipe ID.
     * @return [Flow] of Resource<Boolean> define Success/Failure status
     * using boolean(true/false).
     */
    override suspend fun addToFavourite(id: String): Flow<Resource<Boolean>> {
        return flow {
            localData.getCachedFavourites().let {
                it.data?.toMutableSet()?.let { set ->
                    val isAdded = set.add(id)
                    if (isAdded) {
                        emit(localData.cacheFavourites(set))
                    } else {
                        emit(Resource.Success(false))
                    }
                }
                it.errorCode?.let { errorCode ->
                    emit(Resource.DataError<Boolean>(errorCode))
                }
            }
        }.flowOn(ioDispatcher)
    }

    /**
     * Suspended function removes recipe in locally saved favourites.
     * @param [id] given recipe ID.
     * @return [Flow] of Resource<Boolean> define Success/Failure
     * status using boolean(true/false).
     */
    override suspend fun removeFromFavourite(id: String): Flow<Resource<Boolean>> {
        return flow {
            emit(localData.removeFromFavourites(id))
        }.flowOn(ioDispatcher)
    }

    /**
     * Suspended function tell if given recipe is added to favourites or not.
     * @param [id] given recipe ID.
     * @return [Flow] of Resource<Boolean> define Success/Failure
     * status using boolean(true/false).
     */
    override suspend fun isFavourite(id: String): Flow<Resource<Boolean>> {
        return flow {
            emit(localData.isFavourite(id))
        }.flowOn(ioDispatcher)
    }
}
