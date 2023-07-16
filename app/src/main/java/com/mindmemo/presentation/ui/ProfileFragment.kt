package com.mindmemo.presentation.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
 import com.google.android.material.snackbar.Snackbar
import com.mindmemo.R
import com.mindmemo.databinding.FragmentProfileBinding
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {
    private lateinit var fragmentProfileBinding: FragmentProfileBinding
    private var picturePath: String? = null
    private var selectedImage: Uri? = null

    var firstName: String? = null
    var lastName: String? = null
    var email:String? = null
    var ImgFromStore: String? = null

    var GetContent = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        // Handle the returned Uri
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data = result.data!!
            selectedImage = data.data
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = requireActivity().contentResolver.query(
                selectedImage!!,
                filePathColumn,
                null,
                null,
                null
            )
            cursor!!.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            picturePath = cursor.getString(columnIndex)
            cursor.close()
            fragmentProfileBinding.roundedImageView.setImageBitmap(
                BitmapFactory.decodeFile(
                    picturePath
                )
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentProfileBinding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        setupSaveBtn()
        ReadDataStore()
        setdefaultValue()
        return fragmentProfileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPhotoFromGallery()
    }

    private fun getPhotoFromGallery() {
        fragmentProfileBinding.changeImg.setOnClickListener(View.OnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    2000
                )
            } else {
                val cameraIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                GetContent.launch(cameraIntent)
            }
        })
    }

    private fun setupSaveBtn() {
        fragmentProfileBinding.saveBtn.setOnClickListener{
            writetoDataStore()
            Snackbar
                .make(requireView(), "data saved successfully", Snackbar.LENGTH_LONG)
                .show()
        }
    }

    private fun writetoDataStore() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        val editor = sharedPrefs.edit()
        if (picturePath != null) {
            editor.putString(
                "profileImg",
                encodeTobase64(BitmapFactory.decodeFile(picturePath))
            )
        }
        editor.putString(
            "firstName",
            fragmentProfileBinding.editTextTextPersonName.text.toString()
        )
        editor.putString(
            "email",
            fragmentProfileBinding.editTextTextPersonName3.text.toString()
        )
        editor.putString(
            "lastName",
            fragmentProfileBinding.editTextTextPersonName2.text.toString()
        )
        editor.apply()
    }

    fun decodeBase64(input: String?): Bitmap? {
        val decodedByte = Base64.decode(input, 0)
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
    }

    // method for bitmap to base64
    fun encodeTobase64(image: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun ReadDataStore() {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        ImgFromStore = sharedPrefs.getString("profileImg", null)
        firstName = sharedPrefs.getString("firstName", "")
        lastName = sharedPrefs.getString("lastName", "")
        email = sharedPrefs.getString("email", "")
    }

    private fun setdefaultValue() {
        if (ImgFromStore == null) {
            fragmentProfileBinding.roundedImageView.setImageResource(R.drawable.profile_placeholder)
        } else {
            fragmentProfileBinding.roundedImageView.setImageBitmap(
                decodeBase64(
                    ImgFromStore
                )
            )
        }
        fragmentProfileBinding.editTextTextPersonName.setText(firstName)
        fragmentProfileBinding.editTextTextPersonName3.setText(email)
        fragmentProfileBinding.editTextTextPersonName2.setText(lastName)
    }
}
