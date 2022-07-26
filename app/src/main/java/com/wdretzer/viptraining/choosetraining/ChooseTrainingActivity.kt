package com.wdretzer.viptraining.choosetraining

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.content.ContextCompat
import com.google.android.material.imageview.ShapeableImageView
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.extension.SaveFile
import com.wdretzer.viptraining.inserttraining.InsertTrainingActivity
import java.io.ByteArrayOutputStream
import java.lang.System.currentTimeMillis


class ChooseTrainingActivity : AppCompatActivity() {

    private val imageName = "training-${currentTimeMillis()}"

    private val cameraCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val image = result.data

                image?.extras?.get("data")?.let { photo ->
                    imageTraining.setImageBitmap(photo as Bitmap)
                    val uri = getImageUri(this, photo)
                    uriImage = uri
                    SaveFile(this, photo).saveAndShare()
                }
            }
        }

    private val galleryCallback =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                uriImage = result.data?.data
                imageTraining.setImageURI(uriImage)
            }
        }

    private val textWorkoutList: AppCompatAutoCompleteTextView
        get() = findViewById(R.id.input_name_workout)

    private val btnNext: Button
        get() = findViewById(R.id.btn_next_info)

    private val imageTraining: ShapeableImageView
        get() = findViewById(R.id.image_choose_training)

    private val btnCamera: ShapeableImageView
        get() = findViewById(R.id.btn_camera)

    private var uriImage: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_training)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()

        // get reference to the string array that we just created
        val languages = resources.getStringArray(R.array.training_types)
        val arrayAdapter = ArrayAdapter(this, R.layout.list_item, languages)

        // Desabilita a abertura do Teclado quando o item for clicado:
        textWorkoutList.showSoftInputOnFocus = false
        textWorkoutList.isCursorVisible = false
        textWorkoutList.setAdapter(arrayAdapter)

        checkTraining()

        imageTraining.setOnClickListener { dialogPhoto(it.context) }
        btnCamera.setOnClickListener { dialogPhoto(it.context) }
    }


    override fun onBackPressed() {
        val intent = Intent(this, InsertTrainingActivity::class.java)
        startActivity(intent)
    }


    private fun checkTraining() {
        var numberTraining = 0

        btnNext.setOnClickListener {
            if (textWorkoutList.text.isEmpty()) {
                Toast.makeText(this, "Por favor escolha um Treino!", Toast.LENGTH_LONG).show()
            } else {
                if (textWorkoutList.text.toString() == "Perda de Peso") numberTraining = 1000
                if (textWorkoutList.text.toString() == "Melhorar a forma Física") numberTraining =
                    2000
                if (textWorkoutList.text.toString() == "Sair do Sedentarismo") numberTraining = 3000
                if (textWorkoutList.text.toString() == "Ganhar Músculos") numberTraining = 4000

                sendToChooseExercise(
                    textWorkoutList.text.toString(),
                    numberTraining,
                    uriImage.toString()
                )
            }
        }
    }


    private fun sendToChooseExercise(
        trainingName: String? = null,
        trainingNumber: Int? = null,
        trainingUri: String? = null,
    ) {
        val intent = Intent(this, ChooseExerciseActivity::class.java).apply {
            putExtra("Name", trainingName)
            putExtra("Number", trainingNumber)
            putExtra("Uri", trainingUri)
        }
        startActivity(intent)
    }


    private fun getFromCamera(context: Context) {
        val permission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)

        if (permission == PackageManager.PERMISSION_DENIED) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 0
            )

            val intent = Intent().apply {
                action = MediaStore.ACTION_IMAGE_CAPTURE
            }
            cameraCallback.launch(intent)
        }

        if (permission == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent().apply {
                action = MediaStore.ACTION_IMAGE_CAPTURE
            }
            cameraCallback.launch(intent)
        }
    }


    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, imageName, null)
        return Uri.parse(path)
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
                    0 -> getFromCamera(context)
                    1 -> getFromGallery()
                }
                dialog.dismiss()
            }.show()
    }
}
