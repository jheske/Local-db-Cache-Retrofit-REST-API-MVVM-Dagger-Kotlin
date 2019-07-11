package com.heske.myrecipes.ui.categorylist

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.heske.myrecipes.AppExecutors
import com.heske.myrecipes.R
import com.heske.myrecipes.binding.FragmentDataBindingComponent
import com.heske.myrecipes.databinding.FragmentCategoryListBinding
import com.heske.myrecipes.di.Injectable
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

/**
 * This is a simple list of recipe categories. Hardcoded list of categories is
 * in Constants.kt.
 *
 * There is a SearchBar at the top of the screen allowing users
 * to search on any term they want. The search term isn't related to
 * the categories in the list.
 */
class CategoryListFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var appExecutors: AppExecutors

    lateinit var categoryListViewModel: CategoryListViewModel

    var binding by autoCleared<FragmentCategoryListBinding>()
    var adapter by autoCleared<CategoryListAdapter>()

    var dataBindingComponent: DataBindingComponent
            = FragmentDataBindingComponent(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dataBinding
                = DataBindingUtil.inflate<FragmentCategoryListBinding>(
            inflater,
            R.layout.fragment_category_list,
            container,
            false,
            dataBindingComponent
        )
        binding = dataBinding
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryListViewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(CategoryListViewModel::class.java)
        binding.viewModel = categoryListViewModel

        binding.setLifecycleOwner(viewLifecycleOwner)

        // Syntax is kinda convoluted. Pass the onClick handler
        // to adapter as a parameter, which binds it in createBinding().
        val categoryListAdapter = CategoryListAdapter(
            dataBindingComponent,appExecutors
        ) { category ->
            findNavController().navigate(
                CategoryListFragmentDirections
                    .actionCategoryListToRecipeList(category.title)
            )
        }
        binding.categoryListRecyler.adapter = categoryListAdapter
        this.adapter = categoryListAdapter
        //subscribeOvserver()
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}