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

package com.heske.myrecipes.persistence

import androidx.room.Entity
import androidx.room.TypeConverters

/**
 * This table is keeps track of the user's current query,
 * all the recipes that have been downloaded so far,
 * and the next page to download when user scrolls.
 *
 * NOTE, this table is related to the Recipe table. Its
 * db methods are in RecipeDao.
 */
@Entity(primaryKeys = ["query"])
@TypeConverters(Converters::class)
data class RecipeSearchResult(
    // User's original query
    val query: String,
    // A list of all the recipes that have been downloaded
    val recipeIds: List<String>,
    // Total number of recipes included in the most recent download.
    // It's from googlesamples and I'm not sure why they need it.
    val total: Int,
    val nextPage: Int?
)
