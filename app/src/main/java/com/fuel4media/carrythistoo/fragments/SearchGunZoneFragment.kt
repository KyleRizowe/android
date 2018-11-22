package com.fuel4media.carrythistoo.fragments


import android.content.Context
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.support.design.R.id.message
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.activity.PaymentActivity
import com.fuel4media.carrythistoo.adapter.FilterAdapter
import com.fuel4media.carrythistoo.adapter.GunZoneAdapter
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.FilterType
import com.fuel4media.carrythistoo.model.request.GunZone
import com.fuel4media.carrythistoo.model.request.GunZoneRequest
import com.fuel4media.carrythistoo.requester.SearchGunZoneRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.DialogUtil
import com.fuel4media.carrythistoo.utils.Utility
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.filter_gun_zone.*
import kotlinx.android.synthetic.main.fragment_search_gun_zone.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import org.greenrobot.eventbus.Subscribe
import java.io.IOException


/**
 * A simple [Fragment] subclass.
 */
class SearchGunZoneFragment : BaseFragment(), OnMapReadyCallback, FilterAdapter.OnClickListener, GunZoneAdapter.GunZoneClickListener {
    override fun onGunZoneClick(index: Int, event: GunZone) {

    }

    override fun onCheckBoxClick(value: Boolean, filter: FilterType) {
        if (value) {
            selectedFilter!!.add(filter.filter_id!!)
        } else {
            selectedFilter!!.remove(filter.filter_id)
        }
    }

    var filterAdapter: FilterAdapter? = null
    var gunZoneAdapter: GunZoneAdapter? = null
    var selectedFilter = ArrayList<String>()
    var gunZones = ArrayList<GunZone>()
    var map: GoogleMap? = null

    override fun updateLocationCallback(lastLocation: Location) {
        // Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.FILTER_GUN_ZONE_SUCCESS -> {


                    gunZones.clear()

                    if (eventObject.`object` != null) {
                        gunZones.addAll(eventObject.`object` as ArrayList<GunZone>)
                        addMarkerTomap()
                    }

                    gunZoneAdapter!!.notifyDataSetChanged()

                }
                EventConstant.FILTER_GUN_ZONE_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        })
    }

    private fun geocoding(gunZone: GunZoneRequest?) {
        val geocoder: Geocoder = Geocoder(context)
        try {
            val addresses: List<Address> = geocoder!!.getFromLocationName(gunZone!!.zipe_code, 1)
            if (addresses != null && !addresses.isEmpty()) {
                val address: Address = addresses.get(0)
                // Use the address as needed
                val message: String = String.format("Latitude: %f, Longitude: %f",
                        address.getLatitude(), address.getLongitude());
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                gunZone.lat = address.getLatitude()
                gunZone.long = address.getLongitude()
            } else {
                // Display appropriate message when Geocoder services are not available
                Toast.makeText(context, "Unable to geocode zipcode", Toast.LENGTH_LONG).show();
            }
        } catch (e: IOException) {
            // handle exception
        }
    }

    private fun addMarkerTomap() {
        gunZones.forEachIndexed { index, gunZone ->

            val marker = MarkerOptions().position(LatLng(gunZone.lat!!, gunZone.long!!)).title(UserManager.getInstance().getGunZoneName(gunZone.type.toString())).icon(BitmapDescriptorFactory.defaultMarker(123f))
            map!!.addMarker(marker).showInfoWindow()
            map!!.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(gunZone.lat!!, gunZone.long!!), 22.0f));
        }
    }

    override fun onMapReady(p0: GoogleMap?) {
        map = p0
        map!!.getUiSettings().setZoomControlsEnabled(true)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboaredActivity) {
            context.setToolbar(getString(R.string.title_search_near_by_gun_zone), false, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_gun_zone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        transparent_image.setOnTouchListener(object : View.OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {
                val action = event.getAction()
                when (action) {
                    MotionEvent.ACTION_DOWN -> {
                        // Disallow ScrollView to intercept touch events.
                        scroll_view.requestDisallowInterceptTouchEvent(true)
                        // Disable touch on transparent view
                        return false
                    }

                    MotionEvent.ACTION_UP -> {
                        // Allow ScrollView to intercept touch events.
                        scroll_view.requestDisallowInterceptTouchEvent(false)
                        return true
                    }

                    MotionEvent.ACTION_MOVE -> {
                        scroll_view.requestDisallowInterceptTouchEvent(true)
                        return false
                    }

                    else -> return true
                }
            }
        })

        iv_filter.setOnClickListener(View.OnClickListener {
            if (filter_zone.visibility == View.VISIBLE) {
                filter_zone.visibility = View.GONE
            } else {
                filter_zone.visibility = View.VISIBLE
            }
        })

        setAdapter()

        gunZoneAdapter = GunZoneAdapter(context!!, gunZones, this)

        rv_gun_zones.layoutManager = LinearLayoutManager(context)
        rv_gun_zones.adapter = gunZoneAdapter

        edt_zip_code.setText("209729")

        edt_zip_code.isClickable = true

        getGunZoneFromServer()

        edt_zip_code.setOnClickListener(View.OnClickListener {
            if (UserManager.getInstance().isFreeium()) {
                startActivity(Intent(context, PaymentActivity::class.java))
            } else if (UserManager.getInstance().isOnTrial()) {
                DialogUtil.showPremiumDailog(context)
            } else {
                edt_zip_code.isActivated = true
                edt_zip_code.isEnabled = true
                edt_zip_code.isFocusable = true
            }
        })

        btn_search_gun_zone.setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(edt_zip_code.text.toString().trim())) {
                CommonMethods.showShortToast(context!!, "Please enter zip code")
            } else {
                getGunZoneFromServer()
            }
        })
    }

    private fun getGunZoneFromServer() {
        Utility.showProgressBarSmall(rl_progress_bar)
        val gunZoneRequest = GunZoneRequest()
        gunZoneRequest.filter_type = selectedFilter
        gunZoneRequest.zipe_code = edt_zip_code.text.toString()
        geocoding(gunZoneRequest)
        BackgroundExecutor().getInstance().execute(SearchGunZoneRequester(gunZoneRequest))
    }

    fun setAdapter() {
        filterAdapter = FilterAdapter(context!!, UserManager.getInstance().filters, this)

        rv_filters.layoutManager = GridLayoutManager(context, 3)
        rv_filters.adapter = filterAdapter
    }

    override fun onPause() {
        super.onPause()
        Utility.hideKeyboardFrom(context!!, edt_zip_code)
    }
}// Required empty public constructor
