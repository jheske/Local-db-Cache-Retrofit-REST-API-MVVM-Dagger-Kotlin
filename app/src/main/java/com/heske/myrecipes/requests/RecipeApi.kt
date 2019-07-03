package com.heske.myrecipes.requests

import androidx.lifecycle.LiveData
import com.heske.myrecipes.requests.responses.ApiResponse
import com.heske.myrecipes.requests.responses.RecipeResponse
import com.heske.myrecipes.requests.responses.RecipeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

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
interface RecipeApi {
    //https://www.food2fork.com/api/search?key=dadc63b6325aaf398163b40fea9b5e79&q=chocolate
    @GET("api/search")
    fun searchRecipe(
        @Query("key") key: String,
        @Query("q") query: String,
        @Query("page") page: String
    ): LiveData<ApiResponse<RecipeSearchResponse>>

    @GET("api/get")
    fun getRecipe(
        @Query("key") key: String,
        @Query("rId") recipe_id: String
    ): LiveData<ApiResponse<RecipeResponse>>
}