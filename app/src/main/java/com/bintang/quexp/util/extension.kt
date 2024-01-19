package com.bintang.quexp.util

import android.content.Context
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.bintang.quexp.R

fun htmlStringFormat(context: Context, text1: String, text2: String): Spanned {
    return HtmlCompat.fromHtml(
        String.format(
            context.resources.getString(R.string.btn_login_register),
            text1,
            text2
        ), HtmlCompat.FROM_HTML_MODE_LEGACY
    )
}