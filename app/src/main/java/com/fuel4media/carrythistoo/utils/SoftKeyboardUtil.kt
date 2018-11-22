package com.fuel4media.carrythistoo.utils

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager

import java.lang.reflect.Field

import android.content.ContentValues.TAG

/**
 * Created by craterzone1 on 13/12/16.
 */
class SoftKeyboardUtil {

    private var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null
    private var imm: InputMethodManager? = null
    internal var vHeight = 0

    /**
     * Height monitor and keyboard when the keyboard is open, onDestroy in Activity in the call () method is called
     * In the class removeGlobalOnLayoutListener () method to remove the monitor
     *
     * @param activity
     * @param listener
     */
    fun observeSoftKeyboard(activity: Activity, listener: OnSoftKeyboardChangeListener) {
        val decorView = activity.window.decorView
        val statusBarHeight = getStatusBarHeight(activity)// The height of the status bar
        decorView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            private var keyboardHeight: Int = 0// Soft keyboard height
            private var isShowKeyboard: Boolean = false// It displays the status of the soft keyboard
            private var isVKeyMap: Boolean = false//Virtual buttons, Huawei cell phone

            override fun onGlobalLayout() {
                onGlobalLayoutListener = this

                // Application area that can be displayed. Here including regional applications occupied,
                // And ActionBar and status bar, but no equipment at the bottom of the virtual keys.
                val r = Rect()
                decorView.getWindowVisibleDisplayFrame(r)
                // 屏幕高度。这个高度不含虚拟按键的高度
                val screenHeight = decorView.rootView.height


                // When not display the soft keyboard, heightDiff equal to the height of the status bar
                // When display the soft keyboard, heightDiff become large, equal to the height plus the soft keyboard status bar.
                // So when heightDiff greater than the height of the status bar indicates the soft keyboard appears,
                // This time can be calculated keyboard height, that subtracting the height of the status bar heightDiff


                if (screenHeight - r.bottom < screenHeight / 4) {
                    isVKeyMap = true
                    vHeight = screenHeight - r.bottom
                } else if (screenHeight - r.bottom == 0) {
                    vHeight = 0
                    isVKeyMap = false
                }

                val heightDiff = screenHeight - (r.bottom - r.top)
                if (keyboardHeight == 0 && heightDiff > screenHeight / 4) {
                    if (isVKeyMap) {
                        keyboardHeight = heightDiff - vHeight
                    } else {
                        keyboardHeight = heightDiff
                    }
                }

                Log.e(TAG, "onGlobalLayout: keyboardHeight-------" + keyboardHeight)
                if (isVKeyMap) {

                    if (isShowKeyboard) {
                        // If the soft keyboard is a pop-up state, and less than or equal status bar height heightDiff，
                        // This time that the soft keyboard has Collapse
                        if (heightDiff <= statusBarHeight + vHeight) {
                            isShowKeyboard = false
                            listener.onSoftKeyBoardChange(keyboardHeight, isShowKeyboard)
                        }
                    } else {
                        // If the soft keyboard is retracted state, and heightDiff greater than the height of the status bar,
                        if (heightDiff > statusBarHeight && heightDiff > screenHeight / 4) {
                            isShowKeyboard = true
                            listener.onSoftKeyBoardChange(keyboardHeight, isShowKeyboard)
                        }
                    }
                } else {
                    if (isShowKeyboard) {
                        // If the soft keyboard is a pop-up state, and less than or equal status bar height heightDiff，
                        // This time that the soft keyboard has Collapse
                        if (heightDiff <= statusBarHeight) {
                            isShowKeyboard = false
                            listener.onSoftKeyBoardChange(keyboardHeight, isShowKeyboard)
                        }
                    } else {
                        if (heightDiff > statusBarHeight) {
                            isShowKeyboard = true
                            listener.onSoftKeyBoardChange(keyboardHeight, isShowKeyboard)
                        }
                    }
                }


            }
        })
    }

    interface OnSoftKeyboardChangeListener {
        fun onSoftKeyBoardChange(softKeybardHeight: Int, isShow: Boolean)
    }

    fun removeGlobalOnLayoutListener(activity: Activity) {
        val decorView = activity.window.decorView
        if (onGlobalLayoutListener != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                decorView.viewTreeObserver.removeGlobalOnLayoutListener(onGlobalLayoutListener)
            } else {
                decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
            }
        }
    }

    // Get the height of the status bar
    fun getStatusBarHeight(context: Context): Int {
        try {
            val c = Class.forName("com.android.internal.R\$dimen")
            val obj = c.newInstance()
            val field = c.getField("status_bar_height")
            val x = Integer.parseInt(field.get(obj).toString())
            return context.resources.getDimensionPixelSize(x)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return 0
    }

    fun hideKeyboard(context: Context, view: View) {
        view.requestFocus()
        imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.hideSoftInputFromWindow(view.applicationWindowToken,
                InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun showKeyboard(context: Context, view: View) {
        imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm!!.showSoftInput(view, 0)
    }

    companion object {


        fun hideKeyboard(activity: Activity) {
            try {
                val view = activity.window.currentFocus ?: return
                val inputManager = activity
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            } catch (e: Exception) {
                // Ignore exceptions if any
                Log.e("KeyBoardUtil", e.toString(), e)
            }

        }

        fun showKeyboard(activity: Activity, view: View) {
            try {
                val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                view.requestFocus()
                inputMethodManager.showSoftInput(view, 0)
            } catch (e: Exception) {
                // Ignore exceptions if any
                Log.e("KeyBoardUtil", e.toString(), e)
            }

        }
    }
}
