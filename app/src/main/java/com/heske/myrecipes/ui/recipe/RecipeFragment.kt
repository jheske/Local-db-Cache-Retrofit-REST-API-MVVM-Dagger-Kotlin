package com.heske.myrecipes.ui.recipe

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.heske.myrecipes.AppExecutors
import com.heske.myrecipes.R
import com.heske.myrecipes.databinding.FragmentRecipeBinding
import com.heske.myrecipes.di.Injectable
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

    //private val params by navArgs<UserFragmentArgs>()
    // private var adapter by autoCleared<RepoListAdapter>()
    private var handler = Handler(Looper.getMainLooper())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view =  inflater.inflate(layout.fragment_recipe, container, false)
//        return view
        val binding = DataBindingUtil
            .inflate<FragmentRecipeBinding>(
                inflater,
                R.layout.fragment_recipe,
                container,
                false
            )
//        recipeViewModel = ViewModelProviders.of(this, viewModelFactory)
//            .get(RecipeViewModel::class.java)
//        // binding.args = params
//        binding.viewModel = recipeViewModel
//        binding.setLifecycleOwner(viewLifecycleOwner)

        return binding.root
    }

    /**
     * Created to be able to override in tests
     */
    fun navController() = findNavController()
}