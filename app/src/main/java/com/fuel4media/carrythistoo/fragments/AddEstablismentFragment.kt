package com.fuel4media.carrythistoo.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.request.Establisment
import com.fuel4media.carrythistoo.requester.AddEstablishmentRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.DialogUtil
import com.fuel4media.carrythistoo.utils.Utility
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import kotlinx.android.synthetic.main.fragment_add_establisment.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 */
class AddEstablismentFragment : BaseFragment() {
    val PLACE_PICKER_REQUEST = 1
    var place: Place? = null

    override fun updateLocationCallback(lastLocation: Location) {
        //Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()
    }


    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.ADD_ESTABLISMENT_SUCCESS -> {
                    CommonMethods.showShortToast(context!!, eventObject.`object` as String)
                    clearView()
                }
                EventConstant.ADD_ESTABLISMENT_ERROR -> {
                    CommonMethods.showShortToast(context!!, eventObject.`object` as String)
                }
            }
        })
    }

    private fun clearView() {
        tv_establishment_type.setText("")
        edt_establisment_name.setText("")
        tv_address.setText("")
        edt_zip_code.setText("")
        edt_phone.setText("")
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboaredActivity) {
            context.setToolbar(getString(R.string.title_add_establisment), false, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_establisment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_address.setOnClickListener(View.OnClickListener {
            val builder = PlacePicker.IntentBuilder()

            startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)
        })

        tv_establishment_type.setOnClickListener(View.OnClickListener {
            DialogUtil.showEstablishmentType(
                    context!!,
                    object : DialogUtil.AlertDialogInterface.EstablistTypeDialogClickListener {
                        override fun onEstablishmentTypeSelected(type: String) {
                            tv_establishment_type.text = type
                        }
                    })
        })

        btn_add_establisment.setOnClickListener(View.OnClickListener {
            if (valid()) {
                addEstablismentToServer()
            }
        })
    }

    private fun addEstablismentToServer() {
        var estb = Establisment()
        estb.estableshment_name = edt_establisment_name.text.toString()
        estb.address = tv_address.text.toString()
        estb.lat = place!!.latLng.latitude
        estb.long = place!!.latLng.longitude
        /* estb.state = tv_state.text.toString()
         estb.zipe_code = edt_zip_code.text.toString()*/
        estb.phone = edt_phone.text.toString()

        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().getInstance().execute(AddEstablishmentRequester(estb))
    }

    private fun valid(): Boolean {
        if (TextUtils.isEmpty(edt_establisment_name.text.toString())) {
            CommonMethods.showShortToast(context!!, "Name is empty")
            return false
        } else if (TextUtils.isEmpty(tv_address.text.toString())) {
            CommonMethods.showShortToast(context!!, "address is empty")
            return false
        } else if (TextUtils.isEmpty(tv_establishment_type.text.toString())) {
            CommonMethods.showShortToast(context!!, "Establishment Type is empty")
            return false
            /* } else if (TextUtils.isEmpty(edt_zip_code.text.toString())) {
                 CommonMethods.showShortToast(context!!, "Zip code is empty")
                 return false*/
        } else if (TextUtils.isEmpty(edt_phone.text.toString())) {
            CommonMethods.showShortToast(context!!, "phone is empty")
            return false
        } else {
            return true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                place = PlacePicker.getPlace(data, activity)

                tv_address!!.setText("" + place!!.name + " \n" + place!!.address)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Utility.hideKeyboard(activity!!)
    }

}// Required empty public constructor
