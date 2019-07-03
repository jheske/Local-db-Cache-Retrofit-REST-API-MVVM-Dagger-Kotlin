package com.heske.myrecipes.ui.recipe

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.heske.myrecipes.AppExecutors
import com.heske.myrecipes.R
import com.heske.myrecipes.databinding.FragmentRecipeBinding
import com.heske.myrecipes.di.Injectable
import com.heske.myrecipes.util.TAG
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
class RecipeFragment() : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var recipeViewModel: RecipeViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var binding: FragmentRecipeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(
                inflater,
                R.layout.fragment_recipe,
                container,
                false
            )
        recipeViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(RecipeViewModel::class.java)
        binding.setLifecycleOwner(viewLifecycleOwner)
        binding.recipe = recipeViewModel.recipe
        // Gotta observe the LiveData, or its switchmap in the viewModel will
        // never be triggered, even when its value changes.
        recipeViewModel.recipe.observe(viewLifecycleOwner, Observer {
            Log.d(TAG,"recipeId value has changed!!")
        })
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recipeViewModel.setRecipeId(getString(R.string.txt_test_recipe_id))
    }

    /**
     * Created to be able to override in tests
     */
    fun navController() = findNavController()
}