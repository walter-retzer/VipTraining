package com.wdretzer.viptraining.choosetraining

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.data.extension.DataResult
import com.wdretzer.viptraining.datafirebase.CheckBoxStatus
import com.wdretzer.viptraining.datafirebase.ExerciseData
import com.wdretzer.viptraining.datafirebase.FirestoreData
import com.wdretzer.viptraining.firestore.ReadDataFromFirestoreActivity
import com.wdretzer.viptraining.mainmenu.MainMenuActivity
import com.wdretzer.viptraining.viewmodel.VipTrainingViewModel
import java.io.File
import java.io.FileNotFoundException
import java.io.FileWriter
import java.io.IOException
import java.util.*


class ChooseExerciseActivity : AppCompatActivity() {

    private val btnNext: Button
        get() = findViewById(R.id.btn_next_action)

    private val checkBox1: CheckBox
        get() = findViewById(R.id.checkbox_exercise1)

    private val checkBox2: CheckBox
        get() = findViewById(R.id.checkbox_exercise2)

    private val checkBox3: CheckBox
        get() = findViewById(R.id.checkbox_exercise3)

    private val checkBox4: CheckBox
        get() = findViewById(R.id.checkbox_exercise4)

    private val loading: FrameLayout
        get() = findViewById(R.id.loading)

    private val viewModel: VipTrainingViewModel by viewModels()

    private val imageName = "training-${System.currentTimeMillis()}"

