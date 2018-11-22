package com.fuel4media.carrythistoo.utils

import android.support.v4.app.Fragment
import com.fuel4media.carrythistoo.fragments.BaseFragment


object ChildFragmentFactory {
    fun replaceFragment(fragment: Fragment, id: Int, context: BaseFragment) {
        val fragmentTransaction = context.childFragmentManager.beginTransaction()
        fragmentTransaction.replace(id, fragment).commit()
    }

    fun addFragment(fragment: Fragment, id: Int, context: BaseFragment, TAG: String) {
        val fragmentByTag = context.childFragmentManager.findFragmentByTag(TAG)
        if (fragmentByTag == null) {
            val fragmentTransaction = context.childFragmentManager.beginTransaction()
            fragmentTransaction.add(id, fragment).addToBackStack(TAG).commit()
        }
    }

    fun addFragment(fragment: Fragment, id: Int, context: BaseFragment) {
        val fragmentTransaction = context.childFragmentManager.beginTransaction()
        fragmentTransaction.add(id, fragment).commit()
    }

    fun replaceFragment(fragment: Fragment, id: Int, context: BaseFragment, TAG: String) {
        val fragmentByTag = context.childFragmentManager.findFragmentByTag(TAG)
        if (fragmentByTag == null) {
            val fragmentTransaction = context.childFragmentManager.beginTransaction()
            fragmentTransaction.replace(id, fragment).addToBackStack(TAG).commit()
        }
    }

    fun back(context: BaseFragment) {
        val fragmentManager = context.childFragmentManager
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStack()
        }
    }
}
