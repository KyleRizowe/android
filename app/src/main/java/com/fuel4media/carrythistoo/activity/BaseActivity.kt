package com.fuel4media.carrythistoo.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ProgressBar
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.manager.LogManager
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.utils.DialogUtil
import com.fuel4media.carrythistoo.utils.FragmentFactory
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

abstract class BaseActivity : AppCompatActivity() {

    private val TAG = BaseActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Utility.fullScreenSize(BaseActivity.this);
    }

    public override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    public override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    @Subscribe
    abstract fun onEvent(eventObject: EventObject)


    fun onHandleBaseEvent(eventObject: EventObject?) {
        if (eventObject == null) {
            LogManager.d(TAG, "on event method called but event object is null")
            return
        }
        when (eventObject!!.getId()) {
            EventConstant.NETWORK_ERROR -> DialogUtil.showOneButtonDialog(this, getString(R.string.no_internet_connection), getString(R.string.ok))
            EventConstant.SERVER_ERROR -> DialogUtil.showOneButtonDialog(this, getString(R.string.msg_server_error), getString(R.string.ok))
            EventConstant.TOKEN_EXPIRE -> DialogUtil.showOneButtonDialog(this, getString(R.string.msg_token_expire), getString(R.string.ok), object : DialogUtil.AlertDialogInterface.OneButtonDialogClickListener {
                override fun onButtonClick() {
                    UserManager.getInstance().logout()
                    openLoginActivity()
                    finish()
                }
            })
        }
    }

    private fun openLoginActivity() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    fun changeFragmentWithTag(fragment: Fragment, tag: String) {
        FragmentFactory.replaceFragment(fragment, R.id.container, this, tag)
    }

    fun changeFragment(fragment: Fragment) {
        FragmentFactory.replaceFragment(fragment, R.id.container, this)
    }

    fun addFragment(fragment: Fragment, tag: String) {
        FragmentFactory.addFragment(fragment, R.id.container, this, tag)
    }

    fun showProgressBar(progressBar: ProgressBar?) {
        if (progressBar != null) {
            progressBar.visibility = View.VISIBLE
        }
    }

    fun hideProgressBar(progressBar: ProgressBar?) {
        if (progressBar != null) {
            progressBar.visibility = View.GONE
        }
    }
}
