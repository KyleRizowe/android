package com.fuel4media.carrythistoo.fragments


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.adapter.StateAdapter
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.DialogFragmentCallback
import com.fuel4media.carrythistoo.model.State
import kotlinx.android.synthetic.main.fragment_state_list.*


/**
 * A simple [Fragment] subclass.
 *
 */
class StateListFragment : DialogFragment(), StateAdapter.OnClickListener {
    override fun onCheckBoxClick(value: Boolean, contact: State) {
        if (value) {
            newStateList.add(contact)
        } else {
            newStateList.remove(contact)
        }
    }

    var stateList = ArrayList<State>()
    var newStateList = ArrayList<State>()
    var mainList = ArrayList<State>()
    var callbacks: DialogFragmentCallback? = null

    companion

    object {
        var TAG: String = StateListFragment::class.java.simpleName
        var KEY_LIST: String = "list"

        fun newInstance(stateList: ArrayList<State>): StateListFragment {
            val fragment = StateListFragment()
            val args = Bundle()
            args.putParcelableArrayList(KEY_LIST, stateList)
            fragment.setArguments(args)
            return fragment
        }
    }

    fun setCallback(callback: DialogFragmentCallback) {
        callbacks = callback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_state_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        stateList = arguments!!.getParcelableArrayList(KEY_LIST)

        newStateList.addAll(stateList)

        mainList = UserManager.getInstance().newStates()

        mainList.forEachIndexed { index, state ->
            state.status = false
        }

        newStateList.forEachIndexed { index, state ->
            if (mainList.contains(state)) {
                mainList.get(mainList.indexOf(state)).status = true
            }
        }

        rv_states.adapter = StateAdapter(context!!, mainList, this)
        rv_states.layoutManager = LinearLayoutManager(context)

        tv_ok.setOnClickListener(View.OnClickListener {
            callbacks!!.onOkClick(newStateList)
            dismiss()
        })

        tv_cancel.setOnClickListener(View.OnClickListener {
            dismiss()
        })
    }
}
