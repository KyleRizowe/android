package com.fuel4media.carrythistoo.fragments


import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.activity.PaymentActivity
import com.fuel4media.carrythistoo.adapter.PermitAdapter
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.Permit
import com.fuel4media.carrythistoo.requester.DeletePermitRequester
import com.fuel4media.carrythistoo.requester.PermitListRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.DialogUtil
import com.fuel4media.carrythistoo.utils.Utility
import kotlinx.android.synthetic.main.fragment_permits.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.greenrobot.eventbus.Subscribe
import java.util.zip.GZIPOutputStream


/**
 * A simple [Fragment] subclass.
 *
 */
class PermitsFragment : BaseFragment(), PermitAdapter.PermitClickListener {
    override fun onPermitClick(permit: Permit) {
        DialogUtil.showEditDeleteEventDialog(contex!!, object : DialogUtil.AlertDialogInterface.EditDeleteDialogClickListener {
            override fun onEditClick() {
                (activity as DashboaredActivity).replaceFragmentWithTag(AddPermitFragment.newInstance(permit), AddPermitFragment.TAG)
            }

            override fun onDeleteClick() {
                var msg2: String? = null
                val msg = getString(R.string.msg_delete_permit, permit.permit_id)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    msg2 = Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY).toString()
                } else {
                    msg2 = Html.fromHtml(msg).toString()
                }

                DialogUtil.showTwoButtonDialog(context!!, msg2, "Yes", "No", object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                    override fun onPositiveButtonClick() {
                        deletePermitToServer(permit)
                    }

                    override fun onNegativeButtonClick() {

                    }
                })
            }
        })
    }

    private fun deletePermitToServer(permit: Permit) {
        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().execute(DeletePermitRequester(permit.id!!))
    }

    var permits = ArrayList<Permit>()
    var contex: Context? = null
    var adapter: PermitAdapter? = null

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.PERMIT_LIST_SUCCESS -> {
                    permits.clear()

                    if (eventObject.`object` != null) {
                        permits.addAll(eventObject.`object` as ArrayList<Permit>)
                    }


                    adapter!!.notifyDataSetChanged()

                    (activity as DashboaredActivity).getToolbar()!!.iv_plus.visibility = View.VISIBLE

                }
                EventConstant.PERMIT_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }

                EventConstant.DELETE_PERMIT_SUCCESS -> {
                    Utility.showProgressBarSmall(rl_progress_bar)
                    BackgroundExecutor().getInstance().execute(PermitListRequester())
                }

                EventConstant.DELETE_PERMIT_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        })
    }

    override fun updateLocationCallback(lastLocation: Location) {
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.contex = context;
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_permits, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        (activity as DashboaredActivity).getToolbar()!!.iv_plus.setOnClickListener(View.OnClickListener {
            if (permits.size ==0) {
                (activity as DashboaredActivity).replaceFragmentWithTag(AddPermitFragment.newInstance(null), AddPermitFragment.TAG)
            }else{
                if (UserManager.getInstance().isFreeium()) {
                    startActivity(Intent(context, PaymentActivity::class.java))
                } else if (UserManager.getInstance().isOnTrial()) {
                    DialogUtil.showPremiumDailog(context)
                } else {
                    (activity as DashboaredActivity).replaceFragmentWithTag(AddPermitFragment.newInstance(null), AddPermitFragment.TAG)
                }
            }
        })

        (activity as DashboaredActivity).getToolbar()!!.iv_plus.visibility = View.GONE

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().getInstance().execute(PermitListRequester())

        adapter = PermitAdapter(activity as Context, permits, this)
        rv_permits.layoutManager = LinearLayoutManager(context)
        rv_permits.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        if (contex is DashboaredActivity) {
            (contex as DashboaredActivity).setToolbar(getString(R.string.title_permits), false, true)
        }
    }

}
