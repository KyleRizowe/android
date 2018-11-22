package com.fuel4media.carrythistoo.activity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.support.design.internal.NavigationMenuView
import android.support.design.widget.NavigationView
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.Toolbar
import android.text.Html
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.fragments.*
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.AdminMessage
import com.fuel4media.carrythistoo.model.Event
import com.fuel4media.carrythistoo.model.Message
import com.fuel4media.carrythistoo.model.request.GunZone
import com.fuel4media.carrythistoo.model.response.GunFreeZone
import com.fuel4media.carrythistoo.requester.LogoutRequester
import com.fuel4media.carrythistoo.utils.CommonMethods
import com.fuel4media.carrythistoo.utils.DialogUtil
import com.fuel4media.carrythistoo.utils.FragmentFactory
import com.fuel4media.carrythistoo.utils.Utility
import kotlinx.android.synthetic.main.activity_dashboared.*
import kotlinx.android.synthetic.main.nav_header_dashboared.view.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import kotlinx.android.synthetic.main.toolbar_layout.*
import org.greenrobot.eventbus.Subscribe


class DashboaredActivity : LocationActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        var isVisible = false
    }

    override fun updateLocationCallback() {
        currentFragment.updateLocationCallback(lastLocation)
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        runOnUiThread(Runnable {
            Utility.hideProgressBar(rl_progress_bar)
            onHandleBaseEvent(eventObject)
            when (eventObject.id) {
                EventConstant.LOGOUT_SUCCESS -> {
                    startActivity(Intent(this@DashboaredActivity, LoginActivity::class.java))
                    finish()
                }
                EventConstant.LOGOUT_ERROR -> {
                    CommonMethods.showShortToast(this, eventObject.`object` as String)
                }
            }
        })
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.d("DashboardActivity", "onNewIntent")

        if (intent!!.hasExtra("type")) {
            if (intent.getStringExtra("type").equals("message")) {
                replaceFragment(MessageFragment())
            }

            if (intent.getStringExtra("type").equals("event")) {
                replaceFragment(CalendarFragment())
            }

            if (intent.getStringExtra("type").equals("gunzone")) {
                replaceFragment(HomeFragment())
            }
        }

    }

    var toolbar1: Toolbar? = null
    var toggle: ActionBarDrawerToggle? = null
    private lateinit var currentFragment: BaseFragment

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val message = intent.getParcelableExtra<Message>("message")
            Log.d("receiver", "Got message: $message")

            if (currentFragment is MessageFragment) {
                (currentFragment as MessageFragment).messageNotification(message)
            } else {
                sendNotification(message)
            }
        }
    }


    private val mAdminMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val message = intent.getParcelableExtra<AdminMessage>("admin_message")
            Log.d("receiver", "Got message: $message")
            sendAdminMessageNotification(message)
        }
    }


    private val mGunZoneReciever = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent
            val gunzone = intent.getParcelableExtra<GunFreeZone>("gunzone")
            Log.d("receiver", "Got message: $gunzone")
            sendGunZoneNotification(gunzone)
        }
    }


    private val mEventReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get extra data included in the Intent

            // val event = intent.getStringExtra("event")

            val event = intent.getParcelableExtra<Event>("event")


            if (currentFragment is CalendarFragment) {
                (currentFragment as CalendarFragment).eventNotification(event)
            } else {
                sendEventNotification(event)
            }
        }
    }

    private fun sendNotification(messageBody: Message) {
        val intent = Intent(this, DashboaredActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("type", "message")
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = "111"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setContentTitle(Html.fromHtml("<b><font color='#000000'>Message From Admin!</font></b>"))
                .setContentText(Html.fromHtml("<font color='#000000'>" + messageBody.messages + "</font>"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                //.setColor(getResources().getColor(R.color.colorPrimaryDark))
                //.setSmallIcon(R.drawable.icon_notification)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification)
            notificationBuilder.color = resources.getColor(R.color.colorPrimaryDark)
        } else {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification_color)
        }


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun sendGunZoneNotification(gunZone: GunFreeZone) {
        val intent = Intent(this, DashboaredActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("type", "gunzone")
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = "111"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentTitle(Html.fromHtml("<b><font color='#000000'>Gun Free Zone Alert!</font></b>"))
                .setContentText(Html.fromHtml("<font color='#000000'>" + gunZone.gun_key_id + " Distance - " + gunZone.distance + " Miles </font>"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                // .setColor(getResources().getColor(R.color.colorPrimaryDark))
                //.setSmallIcon(R.drawable.icon_notification)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_notification))
                .setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification)
            notificationBuilder.color = resources.getColor(R.color.colorPrimaryDark)
        } else {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification_color)
        }


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            channel.setShowBadge(true)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }


    private fun sendEventNotification(event: String) {
        val intent = Intent(this, DashboaredActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("type", "event")
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = "111"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setContentTitle("Carry This Too")
                .setContentText(event)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                //.setSmallIcon(R.drawable.logo)
                //.setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification)
            notificationBuilder.color = resources.getColor(R.color.colorPrimaryDark)
        } else {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification_color)
        }


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun sendEventNotification(event: Event) {
        val intent = Intent(this, DashboaredActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("type", "event")
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = "111"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val content = Html.fromHtml("<font color='#000000'>" + event.event_name + " Date :" + CommonMethods.Companion.changeDateFormatTOMonth(event.timestamp) + " \n Location : " + event.addlocation + "</font>").toString()
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setContentTitle(Html.fromHtml("<b><font color='#000000'>Upcoming Event Alert!</font></b>"))
                .setContentText(content)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                //.setColor(getResources().getColor(R.color.colorPrimaryDark))
                //.setSmallIcon(R.drawable.icon_notification)
                .setPriority(Notification.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setStyle(NotificationCompat.BigTextStyle().bigText(content))

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification)
            notificationBuilder.color = resources.getColor(R.color.colorPrimaryDark)
        } else {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification_color)
        }


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun sendAdminMessageNotification(adminMessage: AdminMessage) {
        val intent = Intent(this, DashboaredActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("type", "admin_message")
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT)

        val channelId = "111"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setContentTitle(Html.fromHtml("<b><font color='#000000'>" + adminMessage.title + "</font></b>"))
                .setContentText(Html.fromHtml("<font color='#000000'>" + adminMessage.message + "</font>"))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(Notification.PRIORITY_HIGH)
                //.setColor(resources.getColor(R.color.colorPrimaryDark))
                //.setSmallIcon(R.drawable.icon_notification)
                .setContentIntent(pendingIntent)
                .setStyle(NotificationCompat.BigTextStyle().bigText(Html.fromHtml(adminMessage.message)))

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification)
            notificationBuilder.color = resources.getColor(R.color.colorPrimaryDark)
        } else {
            notificationBuilder.setSmallIcon(R.drawable.icon_notification_color)
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)

            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboared)

        onNewIntent(intent)

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                IntentFilter("message_notification"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mGunZoneReciever,
                IntentFilter("gunzone_notification"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mEventReceiver,
                IntentFilter("event_notification"));

        LocalBroadcastManager.getInstance(this).registerReceiver(mAdminMessageReceiver,
                IntentFilter("admin_message_notification"));

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        val navMenuView = nav_view.getChildAt(0) as NavigationMenuView
        navMenuView.addItemDecoration(DividerItemDecoration(this@DashboaredActivity, DividerItemDecoration.VERTICAL))

        setupActionBar(toolbar)
        onNavigationItemSelected(nav_view.getMenu().getItem(5));
        nav_view.setCheckedItem(R.id.nav_home);
        hideInviteGunOwner()

    }

    private fun hideInviteGunOwner() {
        val nav_Menu = nav_view.getMenu()
        nav_Menu.findItem(R.id.nav_invite_owner).setVisible(false)
    }

    fun setupActionBar(toolbar: Toolbar?) {

        this.toolbar1 = toolbar;
        /* setSupportActionBar(toolbar)
         // supportActionBar!!.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
         supportActionBar!!.setDisplayShowTitleEnabled(false);
         supportActionBar!!.setDisplayHomeAsUpEnabled(true)
         supportActionBar!!.setHomeButtonEnabled(true)
         //supportActionBar!!.setCustomView(R.layout.toolbar_layout)

 */
        toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        // toggle!!.setHomeAsUpIndicator(R.drawable.menu_icon)
        toggle!!.isDrawerIndicatorEnabled = false
        toggle!!.setHomeAsUpIndicator(null)
        drawer_layout.addDrawerListener(toggle!!)
        toggle!!.syncState()

        toggle!!.setToolbarNavigationClickListener(View.OnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        })

        nav_view.setNavigationItemSelectedListener(this)

        val headerView: View = nav_view.getHeaderView(0)

        headerView.iv_close.setOnClickListener(View.OnClickListener {
            drawer_layout.closeDrawer(GravityCompat.START)
        })

        iv_menu_toggle.setOnClickListener(View.OnClickListener {

            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                Utility.hideKeyboard(this)
                drawer_layout.openDrawer(GravityCompat.START)
            }
        })

        iv_menu_toggle_home.setOnClickListener(View.OnClickListener {
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
            } else {
                drawer_layout.openDrawer(GravityCompat.START)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        isVisible = true
    }

    override fun onStop() {
        super.onStop()
        isVisible = false
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            if (FragmentFactory.isFragmentStackIsEmpty(this)) {
                DialogUtil.showTwoButtonDialog(this@DashboaredActivity, getString(R.string.msg_app_exit), getString(R.string.txt_yes), getString(R.string.txt_no), object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                    override fun onPositiveButtonClick() {
                        FragmentFactory.back(this@DashboaredActivity)
                    }

                    override fun onNegativeButtonClick() {

                    }
                })
            } else {
                FragmentFactory.back(this)
            }
        }

    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_home -> {
                replaceFragment(HomeFragment())
            }

            R.id.nav_calendar -> {
                if (UserManager.getInstance().isFreeium()) {
                    startActivity(Intent(this, PaymentActivity::class.java))
                } else if (UserManager.getInstance().isOnTrial()) {
                    DialogUtil.showPremiumDailog(this)
                } else {
                    replaceFragment(CalendarFragment())
                }
            }
            R.id.nav_message -> {
                replaceFragment(MessageFragment())

            }
            R.id.nav_repo_map -> {
                replaceFragment(RepoMapFragment())

            }
            R.id.nav_search -> {
                if (UserManager.getInstance().isFreeium()) {
                    startActivity(Intent(this, PaymentActivity::class.java))
                } else if (UserManager.getInstance().isOnTrial()) {
                    DialogUtil.showPremiumDailog(this)
                } else {
                    replaceFragment(SearchGunZoneFragment())
                }
            }
            R.id.nav_state_listing -> {
                replaceFragment(LawsStateListingFragment())

            }
            R.id.nav_comapre_state -> {
                replaceFragment(CompareStateLawsFragment())

            }

            R.id.nav_permit_info -> {
                replaceFragment(PermitsFragment())

            }

            R.id.nav_add_establisment -> {
                replaceFragment(AddEstablismentFragment())
            }
            R.id.nav_invite_owner -> {
                replaceFragment(InviteGunOwnerFragment())

            }
            R.id.nav_settings -> {
                replaceFragment(SettingsFragment())
            }
            R.id.nav_logout -> {
                DialogUtil.showTwoButtonDialog(this@DashboaredActivity, getString(R.string.msg_logout), getString(R.string.txt_yes), getString(R.string.txt_no), object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                    override fun onPositiveButtonClick() {
                        logoutRequestToServer()
                    }

                    override fun onNegativeButtonClick() {

                    }
                })
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    private fun logoutRequestToServer() {
        Utility.showProgressBarSmall(rl_progress_bar)
        BackgroundExecutor().getInstance().execute(LogoutRequester())

        /*  UserManager.getInstance().logout()
          startActivity(Intent(this@DashboaredActivity, LoginActivity::class.java))
          finish()*/

    }

    fun getToolbar(): Toolbar? {
        return toolbar1
    }

    fun replaceFragment(fragment: BaseFragment) {
        currentFragment = fragment;
        FragmentFactory.replaceFragment(fragment, R.id.container, this)
    }

    fun replaceFragmentWithTag(fragment: BaseFragment, tag: String) {
        currentFragment = fragment;
        FragmentFactory.replaceFragment(fragment, R.id.container, this, tag)
    }

    fun setToolbar(title: String, isBack: Boolean, isPlusIcon: Boolean) {
        toolbar_home.visibility = View.GONE
        toolbar_normal.visibility = View.VISIBLE

        tv_title.setText(title)
        if (isBack) {
            iv_back.visibility = View.VISIBLE
            iv_menu_toggle.visibility = View.GONE
            // toggle!!.setHomeAsUpIndicator(null)
        } else {
            iv_back.visibility = View.GONE
            iv_menu_toggle.visibility = View.VISIBLE
            //toggle!!.setHomeAsUpIndicator(R.drawable.menu_icon)
        }

        if (isPlusIcon) {
            iv_plus.visibility = View.VISIBLE
        } else {
            iv_plus.visibility = View.GONE
        }

        iv_back.setOnClickListener(View.OnClickListener {
            FragmentFactory.back(this)
        })

    }

    fun setToolbarHome() {
        toolbar_home.visibility = View.VISIBLE
        toolbar_normal.visibility = View.GONE
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mEventReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGunZoneReciever);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mAdminMessageReceiver);
    }

    /*
    public open override fun onBackPressed() {
         if (FragmentFactory.isFragmentStackIsEmpty(this)) {
             DialogUtil.showTwoButtonDialog(this@DashboaredActivity, getString(R.string.msg_app_exit), getString(R.string.txt_yes), getString(R.string.txt_no), object : DialogUtil.AlertDialogInterface.TwoButtonDialogClickListener {
                override fun onPositiveButtonClick() {
                     FragmentFactory.back(this@DashboaredActivity)
                 }

                override fun onNegativeButtonClick() {

                 }
             })
         } else {
             FragmentFactory.back(this)
         }
     }*/
}


