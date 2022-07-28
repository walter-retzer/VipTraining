package com.wdretzer.viptraining.view.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.modeldata.extension.DataResult
import com.wdretzer.viptraining.view.menu.MainMenuActivity
import com.wdretzer.viptraining.view.util.SharedPrefVipTraining
import com.wdretzer.viptraining.view.extension.getImageUri
import com.wdretzer.viptraining.viewmodel.VipTrainingViewModel


class MyProfileActivity : AppCompatActivity() {

    private val imageName = "training-${System.currentTimeMillis()}"
    private lateinit var uriImage: Uri

    private val cameraCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val image = result.data

                image?.extras?.get("data")?.let { photo ->
                    imageProfile.setImageBitmap(photo as Bitmap)
                    val uri = getImageUri(this, photo, imageName)
                    uriImage = uri
                }
            }
        }

    private val cameraPermissionCallback =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }

    private val galleryCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uriImage = result.data?.data!!
                imageProfile.setImageURI(uriImage)
            }
        }

    private val imageProfile: ShapeableImageView
        get() = findViewById(R.id.image_profile)

    private val btnCamerae: ShapeableImageView
        get() = findViewById(R.id.btn_camera)

    private val textName: TextView
        get() = findViewById(R.id.text_name_user)

    private val textBirthday: TextInputEditText
        get() = findViewById(R.id.input_birthday)

    private val textWeigth: TextInputEditText
        get() = findViewById(R.id.input_weight)

    private val textHeigth: TextInputEditText
        get() = findViewById(R.id.input_height)

    private val btnSaveProfile: Button
        get() = findViewById(R.id.btn_save_profile)

    private val progressBar: FrameLayout
        get() = findViewById(R.id.progress_bar_login)

    private val viewModel: VipTrainingViewModel by viewModels()

    private val sharedPref: SharedPrefVipTraining = SharedPrefVipTraining.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()

        cameraPermissionCallback.launch(Manifest.permission.CAMERA)

        checkUserName()
        checkButtonClickListeners()
    }

    private fun checkButtonClickListeners() {
        btnSaveProfile.setOnClickListener {
            updateUserPhoto()
            uriImage?.let { updatePhotoFirebase(it, "$imageName.jpg") }

            saveName(textName.text.toString())
            saveBirthday(textBirthday.text.toString())
            saveWeight(textWeigth.text.toString())
            saveHeight(textHeigth.text.toString())
            saveImg(uriImage.toString())

            Toast.makeText(this, "Dados Salvos!", Toast.LENGTH_SHORT).show()
            sendToMainMenu()
        }

        imageProfile.setOnClickListener { dialogPhoto(it.context) }
        btnCamerae.setOnClickListener { dialogPhoto(it.context) }
    }


    private fun checkUserName() {
        viewModel.checkUserName().observe(this) {
            if (it is DataResult.Loading) {
                progressBar.isVisible = it.isLoading
            }

            if (it is DataResult.Warning) {
                textName.text = it.message
            }
        }
    }


    private fun updateUserPhoto() {
        viewModel.updateUserPhoto(uriImage.toString()).observe(this) {
            if (it is DataResult.Loading) {
                progressBar.isVisible = it.isLoading
            }

            if (it is DataResult.Success) {
                Toast.makeText(this, "Update Photo to Authentication!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun updatePhotoFirebase(uri: Uri, imageName: String) {
        viewModel.uploadFileToFirebaseStorage(uri, imageName, "Profile").observe(this) {
            if (it is DataResult.Loading) {
                progressBar.isVisible = it.isLoading
            }

            if (it is DataResult.Success) {
                Toast.makeText(this, "Update Photo In Firebase Storage!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun saveName(name: String) {
        sharedPref.saveString("Name", name)
    }

    private fun saveImg(img: String) {
        sharedPref.saveString("Img", img)
    }

    private fun saveBirthday(name: String) {
        sharedPref.saveString("Date", name)
    }

    private fun saveWeight(weight: String) {
        sharedPref.saveString("Weight", weight)
    }

    private fun saveHeight(height: String) {
        sharedPref.saveString("Height", height)
    }

    private fun sendToMainMenu() {
        Handler().postDelayed({
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }, 2000)
    }

    private fun getFromCamera() {
        val intent = Intent().apply {
            action = MediaStore.ACTION_IMAGE_CAPTURE
        }
        cameraCallback.launch(intent)
    }

    private fun getFromGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_PICK
            type = "image/*"
        }
        galleryCallback.launch(intent)
    }


    private fun dialogPhoto(context: Context) {
        val items = arrayOf("Tirar foto", "Buscar na Galeria")
        AlertDialog
            .Builder(context)
            .setTitle("Qual você deseja usar?")
            .setItems(items) { dialog, index ->
                when (index) {
                    0 -> getFromCamera()
                    1 -> getFromGallery()
                }
                dialog.dismiss()
            }.show()
    }
}
