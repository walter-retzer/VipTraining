package com.wdretzer.viptraining.view.profile

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.modeldata.extension.DataResult
import com.wdretzer.viptraining.view.menu.MainMenuActivity
import com.wdretzer.viptraining.view.util.SharedPrefVipTraining
import com.wdretzer.viptraining.view.extension.getImageUri
import com.wdretzer.viptraining.viewmodel.VipTrainingViewModel


// Classe responsável por Editar Dados do Perfil do Usuário:
class EditProfileActivity : AppCompatActivity() {

    private val imageName = "training-${System.currentTimeMillis()}"
    private lateinit var uriImage: Uri

    // variavel responsável pelo uso da câmera
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

    // variavel responsável pelo uso da galeria de fotos
    private val galleryCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uriImage = result.data?.data!!
                imageProfile.setImageURI(uriImage)
            }
        }

    private val imageProfile: ShapeableImageView
        get() = findViewById(R.id.image_edit_profile)

    private val btnCamerae: ShapeableImageView
        get() = findViewById(R.id.btn_camera)

    private val textName: TextView
        get() = findViewById(R.id.text_name_edit_user)

    private val textBirthday: TextInputEditText
        get() = findViewById(R.id.input_edit_birthday)

    private val textWeigth: TextInputEditText
        get() = findViewById(R.id.input_edit_weight)

    private val textHeigth: TextInputEditText
        get() = findViewById(R.id.input_edit_height)

    private val btnSaveProfile: Button
        get() = findViewById(R.id.btn_edit_profile)

    private val progressBar: FrameLayout
        get() = findViewById(R.id.progress_bar_login)

    private val viewModel: VipTrainingViewModel by viewModels()
    private val sharedPref: SharedPrefVipTraining = SharedPrefVipTraining.instance


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()

        checkUser()
        checkReadPreferences()
        checkClickListeners()
    }


    // Método responsável por checar clicks:
    private fun checkClickListeners() {
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


    // Método responsável pela leitura dos itens: Altura, Peso, Img... salvos no SharedPreferences:
    private fun checkReadPreferences() {
        try {
            textBirthday.setText(sharedPref.readString("Date"))
            textWeigth.setText(sharedPref.readString("Weight"))
            textHeigth.setText(sharedPref.readString("Height"))

            val uri = Uri.parse(sharedPref.readString("Img"))
            imageProfile.setImageURI(uri)

        } catch (e: IllegalArgumentException) {
            Log.e("Shared_Pref:", "Error: $e")
        }
    }


    // Método responsável pela ataulização do nome do usuário na tela:
    private fun checkUser() {
        viewModel.checkUserName().observe(this) {
            if (it is DataResult.Loading) {
                progressBar.isVisible = it.isLoading
            }

            if (it is DataResult.Warning) {
                textName.text = it.message
            }
        }
    }


    // Método responsável pela atualização da foto do usuário na tela:
    private fun updatePhotoFirebase(uri: Uri, imageName: String) {
        viewModel.uploadFileToFirebaseStorage(uri, imageName, "Profile").observe(this) {
            if (it is DataResult.Loading) {
                progressBar.isVisible = it.isLoading
            }

            if (it is DataResult.Success) {
                Log.i("Firestore", "Update Photo In Firebase Storage!")
            }
        }
    }


    // Método responsável pela atualização da foto do usuário no Firebase Authentication:
    private fun updateUserPhoto() {
        viewModel.updateUserPhoto(uriImage.toString()).observe(this) {
            if (it is DataResult.Loading) {
                progressBar.isVisible = it.isLoading
            }

            if (it is DataResult.Success) {
                Log.i("Firestore", "Update Photo to Authentication!")
            }
        }
    }


    // Método responsável por iniciar a Activity: MainMenuActivity
    private fun sendToMainMenu() {
        Handler().postDelayed({
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }, 2000)
    }


    // Método responsável por iniciar o dialog com as opçãoes: Tirar foto e Galeria
    private fun dialogPhoto(context: Context) {
        val items = arrayOf("Tirar foto", "Buscar na Galeria")
        AlertDialog
            .Builder(context)
            .setTitle("Qual você deseja usar?")
            .setItems(items) { dialog, index ->
                when (index) {
                    0 -> getFromCamera(context)
                    1 -> getFromGallery()
                }
                dialog.dismiss()
            }.show()
    }


    // Método responsável por iniciar a Câmera:
    private fun getFromCamera(context: Context) {
        val permission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

        if (permission == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent().apply {
                action = MediaStore.ACTION_IMAGE_CAPTURE
            }
            cameraCallback.launch(intent)
        }
    }


    // Método responsável por iniciar a Galeria de Fotos:
    private fun getFromGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_PICK
            type = "image/*"
        }
        galleryCallback.launch(intent)
    }

    // Método responsável por salvar o nome do usuário no Shared Preferences:
    private fun saveName(name: String) {
        sharedPref.saveString("Name", name)
    }

    // Método responsável por salvar a Imagem do usuário no Shared Preferences:
    private fun saveImg(img: String) {
        sharedPref.saveString("Img", img)
    }

    // Método responsável por salvar a data de nascimento do usuário no Shared Preferences:
    private fun saveBirthday(name: String) {
        sharedPref.saveString("Date", name)
    }

    // Método responsável por salvar o peso do usuário no Shared Preferences:
    private fun saveWeight(weight: String) {
        sharedPref.saveString("Weight", weight)
    }

    // Método responsável por salvar a altura do usuário no Shared Preferences:
    private fun saveHeight(height: String) {
        sharedPref.saveString("Height", height)
    }
}
