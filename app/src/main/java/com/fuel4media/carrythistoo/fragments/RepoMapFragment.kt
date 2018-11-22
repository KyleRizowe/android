package com.fuel4media.carrythistoo.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.activity.PaymentActivity
import com.fuel4media.carrythistoo.adapter.SelectPermitAdapter
import com.fuel4media.carrythistoo.adapter.SelectStateAdapter
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.DialogFragmentCallback
import com.fuel4media.carrythistoo.model.PermitType
import com.fuel4media.carrythistoo.model.State
import com.fuel4media.carrythistoo.model.request.RepoMapRequest
import com.fuel4media.carrythistoo.requester.AddSuggestionRequester
import com.fuel4media.carrythistoo.requester.GetRepoMapRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.DialogUtil
import com.fuel4media.carrythistoo.utils.Utility
import kotlinx.android.synthetic.main.fragment_repo_map.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import org.greenrobot.eventbus.Subscribe

class RepoMapFragment : BaseFragment(), SelectStateAdapter.OnClickListener, SelectPermitAdapter.OnClickListener, DialogFragmentCallback {
    override fun updateLocationCallback(lastLocation: Location) {

    }

    override fun onOkClick(selectedList: ArrayList<State>) {
        selectedStateList.clear()
        selectedStateList.addAll(selectedList)
        //stateAdapter!!.notifyDataSetChanged()
    }

    override fun onCheckBoxClick(value: Boolean, contact: PermitType) {
        if (value) {
            selectedPermitList.add(contact)
        } else {
            selectedPermitList.remove(contact)
        }
    }

    override fun onStateClick(state: State) {
        selectedStateList.remove(state)
        stateAdapter!!.notifyDataSetChanged()
    }


    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.ADD_SUGGESTION_SUCCESS -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
                EventConstant.ADD_SUGGESTION_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
                EventConstant.GET_REPO_MAP_SUCCESS -> {
                    // CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                    iv_repo_map.loadUrl(eventObject.`object` as String)
                }
                EventConstant.GET_REPO_MAP_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        });
    }


    var toolbar1: Toolbar? = null
    var DIALOG_FRAGMENT = 123

    var stateAdapter: SelectStateAdapter? = null
    var permitAdapter: SelectPermitAdapter? = null

    var selectedStateList = ArrayList<State>()
    var selectedPermitList = ArrayList<PermitType>()


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboaredActivity) {
            context.setToolbar(getString(R.string.title_repo_map), false, false)
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("Home", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d("Home", "onPause")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_repo_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as DashboaredActivity).createLocationRequest()

        tv_register_location.text = UserManager.getInstance().getState(UserManager.getInstance().user.state)

        tv_select_state.text = UserManager.getInstance().getState(UserManager.getInstance().user.state)

        //setStateAdapter()

        //setPermitAdapter()

        selectedStateList.add(UserManager.getInstance().getStateItem(UserManager.getInstance().user.state))

        onRefreshMapClick()

        ll_select_state.setOnClickListener(View.OnClickListener {
            if (UserManager.getInstance().isFreeium()) {
                startActivity(Intent(context, PaymentActivity::class.java))
            } else if (UserManager.getInstance().isOnTrial()) {
                DialogUtil.showPremiumDailog(context)
            } else {
                //Need to use in app version 2
                /* val dialogFrag = StateListFragment.newInstance(selectedStateList)
                 // dialogFrag.setTargetFragment(this@HomeFragment, DIALOG_FRAGMENT)
                 dialogFrag.show(fragmentManager!!.beginTransaction(), "dialog")
                 dialogFrag.setCallback(this)*/

                DialogUtil.showStateListing(
                        context!!,
                        UserManager.getInstance().states!!,
                        object : DialogUtil.AlertDialogInterface.StateDialogClickListener {
                            override fun onStateSelected(stateId: Int) {
                                selectedStateList.clear()
                                selectedStateList.add(State(stateId.toString()))
                                tv_select_state.text = UserManager.getInstance().getState(stateId.toString())
                                onRefreshMapClick()
                            }
                        }
                )
            }
        })


        add_suggestion.setOnClickListener {
            DialogUtil.showAddSuggestionDialog(activity as Context, object : DialogUtil.AlertDialogInterface.OnAddSuggestionClickListener {
                override fun onSendClick(string: String) {
                    Utility.showProgressBarSmall(rl_progress_bar)
                    BackgroundExecutor().getInstance().execute(AddSuggestionRequester(string))
                }
            })
        }

        btn_refresh_repo_map.setOnClickListener(View.OnClickListener {
            onRefreshMapClick()
        })
    }

    private fun onRefreshMapClick() {
        if (selectedStateList.size == 0) {
            CommonMethods.showShortToast(context!!, "Please Select State")
        } else {
            Utility.showProgressBarSmall(rl_progress_bar)
            val repoMapRequest = RepoMapRequest()
            repoMapRequest.states = selectedStateList.map { it.state_id }.toTypedArray()
            if (selectedPermitList.size > 0) {
                repoMapRequest.permits = selectedPermitList.map { it.permit_id }.toTypedArray()
            }
            BackgroundExecutor().getInstance().execute(GetRepoMapRequester(repoMapRequest))
        }
    }

    private fun setPermitAdapter() {
        permitAdapter = SelectPermitAdapter(context!!, UserManager.getInstance().permits, this)

        rv_permits.layoutManager = LinearLayoutManager(context)
        rv_permits.adapter = permitAdapter
    }

    private fun setStateAdapter() {
        stateAdapter = SelectStateAdapter(context!!, selectedStateList, this)

        rv_states.layoutManager = GridLayoutManager(context, 4)
        rv_states.adapter = stateAdapter
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DIALOG_FRAGMENT) {
            if (resultCode == Activity.RESULT_OK) {
                selectedStateList.clear()
                selectedStateList = data!!.getParcelableArrayListExtra<State>("states")

                stateAdapter!!.notifyDataSetChanged()
            }
        }
    }
}
