package com.bintang.quexp.util

import android.content.Context
import android.os.Bundle
import android.text.Spanned
import androidx.annotation.IdRes
import androidx.core.text.HtmlCompat
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.ui.NavigationUI
import com.bintang.quexp.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.ref.WeakReference

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