package com.heske.myrecipes.ui.recipe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.heske.myrecipes.models.Recipe
import com.heske.myrecipes.repositories.RecipeRepository
import com.heske.myrecipes.util.AbsentLiveData
import com.heske.myrecipes.util.Resource
import javax.inject.Inject

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
class RecipeViewModel @Inject constructor(recipeRepository: RecipeRepository) : ViewModel() {
    // Looks like the Recipe object is wrapped in an object so it
    // can be tested for exists and correctness.
    val _recipeId: MutableLiveData<RecipeId> = MutableLiveData()

    // Observable but not used right now. Will be useful for testing.
    val recipeId: LiveData<RecipeId>
        get() = _recipeId

    // Gotta observe recipe LiveData (in the Fragment), or the switchmap will
    // never be triggered, even when _recipeId.value is set.
    val recipe: LiveData<Resource<Recipe>> = Transformations
        .switchMap(_recipeId) { input ->
            input.ifExists { recipeName ->
                recipeRepository.loadOneRecipe(recipeName)
            }
        }

    // Called from RecipeFragment RetryCallback
    // retry the recipe request.
    fun retry() {
        val recipeId = _recipeId.value?.recipeName
        if (recipeId != null) {
            _recipeId.value = RecipeId(recipeId)
        }
    }

    // Called by RecipeFragment to request a  recipe.
    // Set recipeLiveData to trigger recipeRepository.loadOneRecipe().
    fun setRecipeId(recipeName: String) {
        val update = RecipeId(recipeName)
        if (_recipeId.value == update) {
            return
        }
        _recipeId.value = update
    }

    /**
     * Google probably uses this for testing whether or not
     * the LiveData object exists and is correct.
     */
    data class RecipeId(val recipeName: String) {
        fun <T> ifExists(f: (String) -> LiveData<T>): LiveData<T> {
            return if (recipeName.isBlank()) {
                AbsentLiveData.create()
            } else {
                f(recipeName)
            }
        }
    }
}
