package com.heske.myrecipes.binding

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.heske.myrecipes.R
import javax.inject.Inject

/**
 * Shared interface for handling Glide image display.
 */
//Recipe images are String addresses that point to images on food2fork.com
class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {
    @BindingAdapter(
        value = ["recipeImageUrl"],
        requireAll = false
    )
    fun bindImage(
        imageView: ImageView,
        url: String?
    ) {
        Glide.with(fragment).load(url).into(imageView)
    }

    @BindingAdapter(
        value = ["categoryImageUri"],
        requireAll = false
    ) fun bindImageUri(imgView: ImageView,
                       imageUri: android.net.Uri?) {
        imageUri?.let {
            Glide.with(imgView.context)
                .load(imageUri)
                .apply(
                    RequestOptions()
                        .error(R.drawable.barbeque)
                )
                .into(imgView)
        }
    }
}