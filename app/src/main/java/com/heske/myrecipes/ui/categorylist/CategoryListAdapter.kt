package com.heske.myrecipes.ui.categorylist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.heske.myrecipes.AppExecutors
import com.heske.myrecipes.R
import com.heske.myrecipes.databinding.LayoutCategoryListItemBinding
import com.heske.myrecipes.models.Category
import com.heske.myrecipes.ui.common.DataBoundListAdapter

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
 * A RecyclerView adapter for [Repo] class.
 */
class CategoryListAdapter(
    private val dataBindingComponent: DataBindingComponent,
    private val appExecutors: AppExecutors,
    private val categoryClickCallback: ((Category) -> Unit)?
) :
    DataBoundListAdapter<Category, LayoutCategoryListItemBinding>(
        appExecutors = appExecutors,
        diffCallback = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.title == newItem.title
            }
        }
    ) {
    override fun createBinding(parent: ViewGroup): LayoutCategoryListItemBinding {
        val binding = DataBindingUtil.inflate<LayoutCategoryListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.layout_category_list_item,
            parent,
            false,
            dataBindingComponent
        )
        // Invoke the function defined in CategoryListFragment and passed
        // into the adapter as a param.
        binding.root.setOnClickListener {
            binding.category?.let {
                categoryClickCallback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: LayoutCategoryListItemBinding, item: Category) {
        binding.category = item
    }
}
