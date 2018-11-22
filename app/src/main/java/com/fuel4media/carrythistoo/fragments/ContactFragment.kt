package com.fuel4media.carrythistoo.fragments


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.adapter.ContactApapter
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.model.Contact
import com.fuel4media.carrythistoo.permissions.PermissionRequest
import com.fuel4media.carrythistoo.permissions.PermissionRequestHandler
import com.fuel4media.carrythistoo.permissions.PermissionsUtil
import com.fuel4media.carrythistoo.utils.Utility
import kotlinx.android.synthetic.main.fragment_contact.*
import org.greenrobot.eventbus.Subscribe


/**
 * A simple [Fragment] subclass.
 */
class ContactFragment : BaseFragment(), PermissionRequest.RequestContact, ContactApapter.OnClickListener {

    var contactApapter: ContactApapter? = null

    @Subscribe
    override fun onEvent(eventObject: EventObject) {

    }

    override fun updateLocationCallback(lastLocation: Location) {
    }

    override fun onCheckBoxClick(value: Boolean, contact: Contact) {
        if (value) {
            selectedList.add(contact)
        } else {
            selectedList.remove(contact)
        }
    }

    val phoneNumbers = ArrayList<String>()
    val nameList = ArrayList<Contact>()
    var selectedList = ArrayList<Contact>()
    val mobileNumberList = ArrayList<String>()

    companion

    object {
        var TAG: String = ContactFragment::class.java.simpleName
        var KEY_SELECTED_CONTACT: String = "selected"

        fun newInstance(selectedContact: ArrayList<Contact>): ContactFragment {
            val fragment = ContactFragment()
            val args = Bundle()
            args.putParcelableArrayList(KEY_SELECTED_CONTACT, selectedContact)
            fragment.setArguments(args)
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboaredActivity) {
            context.setToolbar(getString(R.string.title_contacts), true, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PermissionRequestHandler.requestContact(this@ContactFragment, "")

        btn_done.setOnClickListener(View.OnClickListener {
            selectedList.forEachIndexed { index, contact ->
                contact.mobile_number = getPhoneNumber(contact.name!!, context!!)
            }
            targetFragment!!.onActivityResult(
                    101,
                    Activity.RESULT_OK,
                    Intent().putExtra("mobile_numbers", selectedList)
            )
            (activity as DashboaredActivity).onBackPressed()
        })

        edt_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //contactApapter!!.filter.filter(p0)
                filter(p0.toString())
            }
        })

    }

    fun filter(text: String) {
        if (text.isEmpty()) {
            contactApapter!!.updateList(nameList)
            return
        }
        val temp = ArrayList<Contact>()
        for (d in nameList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.name!!.toLowerCase().contains(text.toLowerCase())) {
                temp.add(d)
            }
        }
        //update recyclerview
        contactApapter!!.updateList(temp)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions as Array<String>, grantResults, this)
    }

    fun getPhoneNumber(name: String, context: Context): String? {
        var ret: String? = null
        val selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " like'%" + name + "%'"
        val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val c = context.contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection, selection, null, null)
        if (c!!.moveToFirst()) {
            ret = c.getString(0)
        }
        c.close()
        /*  if (ret == null)
              ret = "Unsaved"*/
        return ret
    }

    override fun onContactPermissionGranted() {
        val cr = activity!!.getContentResolver()
        val cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cur!!.getCount() > 0) {
            while (cur!!.moveToNext()) {
                val id = cur!!.getString(cur!!.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cur!!.getString(cur!!.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                Log.i("Names", name)

                if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    nameList.add(Contact(id, name))
                }
            }
        }
        cur!!.close()
        setAdapter()
    }

    fun setAdapter() {
        if (arguments!!.containsKey(KEY_SELECTED_CONTACT)) {
            selectedList = arguments!!.getParcelableArrayList(KEY_SELECTED_CONTACT)
        }

        selectedList.forEach {
            if (nameList.contains(it)) {
                nameList.get(nameList.indexOf(it)).status = true
            }
        }

        contactApapter = ContactApapter(context!!, nameList, this)

        rv_contacts.layoutManager = LinearLayoutManager(context)
        rv_contacts.adapter = contactApapter
    }

    override fun onContactPermissionDenied() {
        Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Utility.hideKeyboard(context!!, edt_search)
    }
}// Required empty public constructor
