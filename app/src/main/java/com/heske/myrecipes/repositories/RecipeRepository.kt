package com.heske.myrecipes.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.heske.myrecipes.AppExecutors
import com.heske.myrecipes.models.Recipe
import com.heske.myrecipes.persistence.RecipeDao
import com.heske.myrecipes.persistence.RecipeDatabase
import com.heske.myrecipes.persistence.RecipeSearchResult
import com.heske.myrecipes.requests.RecipeApi
import com.heske.myrecipes.requests.responses.ApiResponse
import com.heske.myrecipes.requests.responses.RecipeResponse
import com.heske.myrecipes.requests.responses.RecipeSearchResponse
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
    fun searchRecipesApi(recipeId: String): LiveData<Resource<Recipe>> {  //fun searchRecipesApi(recipeId: String): LiveData<Resource<Recipe>> {
        return object : NetworkBoundResource<Recipe, RecipeResponse>(appExecutors) {
            // Called after download completes to insert downloaded
            // data into the database.
            override fun saveCallResult(item: RecipeResponse) {
                // will be null if API key is expired (or request limit reached??)
                if (item.recipe != null) {
                    item.recipe.timestamp = (System.currentTimeMillis() / 1000).toInt()
                    recipeDao.insertRecipe(item.recipe)
                }
            }

            // true = there's no data, should fetch it from the Network
            override fun shouldFetch(data: Recipe?): Boolean {
                val fetchRecipe = data == null
                return fetchRecipe
            }

            override fun loadFromDb() : LiveData<Recipe> {
                val recipe = recipeDao.getRecipe(recipe_id = recipeId)
                return recipe
            }

            // Make the network call to get the data
            override fun createCall() : LiveData<ApiResponse<RecipeResponse>> {
                val response = recipeApi.getRecipe(API_KEY, recipeId)
                return response
            }
        }.asLiveData()
    }

    /**
     * Download a list of recipes
     * The list doesn't include recipe ingredients or timestamp. They get filled
     * in when an individual Recipe is fetched.
     */
    fun searchRecipesApi(query: String, pageNumber: Int): LiveData<Resource<List<Recipe>>> {
        return object : NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(appExecutors) {
            // Called after download completes to insert downloaded
            // data into the database.
            // RecipeSearchResponse is the deserialized Retrofit response
            // RecipeSearchResult is a database table for storing the list
            // of Recipes along with some metadata about the search.
            override fun saveCallResult(item: RecipeSearchResponse) {
                // recipe list will be null if the api key is expired
                if (item.recipes == null)
                    return
                Log.d(TAG, "[saveCallResult] Received ${item.recipes.size} recipes ")
                val recipeIds = item.recipes.map {
                    it.recipe_id
                }

                item.recipes.forEach {
                    // if the recipe already exists... I don't want to set the
                    // ingredients or timestamp b/c
                    // they will be erased
                    val insertRowId = recipeDao.insertRecipe(it)
                    if (insertRowId.equals(-1)) {
                        Log.d(TAG, "saveCallResult $it.title: CONFLICT... This recipe is already in the cache")

                        val rowId = recipeDao.updateRecipe(
                            it.recipe_id,
                            it.title,
                            it.publisher,
                            it.image_url,
                            it.social_rank
                        )
                    }
                }

                val recipeSearchResult = RecipeSearchResult(
                    query = query,
                    recipeIds = recipeIds,
                    total = item.count,
                    nextPage = item.nextPage
                )

                // Save query metadata
                db.runInTransaction {
                    //    val recipeRowIds = recipeDao.insertRecipes(item.recipes)
                    val searchResultsRowId = recipeDao.insertSearchResult(recipeSearchResult)
                    Log.d(TAG, "[saveCallResult] searchRowId: $searchResultsRowId")
                }
            }

            // true = there's no data, should fetch it from the Network
            override fun shouldFetch(data: List<Recipe>?) = true

            override fun loadFromDb(): LiveData<List<Recipe>> {
                val recipes =
                    recipeDao.searchRecipes(
                        query,
                        pageNumber
                    )
                return recipes
            }

            // Make the network call to get the data
            override fun createCall() = recipeApi
                .searchRecipe(
                    API_KEY,
                    query,
                    pageNumber.toString()
                );

        }.asLiveData()
    }

}
