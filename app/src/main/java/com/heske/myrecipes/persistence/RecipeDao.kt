package com.heske.myrecipes.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

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
@Dao
abstract class RecipeDao {

    @Insert(onConflict = IGNORE)
   // abstract fun insertRecipes(vararg recipe: Recipe): LongArray
    abstract fun insertRecipes(recipes: List<Recipe>): LongArray

    @Insert(onConflict = REPLACE)
    abstract fun insertRecipe(recipe: Recipe)

    @Query("UPDATE recipes SET title = :title, publisher = :publisher, image_url = :image_url, social_rank = :social_rank " + "WHERE recipe_id = :recipe_id")
    abstract fun updateRecipe(recipe_id: String, title: String, publisher: String, image_url: String, social_rank: Float)

    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%' OR ingredients LIKE '%' || :query || '%' " + "ORDER BY social_rank DESC LIMIT (:pageNumber * 30)")
    abstract fun searchRecipes(query: String, pageNumber: Int): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipes WHERE recipe_id = :recipe_id")
    abstract fun getRecipe(recipe_id: String): LiveData<Recipe>
}









