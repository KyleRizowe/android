package com.fuel4media.carrythistoo.fragments


import android.Manifest
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.craterzone.media.images.CameraUtil
import com.craterzone.media.images.ImageUtil
import com.cz.imagelib.Crop
import com.fuel4media.carrythistoo.R
import com.fuel4media.carrythistoo.activity.DashboaredActivity
import com.fuel4media.carrythistoo.eventbus.EventConstant
import com.fuel4media.carrythistoo.eventbus.EventObject
import com.fuel4media.carrythistoo.executer.BackgroundExecutor
import com.fuel4media.carrythistoo.manager.UserManager
import com.fuel4media.carrythistoo.model.Permit
import com.fuel4media.carrythistoo.permissions.PermissionRequest
import com.fuel4media.carrythistoo.permissions.PermissionRequestHandler
import com.fuel4media.carrythistoo.permissions.PermissionsUtil
import com.fuel4media.carrythistoo.requester.AddPermitRequester
import com.fuel4media.carrythistoo.requester.EditPermitRequester
import com.fuel4media.carrythistoo.utils.*
import com.yalantis.ucrop.UCrop
import kotlinx.android.synthetic.main.fragment_add_permit.*
import kotlinx.android.synthetic.main.fragment_search_gun_zone.*
import kotlinx.android.synthetic.main.progress_bar_small.*
import org.greenrobot.eventbus.Subscribe
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
class AddPermitFragment : BaseFragment(), PermissionRequest.RequestCustomPermissionGroup, PermissionRequest.RequestStorage {
    override fun onAllCustomPermissionGroupGranted() {
        //  CommonMethods.showShortToast(context!!, "All custom permission granted")
        Crop.captureImage(activity)
    }

    override fun onCustomPermissionGroupDenied() {
        //CommonMethods.showShortToast(context!!, "All custom permission denied")
    }

    override fun onStoragePermissionGranted() {
        // CommonMethods.showShortToast(context!!, "Storage permission granted")
        Crop.pickImage(activity)
    }

    override fun onStoragePermissionDenied() {
        //CommonMethods.showShortToast(context!!, "Storage permission denied")
    }


    var permit: Permit? = null
    var permitId: String? = null
    var stateID: String? = null
    var imageUri: Uri? = null
    var imagePath: String? = null

    override fun updateLocationCallback(lastLocation: Location) {
        //Toast.makeText(context, "Location : " + lastLocation.latitude + ", " + lastLocation.longitude + " ", Toast.LENGTH_SHORT).show()
    }

