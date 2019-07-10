package com.heske.myrecipes.ui.recipelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.heske.myrecipes.AppExecutors
import com.heske.myrecipes.R
import com.heske.myrecipes.binding.FragmentDataBindingComponent
import com.heske.myrecipes.databinding.FragmentRecipeListBinding
import com.heske.myrecipes.di.Injectable
import com.heske.myrecipes.util.TAG
import com.heske.myrecipes.util.autoCleared
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
class RecipeListFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var recipeListViewModel: RecipeListViewModel

    // autoCleared is Google's generic memory management class.
    // I don't completely understand when to use it, but it seems to make sense.
    // TODO Perhaps a memory profiler would help to understand this.
    var binding by autoCleared<FragmentRecipeListBinding>()
    var adapter by autoCleared<RecipeListAdapter>()

    /*
     * navArgs() is Google's convenient way to access params passed into the Fragment.
     * Google strongly recommends that the navArgs() method be used only when the
     * Fragment is created by [androidx.navigation.NavController.navigate] with
     * the corresponding [androidx.navigation.NavDirections] object, which ensures
     * that the required arguments are present.
     */
    private val params by navArgs<RecipeListFragmentArgs>()

    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentRecipeListBinding>(
            inflater,
            R.layout.fragment_recipe_list,
            container,
            false,
            dataBindingComponent
        )
        binding = dataBinding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recipeListViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(RecipeListViewModel::class.java)
        binding.viewModel = recipeListViewModel

        binding.args = params

        binding.setLifecycleOwner(viewLifecycleOwner)

        // Syntax is kinda convoluted. Pass the onClick handler
        // to adapter as a parameter, which binds it in createBinding().
        adapter = RecipeListAdapter(
            dataBindingComponent,
            appExecutors
        ) { recipe ->
            findNavController().navigate(
                RecipeListFragmentDirections
                    .actionRecipeListToRecipe(recipe)
            )
        }
        binding.recipeListRecyler.adapter = adapter

        subscribeOvserver()
        recipeListViewModel.runQuery(params.query)
    }

    private fun subscribeOvserver() {
        // Gotta observe recipeList LiveData, or its switchmap in the viewModel will
        // never be triggered, even when  its value changes.
        recipeListViewModel.recipeList.observe(viewLifecycleOwner, Observer { listResource ->
            // we don't need any null checks here for the adapter since LiveData guarantees that
            // it won't call us if fragment is stopped or not started.
            Log.d(TAG, "New recipe list!!")
            //   if (listResource?.data != null) {
            adapter.submitList(listResource?.data)
            // } else {
            //     adapter.submitList(emptyList())
            // }
        })
    }
}