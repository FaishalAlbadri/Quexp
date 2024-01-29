package com.bintang.quexp.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.os.Bundle
import android.os.SystemClock
import android.text.Editable
import android.text.Spanned
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.annotation.IdRes
import androidx.appcompat.app.AlertDialog
import androidx.core.text.HtmlCompat
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.ui.NavigationUI
import com.bintang.quexp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.ref.WeakReference
import java.util.Timer
import java.util.TimerTask

fun htmlStringFormat(context: Context, text1: String, text2: String): Spanned {
    return HtmlCompat.fromHtml(
        String.format(
            context.resources.getString(R.string.btn_login_register),
            text1,
            text2
        ), HtmlCompat.FROM_HTML_MODE_LEGACY
    )
}

fun BottomNavigationView.setupWithNavController(navController: NavController) {
    NavigationUI.setupWithNavController(this, navController)
}

fun NavigationUI.setupWithNavController(
    navigationSlideView: BottomNavigationView,
    navController: NavController
) {
    navigationSlideView.setOnItemSelectedListener { item ->
        onNavDestinationSelected(
            item,
            navController
        )
        navController.popBackStack(item.itemId, inclusive = false)
        true
    }
    navigationSlideView.setOnItemReselectedListener { item ->
        navController.popBackStack(item.itemId, inclusive = true)
        navController.navigate(item.itemId)
        true
    }
    val weakReference = WeakReference(navigationSlideView)
    navController.addOnDestinationChangedListener(
        object : NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                val view = weakReference.get()
                if (view == null) {
                    navController.removeOnDestinationChangedListener(this)
                    return
                }
                view.menu.forEach { item ->
                    if (destination.matchDestination(item.itemId)) {
                        item.isChecked = true
                    }
                }
            }
        })
}

fun NavDestination.matchDestination(@IdRes destId: Int): Boolean =
    hierarchy.any { it.id == destId }

fun createAlertDialog(context: Context): AlertDialog {
    return AlertDialog.Builder(context, R.style.CustomAlertDialog)
        .setCancelable(false)
        .setView(R.layout.loading)
        .create()
}

fun Bitmap.createBitmapWithBorder(borderSize: Float, borderColor: Int): Bitmap {
    val borderOffset = (borderSize * 2).toInt()
    val halfWidth = width / 2
    val halfHeight = height / 2
    val circleRadius = Math.min(halfWidth, halfHeight).toFloat()
    val newBitmap = Bitmap.createBitmap(
        width + borderOffset,
        height + borderOffset,
        Bitmap.Config.ARGB_8888
    )

    val centerX = halfWidth + borderSize
    val centerY = halfHeight + borderSize

    val paint = Paint()
    val canvas = Canvas(newBitmap).apply {
        drawARGB(0, 0, 0, 0)
    }

    paint.isAntiAlias = true
    paint.style = Paint.Style.FILL
    canvas.drawCircle(centerX, centerY, circleRadius, paint)

    paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    canvas.drawBitmap(this, borderSize, borderSize, paint)

    paint.xfermode = null
    paint.style = Paint.Style.STROKE
    paint.color = borderColor
    paint.strokeWidth = borderSize
    canvas.drawCircle(centerX, centerY, circleRadius, paint)
    return newBitmap
}

fun EditText.onTextChangedListener(debounceTime: Long = 500L, action: () -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        private var timer = Timer()
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            timer.cancel()
            timer = Timer()
            timer.schedule(object : TimerTask() {
                override fun run() {
                    action()
                }
            }, debounceTime)
        }
    })
}
