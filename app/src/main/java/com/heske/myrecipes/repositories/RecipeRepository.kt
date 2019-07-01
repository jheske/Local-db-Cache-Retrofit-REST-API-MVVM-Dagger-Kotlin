package com.heske.myrecipes.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.heske.myrecipes.AppExecutors
import com.heske.myrecipes.models.Recipe
import com.heske.myrecipes.persistence.RecipeDao
import com.heske.myrecipes.persistence.RecipeDatabase
import com.heske.myrecipes.requests.RecipeApi
import com.heske.myrecipes.requests.responses.*
import com.heske.myrecipes.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/* Copyright (c) 2019 Jill Heske All rights reserved.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
@Singleton
class RecipeRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val db: RecipeDatabase,
    private val recipeDao: RecipeDao,
    private val recipeApi: RecipeApi
) {
    private val repoListRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    /**
     * Download one recipe.
     */
    fun loadOneRecipe(recipeId: String): LiveData<Resource<Recipe>> {
        return object : NetworkBoundResource<Recipe, RecipeResponse>(appExecutors) {
                    override fun saveCallResult(item: RecipeResponse) {
                // will be null if API key is expired
                if (item.recipe != null) {
                    // item.recipe.setTimestamp((System.currentTimeMillis() / 1000).toInt())
                    item.recipe.timestamp = (System.currentTimeMillis() / 1000).toInt()
                    recipeDao.insertRecipe(item.recipe)
                }
            }

            override fun shouldFetch(data: Recipe?): Boolean {
                if (data == null) {
                    return false
                }
                Log.d(TAG, "shouldFetch: recipe: $data")
                val currentTime = (System.currentTimeMillis() / 1000).toInt()
                Log.d(TAG, "shouldFetch: current time: $currentTime")

                val lastRefresh = data.timestamp
                Log.d(TAG, "shouldFetch: last refresh: $lastRefresh")

                Log.d(
                    TAG, "shouldFetch: it's been " + (currentTime - lastRefresh) / 60 / 60 / 24 +
                            " days since this recipe was refreshed. 30 days must elapse before refreshing. "
                )
                if (currentTime - data.timestamp >= RECIPE_REFRESH_TIME) {
                    Log.d(TAG, "shouldFetch: SHOULD REFRESH RECIPE?! " + true)
                    return true
                }
                Log.d(TAG, "shouldFetch: SHOULD REFRESH RECIPE?! " + false)
                return false
            }

            override fun loadFromDb(): LiveData<Recipe> {
                return recipeDao.getRecipe(recipeId)
            }

            override fun createCall(): LiveData<ApiResponse<RecipeResponse>> {
                return recipeApi.getRecipe(
                    API_KEY,
                    recipeId
                )
            }
        }.asLiveData()
    }

    /**
     * Search for an download one page/List of Recipes.
     */
    fun search (query: String, pageNumber: Int): LiveData<Resource<List<Recipe>>> {
        return object : NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(appExecutors) {

            override fun saveCallResult(item: RecipeSearchResponse) {

                // Bundle the results for batch database insert
                val repoSearchResult = RecipeSearchResponse(
                    count = item.count,
                    recipes = item.recipes,
                    error = item.error
                )

                // TODO replace "!!" with null safety
                // Insert them all in one transaction
                db.runInTransaction {
                    recipeDao.insertRecipes(item.recipes!!)
                }
            }

            override fun shouldFetch(data: List<Recipe>?) = data == null

            override fun loadFromDb(): LiveData<List<Recipe>> =
                recipeDao.searchRecipes(query, pageNumber)

            override fun createCall() = recipeApi.searchRecipe(API_KEY, query, pageNumber.toString())

            override fun processResponse(response: ApiSuccessResponse<RecipeSearchResponse>)
                    : RecipeSearchResponse {
                val body = response.body
                body.nextPage = response.nextPage
                return body
            }
        }.asLiveData()
    }

    /**
     * RecipeListViewModel calls search() to download the latest page of recipes.
     * search() and searchNextPage() are where the real searching gets done!
     * provide paged searching and downloading of results
     */
//     fun searchNextPage(query: String): LiveData<Resource<Boolean>> {
//        val fetchNextSearchPageTask = FetchNextSearchPageTask(
//            query = query,
//            recipeApi = recipeApi,
//            db = db
//        )
//        appExecutors.networkIO().execute(fetchNextSearchPageTask)
//        return fetchNextSearchPageTask.liveData
//    }

//    fun searchForRecipe(query: String): LiveData<Resource<List<Recipe>>> {
//        return object : NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(appExecutors) {
//
//            override fun saveCallResult(item: RecipeSearchResponse) {
//                val repoIds = item.items.map { it.id }
//                val repoSearchResult = RepoSearchResult(
//                    query = query,
//                    repoIds = repoIds,
//                    totalCount = item.total,
//                    next = item.nextPage
//                )
//                db.runInTransaction {
//                    recipeDao.insertRepos(item.items)
//                    recipeDao.insert(repoSearchResult)
//                }
//            }
//
//            override fun shouldFetch(data: List<Recipe>?) = data == null
//
//            override fun loadFromDb(): LiveData<List<Recipe>> {
//                return Transformations.switchMap(recipeDao.searchForRecipe(query)) { searchData ->
//                    if (searchData == null) {
//                        AbsentLiveData.create()
//                    } else {
//                        recipeDao.loadOrdered(searchData.repoIds)
//                    }
//                }
//            }
//
//            override fun createCall() = githubService.searchRepos(query)
//
//            override fun processResponse(response: ApiSuccessResponse<RepoSearchResponse>)
//                    : RepoSearchResponse {
//                val body = response.body
//                body.nextPage = response.nextPage
//                return body
//            }
//        }.asLiveData()
//    }

}
