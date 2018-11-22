package com.fuel4media.carrythistoo.utils

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity


/**
 * This Class Handle the Fragment transaction
 */

object FragmentFactory {
    private val TAG = FragmentFactory::class.java.name

    fun replaceFragment(fragment: Fragment, id: Int, context: Context) {
        //LogManager.d(TAG, "on replaceFragment without tag method");
        val fragmentTransaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(id, fragment).commit()
    }

    /*public static void replaceFragmentWithAnim(Fragment fragment, int id, Context context) {
        LogManager.d(TAG, "on replaceFragmentWithAnim without tag method");
        FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(id, fragment).commit();
    }*/

    fun addFragment(fragment: Fragment, id: Int, context: Context, TAG: String) {
        //LogManager.d(TAG, "on addFragment method");
        val fragmentByTag = (context as FragmentActivity).supportFragmentManager.findFragmentByTag(TAG)
        if (fragmentByTag == null) {
            val fragmentTransaction = context.supportFragmentManager.beginTransaction()
            fragmentTransaction.add(id, fragment, TAG).addToBackStack(null).commit()
        }
    }

    fun replaceFragment(fragment: Fragment, id: Int, context: Context, TAG: String) {
        //LogManager.d(TAG, "on replaceFragment method");
        val fragmentByTag = (context as FragmentActivity).supportFragmentManager.findFragmentByTag(TAG)
        if (fragmentByTag == null) {
            val fragmentTransaction = context.supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(id, fragment, TAG).addToBackStack(null).commit()
        }
    }

    /*public static void replaceFragmentWithAnim(Fragment fragment, int id, Context context, String TAG) {
        LogManager.d(TAG, "on replaceFragmentWithAnim method");
        Fragment fragmentByTag = ((FragmentActivity) context).getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragmentByTag == null) {
            FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
            fragmentTransaction.replace(id, fragment, TAG).addToBackStack(null).commit();
        }
    }*/

    fun addFragment(fragment: Fragment, id: Int, context: Context) {
        //LogManager.d(TAG, "on addFragment without tag method");
        val fragmentTransaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
        fragmentTransaction.add(id, fragment).commit()
    }

    fun replaceFragmentBackStack(fragment: Fragment, id: Int, context: Context, TAG: String) {
        //LogManager.d(TAG, "on replaceFragmentBackStack with tag method");
        val fragmentTransaction = (context as FragmentActivity).supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(id, fragment, TAG).addToBackStack(null).commit()
    }

    fun back(context: Context) {
        //LogManager.d(TAG, "on back method");
        val fragmentManager = (context as FragmentActivity).supportFragmentManager
        if (fragmentManager.backStackEntryCount >= 1) {
            fragmentManager.popBackStack()
        } else {
            context.finish()
        }
    }

    fun backWithEmptyStack(context: Context) {
        //LogManager.d(TAG, "on back method");
        val fragmentManager = (context as FragmentActivity).supportFragmentManager
        if (fragmentManager.backStackEntryCount > 1) {
            fragmentManager.popBackStack()
        } else {
            context.finish()
        }
    }

    fun removedBack(context: Context): Boolean {
        //LogManager.d(TAG, "on reomve back method");
        val fragmentManager = (context as FragmentActivity).supportFragmentManager
        if (fragmentManager.backStackEntryCount >= 1) {
            fragmentManager.popBackStack()
            return true
        }
        return false
    }

    fun isFragmentStackIsEmpty(context: Context): Boolean {
        //LogManager.d(TAG, "on isFragmentStackIsEmpty method");
        val fragmentManager = (context as FragmentActivity).supportFragmentManager
        return if (fragmentManager.backStackEntryCount >= 1) {
            false
        } else true
    }
}