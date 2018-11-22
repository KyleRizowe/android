package com.fuel4media.carrythistoo.activity

import android.annotation.SuppressLint
import android.content.Context
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.fragments.BaseFragment
import com.fuel4media.carrythistoo.fragments.RegisterFragment
import com.fuel4media.carrythistoo.prefrences.AppPreference
import com.fuel4media.carrythistoo.utils.FragmentFactory
import org.greenrobot.eventbus.Subscribe

class LoginActivity : LocationActivity() {

    private lateinit var currentFragment: BaseFragment

    override fun updateLocationCallback() {
        currentFragment.updateLocationCallback(lastLocation)
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        currentFragment = RegisterFragment()
        FragmentFactory.replaceFragment(currentFragment, R.id.fragment, this)
    }

    fun replaceFragment(fragment: BaseFragment) {
        currentFragment = fragment
        FragmentFactory.replaceFragment(fragment, R.id.fragment, this)
    }


    fun replaceFragmentWithTag(fragment: BaseFragment, tag: String) {
        currentFragment = fragment
        FragmentFactory.replaceFragment(fragment, R.id.fragment, this, tag)
    }
}
