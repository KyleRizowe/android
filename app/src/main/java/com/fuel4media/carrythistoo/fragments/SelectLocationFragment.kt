package com.fuel4media.carrythistoo.fragments


import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.eventbus.EventObject
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 */
class SelectLocationFragment : BaseFragment() {

    override fun updateLocationCallback(lastLocation: Location) {
        //Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()
    }

    var TAG = SelectLocationFragment::class.simpleName

    @Subscribe
    override fun onEvent(eventObject: EventObject) {

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}// Required empty public constructor
