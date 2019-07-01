package com.heske.myrecipes.ui.recipelist

import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.heske.myrecipes.models.Recipe
import com.heske.myrecipes.models.Status
import com.heske.myrecipes.repositories.RecipeRepository
import com.heske.myrecipes.util.AbsentLiveData
import com.heske.myrecipes.util.Resource
import java.util.*
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


//class RecipeListViewModel @Inject constructor(recipeRepository: RecipeRepository) : ViewModel() {
//
//    private val _query = MutableLiveData<String>()
//    private val nextPageHandler = NextPageHandler(recipeRepository)
//
//    val query: LiveData<String> = _query
//
//    val results: LiveData<Resource<List<Recipe>>> = Transformations
//        .switchMap(_query) { search ->
//            if (search.isNullOrBlank()) {
//                AbsentLiveData.create()
//            } else {
//                recipeRepository.search(search, 1)
//            }
//        }
//
//    val loadMoreStatus: LiveData<LoadMoreState>
//        get() = nextPageHandler.loadMoreState
//
//    /**
//     * Called from Fragment to initiate searchForRecipe
//     */
//    fun setQuery(originalInput: String) {
//        val input = originalInput.toLowerCase(Locale.getDefault()).trim()
//        if (input == _query.value) {
//            return
//        }
//        nextPageHandler.reset()
//        _query.value = input
//    }
//
//    /**
//     * Called from Fragment during scrolling to load next page
//     */
//    fun loadNextPage() {
//        _query.value?.let {
//            if (it.isNotBlank()) {
//                nextPageHandler.queryNextPage(it)
//            }
//        }
//    }
//
//    /**
//     * Called from Fragment RetryCallback to run the query again
//     */
//    fun refresh() {
//        _query.value?.let {
//            _query.value = it
//        }
//    }
//
//    class LoadMoreState(val isRunning: Boolean, val errorMessage: String?) {
//        private var handledError = false
//
//        val errorMessageIfNotHandled: String?
//            get() {
//                if (handledError) {
//                    return null
//                }
//                handledError = true
//                return errorMessage
//            }
//    }
//
//    /**
//     * Does all the work to request the next page from the repository.
//     * nextPageLiveData?.observeForever(this) observes until nextPageLiveData returns
//     * a result, which is of type Resource<Boolean>.
//     * We should unregister() the observer when we're done with it.
//     */
//    class NextPageHandler(private val repository: RecipeRepository) : Observer<Resource<Boolean>> {
//        private var nextPageLiveData: LiveData<Resource<Boolean>>? = null
//        val loadMoreState = MutableLiveData<LoadMoreState>()
//        private var query: String? = null
//        private var _hasMore: Boolean = false
//        val hasMore
//            get() = _hasMore
//
//        init {
//            reset()
//        }
//
//        fun queryNextPage(query: String) {
//            if (this.query == query) {
//                return
//            }
//            unregister()
//            this.query = query
//            //nextPageLiveData is a LiveData<Resource<Boolean>>
//            nextPageLiveData = repository.search(query, 1)
//            loadMoreState.value = LoadMoreState(
//                isRunning = true,
//                errorMessage = null
//            )
//            nextPageLiveData?.observeForever(this)
//        }
//
//        override fun onChanged(result: Resource<Boolean>?) {
//            if (result == null) {
//                reset()
//            } else {
//                when (result.status) {
//                    Status.SUCCESS -> {
//                        _hasMore = result.data == true
//                        unregister()
//                        loadMoreState.setValue(
//                            LoadMoreState(
//                                isRunning = false,
//                                errorMessage = null
//                            )
//                        )
//                    }
//                    Status.ERROR -> {
//                        _hasMore = true
//                        unregister()
//                        loadMoreState.setValue(
//                            LoadMoreState(
//                                isRunning = false,
//                                errorMessage = result.message
//                            )
//                        )
//                    }
//                    Status.LOADING -> {
//                        // ignore
//                    }
//                }
//            }
//        }
//
//        private fun unregister() {
//            nextPageLiveData?.removeObserver(this)
//            nextPageLiveData = null
//            if (_hasMore) {
//                query = null
//            }
//        }
//
//        fun reset() {
//            unregister()
//            _hasMore = true
//            loadMoreState.value = LoadMoreState(
//                isRunning = false,
//                errorMessage = null
//            )
//        }
//    }
//}
