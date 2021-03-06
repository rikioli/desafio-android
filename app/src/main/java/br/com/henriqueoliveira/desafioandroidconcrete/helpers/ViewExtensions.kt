package br.com.henriqueoliveira.desafioandroidconcrete.helpers

import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

fun View.show(show: Boolean = true) {
    visibility = if (show) View.VISIBLE else View.GONE
}

fun View.invisible(invisible: Boolean = true) {
    visibility = if (!invisible) View.VISIBLE else View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun ImageView.loadUrl(url: String?, @DrawableRes placeholder: Int? = null) {
    if (placeholder == null) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
        return
    }
    Glide.with(context)
            .load(url)
            .into(this)
}