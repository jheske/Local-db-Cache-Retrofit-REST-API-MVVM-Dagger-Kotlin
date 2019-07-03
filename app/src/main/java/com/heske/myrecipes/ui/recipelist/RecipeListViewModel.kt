package com.heske.myrecipes.ui.recipelist

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
/*
* Copyright (C) 2017 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
class RecipeListViewModel @Inject constructor(recipeRepository: RecipeRepository) : ViewModel() {

    // The query is wrapped in a QueryWrapper object so the LiveData can be tested
    // for exists and correctness.
    val _query: MutableLiveData<QueryWrapper> = MutableLiveData()
    val query: LiveData<QueryWrapper>
        get() = _query

    // Must add at least one observer on a LiveData (usually in the Fragment),
    // or the switchmap will never  trigger, not even when _query.value is set.
    val recipeList: LiveData<Resource<List<Recipe>>> = Transformations
        .switchMap(_query) { input ->
            input.ifExists { queryString ->
                recipeRepository.searchRecipesApi(queryString,1)
            }
        }

    // Called by RecipeFragment to request a  recipe.
    // Set recipeLiveData to trigger recipeRepository.searchRecipesApi().
    fun runQuery(query: String) {
        val update = QueryWrapper(query)
        if (_query.value == update) {
            return
        }
        _query.value = update
    }

    /**
     * Google probably uses this for testing whether or not
     * the LiveData object exists and is correct.
     */
    data class QueryWrapper(val query: String) {
        fun <T> ifExists(f: (String) -> LiveData<T>): LiveData<T> {
            return if (query.isBlank()) {
                AbsentLiveData.create()
            } else {
                f(query)
            }
        }
    }
}