    private lateinit var exercise1: ExerciseData
    private lateinit var exercise2: ExerciseData
    private lateinit var exercise3: ExerciseData
    private lateinit var exercise4: ExerciseData
    private lateinit var setNameTraining: String
    private var setNumberTraining: Int = 1000
    private var setUriImageTraining: String? = null
    private var listExercise = mutableListOf<ExerciseData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_exercise)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()
        checkBundle()
        btnNext.setOnClickListener { checkItems() }
    }

    private fun checkItems() {
        if (checkBox1.isChecked || checkBox2.isChecked || checkBox3.isChecked || checkBox4.isChecked) {
            loading.isVisible = true
            btnNext.isVisible = false

            checkBundle()
            insertDataOnFirestore()
            sendToReadFirestore()
            uploadToFirebase(saveFile())

            setUriImageTraining?.let {
                val uri = Uri.parse(setUriImageTraining)
                updatePhotoFirebase(uri, imageName)
            }

        } else {
            Toast.makeText(
                this,
                "Por favor selecione ao menos 1 exercício!",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun insertDataOnFirestore() {
        val db = Firebase.firestore
        val training = db.collection("Treino")

        val statusCheckBox = CheckBoxStatus(
            checkBox1.isChecked,
            checkBox2.isChecked,
            checkBox3.isChecked,
            checkBox4.isChecked
        )

        val documentFirestore =
            FirestoreData(
                setNumberTraining,
                setNameTraining,
                Timestamp.now(),
                listExercise,
                statusCheckBox
            )

        training.document(Timestamp.now().toString()).set(documentFirestore)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "DocumentSnapshot added with ID: $documentReference")
                loading.isVisible = false
            }
            .addOnFailureListener { e ->
                Log.d("Firestore", "Error adding document", e)
                loading.isVisible = false
                btnNext.isVisible = true
            }
    }

    override fun onBackPressed() {
        val intent = Intent(this, ChooseTrainingActivity::class.java)
        startActivity(intent)
    }


    private fun sendToReadFirestore() {
        btnNext.isVisible = true
        val intent = Intent(this, ReadDataFromFirestoreActivity::class.java)
        startActivity(intent)
    }


    private fun checkBundle() {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            setNumberTraining = bundle.getInt("Number")
            setNameTraining = bundle.getString("Name").toString()
            setUriImageTraining = bundle.getString("Uri").toString()

            if (setNumberTraining == 1000) {
                checkBox1.text = "Caminhada Leve por 40min"
                checkBox2.text = "Caminhada e Corrida Leve por 40min"
                checkBox3.text = "Pedalada Leve por 50min"
                checkBox4.text = "Caminhada Leve na Esteira por 40min"

                if (checkBox1.isChecked) {
                    exercise1 = ExerciseData(1, setUriImageTraining, checkBox1.text.toString())
                    listExercise.add(exercise1)
                }

                if (checkBox2.isChecked) {
                    exercise2 = ExerciseData(2, setUriImageTraining, checkBox2.text.toString())
                    listExercise.add(exercise2)
                }

                if (checkBox3.isChecked) {
                    exercise3 = ExerciseData(3, setUriImageTraining, checkBox3.text.toString())
                    listExercise.add(exercise3)
                }

                if (checkBox4.isChecked) {
                    exercise4 = ExerciseData(4, setUriImageTraining, checkBox4.text.toString())
                    listExercise.add(exercise4)
                }
            }

            if (setNumberTraining == 2000) {
                checkBox1.text = "Abdominal Reto"
                checkBox2.text = "Abdominal bicicleta"
                checkBox3.text = "Extensão de pernas na máquina"
                checkBox4.text = "Puxada de triceps"

                if (checkBox1.isChecked) {
                    exercise1 = ExerciseData(21, setUriImageTraining, checkBox1.text.toString())
                    listExercise.add(exercise1)

                }
                if (checkBox2.isChecked) {
                    exercise2 = ExerciseData(22, setUriImageTraining, checkBox2.text.toString())
                    listExercise.add(exercise2)

                }
                if (checkBox3.isChecked) {
                    exercise3 = ExerciseData(23, setUriImageTraining, checkBox3.text.toString())
                    listExercise.add(exercise3)

                }
                if (checkBox4.isChecked) {
                    exercise4 = ExerciseData(24, setUriImageTraining, checkBox4.text.toString())
                    listExercise.add(exercise4)

                }
            }

            if (setNumberTraining == 3000) {
                checkBox1.text = "Caminhada Leve por 30min"
                checkBox2.text = "Flexão de Pernas"
                checkBox3.text = "Flexão de Braços"
                checkBox4.text = "Caminhada Leve na Esteira por 30min"

                if (checkBox1.isChecked) {
                    exercise1 = ExerciseData(31, setUriImageTraining, checkBox1.text.toString())
                    listExercise.add(exercise1)

                }
                if (checkBox2.isChecked) {
                    exercise2 = ExerciseData(32, setUriImageTraining, checkBox2.text.toString())
                    listExercise.add(exercise2)

                }
                if (checkBox3.isChecked) {
                    exercise3 = ExerciseData(33, setUriImageTraining, checkBox3.text.toString())
                    listExercise.add(exercise3)

                }
                if (checkBox4.isChecked) {
                    exercise4 = ExerciseData(34, setUriImageTraining, checkBox4.text.toString())
                    listExercise.add(exercise4)

                }
            }

            if (setNumberTraining == 4000) {
                checkBox1.text = "Agachamento livre com barra"
                checkBox2.text = "Elevação de paturilha sentado"
                checkBox3.text = "Remada em polia baixa"
                checkBox4.text = "Encolhimneto com barra"

                if (checkBox1.isChecked) {
                    exercise1 = ExerciseData(41, setUriImageTraining, checkBox1.text.toString())
                    listExercise.add(exercise1)
                }

                if (checkBox2.isChecked) {
                    exercise2 = ExerciseData(42, setUriImageTraining, checkBox2.text.toString())
                    listExercise.add(exercise2)
                }

                if (checkBox3.isChecked) {
                    exercise3 = ExerciseData(43, setUriImageTraining, checkBox3.text.toString())
                    listExercise.add(exercise3)
                }

                if (checkBox4.isChecked) {
                    exercise4 = ExerciseData(44, setUriImageTraining, checkBox4.text.toString())
                    listExercise.add(exercise4)
                }
            }
        }
    }

    private fun updatePhotoFirebase(uri: Uri, imageName: String) {
        viewModel.uploadPhotoProfileToFirebaseStorage(uri, imageName, "Image").observe(this) {
            if (it is DataResult.Loading) {
                loading.isVisible = it.isLoading
            }

            if (it is DataResult.Success) {
                Toast.makeText(this, "Update Photo In Firebase Storage!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun uploadToFirebase(uri: Uri) {

        val firebaseStorage = FirebaseStorage.getInstance()
        val storage = firebaseStorage.getReference("Documents")
        val fileReference = storage.child("image_list.txt")

        uri.apply {
            fileReference
                .putFile(this)
                .addOnSuccessListener {
                    Log.d("Firebase Storage:", "Arquivo Enviado ao Firebase Storage com sucesso!")
                }
                .addOnFailureListener {
                    Log.d("Firebase Storage:", "rquivo Não Enviado ao Firebase Storage!")
                }
        }
    }


    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun saveFile(): Uri {
        val file = getDisc()

        if (!file.exists() && !file.mkdirs()) {
            file.mkdir()
        }

        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy_HH.mm.ss")
        val date = simpleDateFormat.format(Date())
        val name = "img.txt"
        val fileName = file.absolutePath + "/" + name
        val newFile = File(fileName)

        try {
            val fileWriter = FileWriter(newFile, true)
            newFile.appendText("Data: $date; Treino: $setNameTraining; Imagem: $setUriImageTraining \n")
            fileWriter.flush()
            fileWriter.close()

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            Log.d("Firebase Storage:", "Arquivo para o Firebase Storage Inexistente!")

        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("Firebase Storage:", "Falha ao Salvar o Arquivo!")
        }

        return Uri.parse(newFile.toUri().toString())
    }


    private fun getDisc(): File {
        return File(this.externalCacheDir!!.absolutePath, "/VipImage")
    }

}
