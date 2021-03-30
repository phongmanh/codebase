package com.manhnguyen.codebase.presentation.main

import android.os.Bundle
import androidx.lifecycle.ProcessLifecycleOwner
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.base.ActivityBase
import com.manhnguyen.codebase.presentation.fragments.GoldPriceFragment
import org.koin.android.ext.android.inject


class MainActivity : ActivityBase() {

    @BindView(R.id.mainNavigation)
    lateinit var mainNavigation: BottomNavigationView

    @BindView(R.id.main_toolbar)
    lateinit var mainToolbar: MaterialToolbar


    private val mainViewModel: MainViewModel by inject()

    private var itemSelected: Int = -1
    private var isInit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)


        ProcessLifecycleOwner.get().lifecycle.addObserver(
            ActivityLifecycle(mainViewModel, this)
        )
        replaceFragment(GoldPriceFragment(), GoldPriceFragment::javaClass.name)
    }


    private fun initToolbarTile() {
        try {
            mainNavigation.inflateMenu(R.menu.main_navigation_menu)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun navAction() {
        try {
            val listener =
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                    }
                }

            mainNavigation.setOnNavigationItemSelectedListener(listener)
            mainNavigation.selectedItemId = R.id.main_nav_admin_site_map

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

}




