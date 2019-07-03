package com.heske.myrecipes.ui.recipelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.heske.myrecipes.databinding.LayoutRecipeListItemBinding
import com.heske.myrecipes.models.Recipe

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
class RecipeListAdapter :
    ListAdapter<Recipe, RecipeListAdapter.AdapterViewHolder>(DiffCallback()) {

    /**
     * The FlickrRvAdapterViewHolder constructor takes the binding variable from the associated
     * GridViewItem, which nicely gives it access to the full object information.
     */
    class AdapterViewHolder(private var binding: LayoutRecipeListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            listItemlickListener: View.OnClickListener,
            listItem: Recipe
        ) {
            binding.apply {
                // Binding variables are in listitem_flickr_image.xml.
                clickListener = listItemlickListener
                recipe = listItem
                executePendingBindings()
            }
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            AdapterViewHolder {
        return AdapterViewHolder(
            LayoutRecipeListItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        getItem(position).let { recipe ->
            with(holder) {
                itemView.tag = recipe
                val clickListener = createOnClickListener(recipe)
                bind(clickListener, recipe)
            }
        }
    }

    // Navigate to RecipesListFragment, passing the recipe title, which RecipesList
// will use to query for related recipes.
    private fun createOnClickListener(recipe: Recipe): View.OnClickListener {
        return View.OnClickListener {
         //   it.findNavController().navigate(
          //      RecipeListFragmentDirections.actionRecipeListToRecipe(recipe)
         //   )
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of
     * images has been updated.
     */
    class DiffCallback : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.recipe_id === newItem.recipe_id
        }

        override fun areContentsTheSame(oldItem: Recipe, newItem: Recipe): Boolean {
            return oldItem.title == newItem.title
        }
    }
}


