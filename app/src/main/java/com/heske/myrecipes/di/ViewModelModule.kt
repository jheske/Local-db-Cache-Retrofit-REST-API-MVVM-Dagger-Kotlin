/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.heske.myrecipes.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.heske.myrecipes.ui.recipe.RecipeViewModel
import com.heske.myrecipes.viewmodels.RecipeViewModelFactory

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(RecipeViewModel::class)
    abstract fun bindRepoViewModel(repoViewModel: RecipeViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: RecipeViewModelFactory): ViewModelProvider.Factory
}