    @Subscribe
    override fun onEvent(eventObject: EventObject) {
        activity!!.runOnUiThread(Runnable {
            onHandleBaseEvent(eventObject)
            Utility.hideProgressBar(rl_progress_bar)
            when (eventObject.id) {
                EventConstant.ADD_PERMIT_SUCCESS -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                    (activity as DashboaredActivity).onBackPressed()
                }
                EventConstant.ADD_PERMIT_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }

                EventConstant.EDIT_PERMIT_SUCCESS -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                    (activity as DashboaredActivity).onBackPressed()
                }
                EventConstant.EDIT_PERMIT_ERROR -> {
                    CommonMethods.showShortToast(activity as Context, eventObject.`object` as String)
                }
            }
        })

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DashboaredActivity) {
            (context as DashboaredActivity).setToolbar(getString(R.string.title_permit_information), true, false)
        }
    }

    companion object {
        var TAG: String = AddPermitFragment::class.java.simpleName
        val KEY_PERMIT = "permit"

        fun newInstance(permit: Permit?): AddPermitFragment {
            val fragment = AddPermitFragment()
            val bundle = Bundle()
            bundle.putParcelable(KEY_PERMIT, permit)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_permit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (arguments!!.get(KEY_PERMIT) != null) {
            permit = arguments!!.get(KEY_PERMIT) as Permit
        }

        if (permit != null) {
            edt_state_name.setText(permit!!.state_name)
            edt_permit_id.setText(permit!!.permit_id)
            tv_permit_type.setText(permit!!.permit_name)
            imagePath = permit!!.permit_image
            permitId = permit!!.permit_type
            stateID = permit!!.state_id
            GlideUtil.loadImage(context!!, iv_permit_image, permit!!.permit_image)
        }

        edt_state_name.setOnClickListener(View.OnClickListener {
            DialogUtil.showStateListing(context!!, UserManager.getInstance().states, object : DialogUtil.AlertDialogInterface.StateDialogClickListener {
                override fun onStateSelected(stateId: Int) {
                    stateID = stateId.toString()
                    edt_state_name.setText(UserManager.getInstance().getState(stateId.toString()))
                }
            })
        })

        tv_permit_type.setOnClickListener(View.OnClickListener {
            DialogUtil.showPermits(context!!, UserManager.getInstance().permits, object : DialogUtil.AlertDialogInterface.StateDialogClickListener {
                override fun onStateSelected(stateId: Int) {
                    permitId = stateId.toString()
                    tv_permit_type.setText(UserManager.getInstance().getPermitName(stateId.toString()))
                }
            })
        })

        iv_permit_image.setOnClickListener(View.OnClickListener {
            selectedImage()
        })

        btn_confirm_permit.setOnClickListener(View.OnClickListener {
            if (valid()) {
                Utility.showProgressBarSmall(rl_progress_bar)
                sendPermitToServer()
            }
        })
    }

    private fun valid(): Boolean {
        if (TextUtils.isEmpty(edt_state_name.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, "State name is empty")
            return false
        } else if (TextUtils.isEmpty(edt_permit_id.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, "Permit ID is empty")
            return false
        } else if (TextUtils.isEmpty(tv_permit_type.text.toString().trim())) {
            CommonMethods.showShortToast(context!!, "Permit type is empty")
            return false
        } else if (imagePath == null) {
            CommonMethods.showShortToast(context!!, "Please select image")
            return false
        } else {
            return true
        }
    }

    private fun sendPermitToServer() {
        val newPermit = Permit()
        newPermit.permit_id = edt_permit_id.text.toString()
        newPermit.state_name = stateID
        newPermit.permit_type = permitId
        if (imageUri != null) {
            newPermit.permit_image = imageUri!!.path
        }

        if (permit != null) {
            newPermit.id = permit!!.id
            BackgroundExecutor().getInstance().execute(EditPermitRequester(newPermit))
        } else {
            BackgroundExecutor().getInstance().execute(AddPermitRequester(newPermit))
        }
    }

    private fun selectedImage() {
        DialogUtil.openChooseMediaDialog(context!!, object : DialogUtil.AlertDialogInterface.OpenCameraDialogListener {
            override fun onCameraClick() {
                PermissionRequestHandler.requestCustomPermissionGroup(this@AddPermitFragment, "", Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            override fun onGalleryClick() {
                PermissionRequestHandler.requestStorage(this@AddPermitFragment, "")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Crop.REQUEST_PICK -> {
                if (resultCode == RESULT_OK) {
                    pickImageFromGallery(data!!.getData())
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    //Toast.makeText(context, "User cancelled image pick", Toast.LENGTH_SHORT).show();
                }
            }
            Crop.REQUEST_CAPTURE -> {
                if (resultCode == RESULT_OK) {
                    captureImageFromCamera()
                } else if (resultCode == Activity.RESULT_CANCELED) {
                    // user cancelled Image capture
                } else {
                    // failed to capture image
                }
            }
            UCrop.REQUEST_CROP -> {
                handleCrop(resultCode, data)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionsUtil.onRequestPermissionsResult(requestCode, permissions as Array<String>, grantResults, this)
    }

    private fun pickImageFromGallery(uri: Uri?) {
        var imagePath = ImageUtil.getPath(context, uri!!)
        imagePath = CameraUtil.roateImageIfRequired(imagePath)
        val copyImageFile = FileUtil.copyImageFile(context, uri, imagePath)
        UCrop.of(uri, Uri.fromFile(copyImageFile))
                .start(activity!!)
    }

    private fun captureImageFromCamera() {
        val imageCaptureUri = com.cz.imagelib.CameraUtil.getLastImageUri()
        var imagePath = imageCaptureUri.path
        //String imagePath = ImageUtil.getPath(context, imageCaptureUri);
        imagePath = com.cz.imagelib.CameraUtil.roateImageIfRequired(imagePath)
        val copyImagePath = FileUtil.copyImageFile(context, imageCaptureUri, imagePath)
        UCrop.of(imageCaptureUri, Uri.fromFile(copyImagePath))
                .start(activity!!)
    }

    private fun handleCrop(resultCode: Int, result: Intent?) {
        if (resultCode == RESULT_OK && result != null) {
            imageUri = UCrop.getOutput(result)
            imagePath = imageUri!!.getPath()
            val file = File(imageUri!!.getPath())
            //file size in KB
            val fileSize = file.length() / 1024
            if (fileSize > 1024) {
                CommonMethods.showShortToast(context!!, "File size must be less than 1 MB")
                return
            }

            GlideUtil.loadImage(context!!, iv_permit_image, imageUri)

        } else if (resultCode == UCrop.RESULT_ERROR && result != null) {
            Toast.makeText(context, UCrop.getError(result)!!.message, Toast.LENGTH_SHORT).show()
        } else {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Utility.hideKeyboardFrom(context!!, edt_permit_id)
    }

}// Required empty public constructor
