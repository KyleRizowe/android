package com.fuel4media.carrythistoo.fragments


import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.activity.PaymentActivity
import com.fuel4media.carrythistoo.adapter.CompareStateLawsAdpater
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.StateLaws
import com.fuel4media.carrythistoo.requester.ComapreStateLawsRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.DialogUtil
import com.fuel4media.carrythistoo.utils.Utility
import kotlinx.android.synthetic.main.compare_list_laws.*
import kotlinx.android.synthetic.main.compare_state_laws.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 */
class CompareStateLawsFragment : BaseFragment(), CompareStateLawsAdpater.LawClickListener {
    override fun onLawClick(law: StateLaws) {
        if (law.description != null) {
            DialogUtil.showLawDescDialog(context!!, law.description)
        }
    }

    override fun updateLocationCallback(lastLocation: Location) {
        //Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()
    }


    var lawsList = ArrayList<StateLaws>()
    var contex: Context? = null
    var adapter: CompareStateLawsAdpater? = null

    var state1: Int? = null
    var state2: Int? = null

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.COMAPRE_STATE_LAWS_LIST_SUCCESS -> {
                    // val stateLawsListing = eventObject.`object` as StateLawsListing
                    lawsList.clear()

                    if (eventObject.`object` != null) {
                        lawsList.addAll(eventObject.`object` as ArrayList<StateLaws>)
                    }

                    adapter!!.notifyDataSetChanged()

                    //GlideUtil.loadImage(context!!, iv_permit_map, stateLawsListing.repomap)
                }

                EventConstant.COMPARE_STATE_LAWS_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        })

    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboaredActivity) {
            context.setToolbar(getString(R.string.title_compare_ccw_laws), false, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compare_state_laws, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_register_location_comp.text = UserManager.getInstance().getState(UserManager.getInstance().user.state)

        ll_first_state.setOnClickListener(View.OnClickListener {
            DialogUtil.showStateListing(
                    context!!,
                    UserManager.getInstance().states!!,
                    object : DialogUtil.AlertDialogInterface.StateDialogClickListener {
                        override fun onStateSelected(stateId: Int) {
                            adapter!!.updateState1(stateId.toString())
                            tv_first_state.text = UserManager.getInstance().getState(stateId.toString())
                            state1 = stateId
                        }
                    }
            )
        })

        ll_second_state.setOnClickListener(View.OnClickListener {
            DialogUtil.showStateListing(
                    context!!,
                    UserManager.getInstance().states!!,
                    object : DialogUtil.AlertDialogInterface.StateDialogClickListener {
                        override fun onStateSelected(stateId: Int) {
                            adapter!!.updateState2(stateId.toString())
                            tv_second_state.text = UserManager.getInstance().getState(stateId.toString())
                            state2 = stateId
                        }
                    }
            )
        })

        btn_compare_laws.setOnClickListener(View.OnClickListener {
            if (UserManager.getInstance().isFreeium()) {
                startActivity(Intent(context, PaymentActivity::class.java))
            } else if (UserManager.getInstance().isOnTrial()) {
                DialogUtil.showPremiumDailog(context)
            } else {
                if (valid())
                    comapareStateLaws(state1, state2)
            }
        })

        adapter = CompareStateLawsAdpater(activity as Context, lawsList, this)
        rv_compare_state_laws.layoutManager = LinearLayoutManager(context)
        rv_compare_state_laws.adapter = adapter
    }

    private fun valid(): Boolean {
        if (tv_first_state.text.isEmpty()) {
            CommonMethods.showShortToast(context!!, "Please select first state")
            return false
        } else if (tv_second_state.text.isEmpty()) {
            CommonMethods.showShortToast(context!!, "Please select second state")
            return false
        } else {
            return true
        }
    }

    private fun comapareStateLaws(state1: Int?, state2: Int?) {
        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().getInstance().execute(ComapreStateLawsRequester(state1.toString(), state2.toString()))
    }
}// Required empty public constructor
