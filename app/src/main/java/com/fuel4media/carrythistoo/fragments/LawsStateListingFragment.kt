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
import com.fuel4media.carrythistoo.adapter.StateLawsAdapter
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.StateLaws
import com.fuel4media.carrythistoo.model.response.StateLawsListing
import com.fuel4media.carrythistoo.requester.StateLawsListRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.DialogUtil
import com.fuel4media.carrythistoo.utils.GlideUtil
import com.fuel4media.carrythistoo.utils.Utility
import kotlinx.android.synthetic.main.fragment_laws_state_listing.*
import kotlinx.android.synthetic.main.list_laws.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import kotlinx.android.synthetic.main.select_state_laws.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 */
class LawsStateListingFragment : BaseFragment(), StateLawsAdapter.LawClickListener {
    override fun onLawClick(law: StateLaws) {
        if (law.description != null) {
            DialogUtil.showLawDescDialog(context!!, law.description)
        }
    }

    var lawsList = ArrayList<StateLaws>()
    var contex: Context? = null
    var adapter: StateLawsAdapter? = null

    override fun updateLocationCallback(lastLocation: Location) {
        // Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.STATE_LAWS_LIST_SUCCESS -> {
                    val stateLawsListing = eventObject.`object` as StateLawsListing
                    lawsList.clear()

                    if (eventObject.`object` != null) {
                        lawsList.addAll(stateLawsListing.ccwlawlist as ArrayList<StateLaws>)
                    }

                    adapter!!.notifyDataSetChanged()

                    GlideUtil.loadImage(context!!, iv_permit_map, stateLawsListing.repomap)

                }
                EventConstant.STATE_LAWS_LIST_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        })
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboaredActivity) {
            context.setToolbar(getString(R.string.title_state_listing), false, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_laws_state_listing, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_register_location.text = UserManager.getInstance().getState(UserManager.getInstance().user.state)

        getLawsFromServer(UserManager.getInstance().user.state)

        tv_select_state.text = UserManager.getInstance().getState(UserManager.getInstance().user.state)

        rl_select_state.setOnClickListener(View.OnClickListener {
            if (UserManager.getInstance().isFreeium()) {
                startActivity(Intent(context, PaymentActivity::class.java))
            } else if (UserManager.getInstance().isOnTrial()) {
                DialogUtil.showPremiumDailog(context)
            } else {
                DialogUtil.showStateListing(
                        context!!,
                        UserManager.getInstance().states!!,
                        object : DialogUtil.AlertDialogInterface.StateDialogClickListener {
                            override fun onStateSelected(stateId: Int) {
                                tv_select_state.text = UserManager.getInstance().getState(stateId.toString())
                                getLawsFromServer(stateId.toString())
                            }
                        }
                )
            }
        })


        adapter = StateLawsAdapter(activity as Context, lawsList, this)
        rv_state_laws.layoutManager = LinearLayoutManager(context)
        rv_state_laws.adapter = adapter

        /* ll_search.setOnClickListener(View.OnClickListener {
             DialogUtil.showStateListing(
                     context!!,
                     null,
                     object : DialogUtil.AlertDialogInterface.StateDialogClickListener {
                         override fun onStateSelected(stateId: Int) {

                         }
                     }
             )
         })*/
    }

    private fun getLawsFromServer(state: String?) {
        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().getInstance().execute(StateLawsListRequester(state!!))
    }

}// Required empty public constructor
