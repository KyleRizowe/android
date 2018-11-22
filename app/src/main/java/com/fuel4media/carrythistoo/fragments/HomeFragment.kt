package com.fuel4media.carrythistoo.fragments


import android.app.Activity
import android.content.Context
import android.content.Context.SENSOR_SERVICE
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.activity.PaymentActivity
import com.fuel4media.carrythistoo.adapter.SelectPermitAdapter
import com.fuel4media.carrythistoo.adapter.SelectStateAdapter
import com.fuel4media.carrythistoo.drawroutemap.DrawMarker
import com.fuel4media.carrythistoo.drawroutemap.DrawRouteMaps
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.DialogFragmentCallback
import com.fuel4media.carrythistoo.model.PermitType
import com.fuel4media.carrythistoo.model.State
import com.fuel4media.carrythistoo.model.request.GunZone
import com.fuel4media.carrythistoo.model.request.RepoMapRequest
import com.fuel4media.carrythistoo.model.response.GunFreeZone
import com.fuel4media.carrythistoo.prefrences.AppPreference
import com.fuel4media.carrythistoo.requester.AddSuggestionRequester
import com.fuel4media.carrythistoo.requester.GetNearByGunZoneRequester
import com.fuel4media.carrythistoo.requester.GetRepoMapRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.DialogUtil
import com.fuel4media.carrythistoo.utils.Utility
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : BaseFragment(), OnMapReadyCallback, SensorEventListener {

    private var mSensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    private var location: Location? = null

    val PLACE_PICKER_REQUEST = 1


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(p0: SensorEvent?) {
        /* if (location != null) {
             showDirection(location!!)
         }*/
    }


    override fun updateLocationCallback(lastLocation: Location) {
        // Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()

        updateLocationAtMap(lastLocation)

        var distance: Float? = 0f

        if (location != null) {
            distance = location!!.distanceTo(lastLocation)
        }

        location = lastLocation;

        if (distance!!.compareTo(10f) > 0) {
            BackgroundExecutor().getInstance().execute(GetNearByGunZoneRequester(location!!.latitude, location!!.longitude))
        }
        //showDirection(lastLocation)


    }

    private fun updateLocationAtMap(lastLocation: Location) {
        if (marker != null) {
            marker!!.remove();
        }

        if (map != null) {
            val myPlace = LatLng(lastLocation.latitude, lastLocation.longitude)
            val markerOpt = MarkerOptions().position(myPlace).title("Current Location")
            map!!.getUiSettings().setZoomControlsEnabled(true)
            marker = map!!.addMarker(markerOpt)
            map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 14.0f))
        }
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.GUN_FREE_ZONE_SUCCESS -> {

                    gunFreeZones.clear()
                    gunFreeZones.addAll(eventObject.`object` as ArrayList<GunFreeZone>)


                    addMarkerTomap()
                }

                EventConstant.GUN_FREE_ZONE_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        });
    }

    private fun addMarkerTomap() {
        gunFreeZones.forEachIndexed { index, gunZone ->

            if (gunZone.lat != null && gunZone.long != null) {
                val marker = MarkerOptions().position(LatLng(gunZone.lat!!, gunZone.long!!)).title(gunZone.zone_name).icon(BitmapDescriptorFactory.defaultMarker(150f))
                map!!.addMarker(marker).showInfoWindow()
            }
            // map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(gunZone.lat!!, gunZone.long!!), 14.0f));
        }
    }


    var toolbar1: Toolbar? = null
    var map: GoogleMap? = null
    private var marker: Marker? = null

    var isSource = false
    var isDestination = false

    var sourceLocation: LatLng? = null
    var destinationLocation: LatLng? = null

    var gunFreeZones = ArrayList<GunFreeZone>()

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboaredActivity) {
            context.setToolbarHome()
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as DashboaredActivity).createLocationRequest()

        BackgroundExecutor().getInstance().execute(GetNearByGunZoneRequester(AppPreference.getInstance().latitude, AppPreference.getInstance().longitude))

        val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        toolbar1 = (activity as DashboaredActivity).getToolbar()

        toolbar1!!.edt_destination_location.setOnClickListener {

            isDestination = true
            isSource = false

            val builder = PlacePicker.IntentBuilder()

            startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)
        }

        toolbar1!!.edt_source_location.setOnClickListener {
            isSource = true
            isDestination = false

            val builder = PlacePicker.IntentBuilder()

            startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)
        }

        toolbar1!!.tv_go.setOnClickListener {
            if (UserManager.getInstance().isFreeium()) {
                startActivity(Intent(context, PaymentActivity::class.java))
            } else if (UserManager.getInstance().isOnTrial()) {
                DialogUtil.showPremiumDailog(context)
            } else {
                isSource = true
                if (TextUtils.isEmpty(toolbar1!!.edt_source_location.text)) {
                    sourceLocation = LatLng(location!!.altitude, location!!.longitude)
                    // CommonMethods.showShortToast(context!!, "Please select source location")
                }
                if (TextUtils.isEmpty(toolbar1!!.edt_destination_location.text)) {
                    CommonMethods.showShortToast(context!!, "Please select destination location")
                } else {
                    showDirection()
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSensorManager = context!!.getSystemService(SENSOR_SERVICE) as SensorManager?;
        sensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null) {
            // for the system's orientation sensor registered listeners
            mSensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);//SensorManager.SENSOR_DELAY_Fastest
        } else {
            Toast.makeText(context, "Not Supported", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!


        //map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30))
    }


    fun showDirection() {

        var des = Location("")
        des.latitude = destinationLocation!!.latitude
        des.longitude = destinationLocation!!.longitude

        var source = Location("")
        source.latitude = sourceLocation!!.latitude
        source.longitude = sourceLocation!!.longitude


        val bearing = source!!.bearingTo(des)

        val origin = sourceLocation
        val destination = destinationLocation

        DrawRouteMaps.getInstance(context)
                .draw(origin, destination, map)
        DrawMarker.getInstance(context).draw(map, origin, R.drawable.location, "Origin Location")
        DrawMarker.getInstance(context).draw(map, destination, R.drawable.location_black, "Destination Location")

//        val bounds = LatLngBounds.Builder()
//                .include(origin)
//                .include(destination).build()
//        val displaySize = Point()
//        activity!!.getWindowManager().getDefaultDisplay().getSize(displaySize)

        val SYDNEY = CameraPosition.Builder().target(origin)
                .zoom(15.5f)
                .bearing(bearing)
                .tilt(65f)
                .build()
        map!!.moveCamera((CameraUpdateFactory.newCameraPosition(SYDNEY)))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place: Place = PlacePicker.getPlace(data, context);

                if (isSource) {
                    toolbar1!!.edt_source_location!!.text = "" + place.name
                    sourceLocation = place.latLng
                }

                if (isDestination) {
                    toolbar1!!.edt_destination_location!!.text = "" + place.name
                    destinationLocation = place.latLng
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSensorManager!!.unregisterListener(this);
        //Toast.makeText(context, "Destroy", Toast.LENGTH_SHORT).show();
    }


/* override fun onOkClick(selectedList: ArrayList<State>) {
     selectedStateList.clear()
     selectedStateList.addAll(selectedList)
     stateAdapter!!.notifyDataSetChanged()
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

 private var mSensorManager: SensorManager? = null
 private var sensor: Sensor? = null
 private var location: Location? = null

 val PLACE_PICKER_REQUEST = 1

 override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

 }

 override fun onSensorChanged(p0: SensorEvent?) {
     *//* if (location != null) {
             showDirection(location!!)
         }*//*
    }

    override fun updateLocationCallback(lastLocation: Location) {
        // Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()

        updateLocationAtMap(lastLocation)

        location = lastLocation;
        //showDirection(lastLocation)


    }

    private fun updateLocationAtMap(lastLocation: Location) {
        if (marker != null) {
            marker!!.remove();
        }

        if (map != null) {
            val myPlace = LatLng(lastLocation.latitude, lastLocation.longitude)
            val markerOpt = MarkerOptions().position(myPlace).title("Location")
            map!!.getUiSettings().setZoomControlsEnabled(true)
            marker = map!!.addMarker(markerOpt)
            map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(myPlace, 14.0f))
        }
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
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
                EventConstant.GET_REPO_MAP_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        });
    }


    var toolbar1: Toolbar? = null
    var map: GoogleMap? = null
    private var marker: Marker? = null

    var isSource = false
    var isDestination = false

    var DIALOG_FRAGMENT = 123

    var stateAdapter: SelectStateAdapter? = null
    var permitAdapter: SelectPermitAdapter? = null

    var selectedStateList = ArrayList<State>()
    var selectedPermitList = ArrayList<PermitType>()


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboaredActivity) {
            context.setToolbarHome()
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as DashboaredActivity).createLocationRequest()

        tv_register_location.text = UserManager.getInstance().getState(UserManager.getInstance().user.state)


        setStateAdapter()

        setPermitAdapter()

        ll_select_state.setOnClickListener(View.OnClickListener {
            val dialogFrag = StateListFragment.newInstance(selectedStateList)
            // dialogFrag.setTargetFragment(this@HomeFragment, DIALOG_FRAGMENT)
            dialogFrag.show(fragmentManager!!.beginTransaction(), "dialog")
            dialogFrag.setCallback(this)
        })


        val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        toolbar1 = (activity as DashboaredActivity).getToolbar()

        toolbar1!!.edt_destination_location.setOnClickListener {

            isDestination = true
            isSource = false

            val builder = PlacePicker.IntentBuilder()

            startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)
        }

        toolbar1!!.edt_source_location.setOnClickListener {
            isSource = true
            isDestination = false

            val builder = PlacePicker.IntentBuilder()

            startActivityForResult(builder.build(activity), PLACE_PICKER_REQUEST)
        }

        add_suggestion.setOnClickListener {
            DialogUtil.showAddSuggestionDialog(activity as Context, object : DialogUtil.AlertDialogInterface.OnAddSuggestionClickListener {
                override fun onSendClick(string: String) {
                    Utility.showProgressBarSmall(rl_progress_bar)
                    BackgroundExecutor().getInstance().execute(AddSuggestionRequester(string))
                }
            })
        }

        btn_refresh_repo_map.setOnClickListener(View.OnClickListener {
            if (selectedStateList.size == 0) {
                CommonMethods.showShortToast(context!!, "Please Select State")
            } else if (selectedPermitList.size == 0) {
                CommonMethods.showShortToast(context!!, "Please Select Permit")
            } else {
                Utility.showProgressBarSmall(rl_progress_bar)
                val repoMapRequest = RepoMapRequest()
                repoMapRequest.states = selectedStateList.map { it.state_id }.toTypedArray()
                repoMapRequest.permits = selectedPermitList.map { it.permit_id }.toTypedArray()
                BackgroundExecutor().getInstance().execute(GetRepoMapRequester(repoMapRequest))
            }
        })
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mSensorManager = context!!.getSystemService(SENSOR_SERVICE) as SensorManager?;
        sensor = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null) {
            // for the system's orientation sensor registered listeners
            mSensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);//SensorManager.SENSOR_DELAY_Fastest
        } else {
            Toast.makeText(context, "Not Supported", Toast.LENGTH_SHORT).show();
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!


        //map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30))
    }


    fun showDirection(location: Location) {

        val destLoc = Location("")
        destLoc.latitude = 28.5921
        destLoc.longitude = 77.0460

        val bearing = location.bearingTo(destLoc)

        val origin = LatLng(location.latitude, location.longitude)
        val destination = LatLng(28.5921, 77.0460)

        DrawRouteMaps.getInstance(context)
                .draw(origin, destination, map)
        DrawMarker.getInstance(context).draw(map, origin, R.drawable.location, "Origin Location")
        DrawMarker.getInstance(context).draw(map, destination, R.drawable.location_black, "Destination Location")

//        val bounds = LatLngBounds.Builder()
//                .include(origin)
//                .include(destination).build()
//        val displaySize = Point()
//        activity!!.getWindowManager().getDefaultDisplay().getSize(displaySize)

        val SYDNEY = CameraPosition.Builder().target(origin)
                .zoom(15.5f)
                .bearing(bearing)
                .tilt(65f)
                .build()
        map!!.moveCamera((CameraUpdateFactory.newCameraPosition(SYDNEY)))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val place: Place = PlacePicker.getPlace(data, context);

                if (isSource) {
                    toolbar1!!.edt_source_location!!.text = "" + place.name
                }

                if (isDestination) {
                    toolbar1!!.edt_destination_location!!.text = "" + place.name
                }
            }
        } else if (requestCode == DIALOG_FRAGMENT) {
            if (resultCode == Activity.RESULT_OK) {
                selectedStateList.clear()
                selectedStateList = data!!.getParcelableArrayListExtra<State>("states")

                stateAdapter!!.notifyDataSetChanged()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mSensorManager!!.unregisterListener(this);
        //Toast.makeText(context, "Destroy", Toast.LENGTH_SHORT).show();
    }*/
}// Required empty public constructor

