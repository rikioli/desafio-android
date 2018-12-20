package br.com.henriqueoliveira.desafioandroidconcrete.helpers

import android.app.Activity
import android.content.Context
import android.widget.Toast
import android.view.Gravity

fun Activity.showSnack(text: String, length: Int = com.google.android.material.snackbar.Snackbar.LENGTH_SHORT) {
    com.google.android.material.snackbar.Snackbar.make(findViewById(android.R.id.content), text, length).show()
}

fun String.toHtmlColored(hexColor: String): String {

    return "<font color=$hexColor>$this</font>"
}