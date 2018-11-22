package com.fuel4media.carrythistoo.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.activity.PaymentActivity
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.Contact
import com.fuel4media.carrythistoo.model.request.InviteOwners
import com.fuel4media.carrythistoo.requester.InviteGunOwnersRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.DialogUtil
import com.fuel4media.carrythistoo.utils.Utility
import kotlinx.android.synthetic.main.fragment_invite_gun_owner.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 */
class InviteGunOwnerFragment : BaseFragment() {

    var contex: Context? = null
    var mobile_numbers = ArrayList<String>()
    var selectedContact = ArrayList<Contact>()

    override fun updateLocationCallback(lastLocation: Location) {
        // Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()
    }


    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.INVITE_OWNERS_SUCCESS -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                    (activity as DashboaredActivity).replaceFragment(HomeFragment())
                }
                EventConstant.INVITE_OWNERS_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.contex = context
        if (context is DashboaredActivity) {
            context.setToolbar(getString(R.string.title_invite_owners), false, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invite_gun_owner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_mobile_no.setOnClickListener(View.OnClickListener {
            if (activity is DashboaredActivity) {
                val fragment = ContactFragment.newInstance(selectedContact)
                fragment.setTargetFragment(this@InviteGunOwnerFragment, 101)
                (activity as DashboaredActivity).replaceFragmentWithTag(fragment, "ContactFragment")
            }
        })
        btn_invite.setOnClickListener(View.OnClickListener {
            if (!TextUtils.isEmpty(tv_mobile_no.text.toString().trim())) {
                Utility.showProgressBarSmall(rl_progress_bar)
                BackgroundExecutor().getInstance().execute(InviteGunOwnersRequester(InviteOwners(mobile_numbers)))
            } else {
                CommonMethods.showShortToast(context!!, "Please enter mobile number")
            }
        })

    }

    override fun onResume() {
        super.onResume()
        if (contex is DashboaredActivity) {
            (contex as DashboaredActivity).setToolbar(getString(R.string.title_invite_owners), false, false)
        }

        if (selectedContact.size > 0) {
            tv_mobile_no.text = selectedContact.size.toString().plus(" mobile number selected")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === 101 && resultCode === Activity.RESULT_OK) {
            Log.d("Invite", "get List")
            selectedContact = data!!.getParcelableArrayListExtra<Contact>("mobile_numbers")

            selectedContact.forEach {
                mobile_numbers.add(it.mobile_number!!)
            }
        }
    }

}// Required empty public constructor
