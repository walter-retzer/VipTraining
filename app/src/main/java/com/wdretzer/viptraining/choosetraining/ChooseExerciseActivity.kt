package com.wdretzer.viptraining.choosetraining

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.datafirebase.ExerciseData
import com.wdretzer.viptraining.datafirebase.FirestoreData
import com.wdretzer.viptraining.firestore.ReadDataFromFirestoreActivity
import com.wdretzer.viptraining.inserttraining.InsertTrainingActivity
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

    var setNumberTraining: Int? = null
    var setNameTraining: String? = null
    var listExercise = mutableListOf<ExerciseData>()
    var exercise1: ExerciseData? = null
    var exercise2: ExerciseData? = null
    var exercise3: ExerciseData? = null
    var exercise4: ExerciseData? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_exercise)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()
        checkBundle()

        btnNext.setOnClickListener {

            if (checkBox1.isChecked || checkBox2.isChecked || checkBox3.isChecked || checkBox4.isChecked) {
                checkBundle()
                insertDataOnFirestore()
                sendToReadFirestore()
            } else {
                Toast.makeText(
                    this,
                    "Por favor selecione ao menos 1 exercicio!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    private fun insertDataOnFirestore() {
        val db = Firebase.firestore
        val training = db.collection("Treino")
        val dateTime: Date = Calendar.getInstance().time

        val documentFirestore =
            FirestoreData(setNumberTraining, setNameTraining, Timestamp.now(), listExercise)

        training.document(Timestamp.now().toString()).set(documentFirestore)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "DocumentSnapshot added with ID: $documentReference")
            }
            .addOnFailureListener { e ->
                Log.d("Firestore", "Error adding document", e)
            }
    }

    override fun onBackPressed() {
        val intent = Intent(this, InsertTrainingActivity::class.java)
        startActivity(intent)
    }

    private fun sendToReadFirestore() {
        Handler().postDelayed({
            val intent = Intent(this, ReadDataFromFirestoreActivity::class.java)
            startActivity(intent)
        }, 2000)
    }

    private fun checkBundle() {
        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            setNumberTraining = bundle.getInt("Number")
            setNameTraining = bundle.getString("Name")

            if (setNumberTraining == 1000) {
                checkBox1.text = "01 - Caminhada Leve por 40min"
                checkBox2.text = "02 - Caminhada e Corrida Leve por 40min"
                checkBox3.text = "03 - Pedalada Leve por 50min"
                checkBox4.text = "04 - Caminhada Leve na Esteira por 40min"

                if (checkBox1.isChecked) {
                    exercise1 = ExerciseData(1, null, checkBox1.text.toString())
                    listExercise.add(exercise1!!)
                }

                if (checkBox2.isChecked) {
                    exercise2 = ExerciseData(2, null, checkBox2.text.toString())
                    listExercise.add(exercise2!!)
                }

                if (checkBox3.isChecked) {
                    exercise3 = ExerciseData(3, null, checkBox3.text.toString())
                    listExercise.add(exercise3!!)
                }
                if (checkBox4.isChecked) {
                    exercise4 = ExerciseData(4, null, checkBox4.text.toString())
                    listExercise.add(exercise4!!)
                }
            }

            if (setNumberTraining == 2000) {
                checkBox1.text = "21 - Abdominal Reto"
                checkBox2.text = "22 - Abdominal bicicleta"
                checkBox3.text = "23 - Extensão de pernas na máquina"
                checkBox4.text = "24 - Puxada de triceps"

                if (checkBox1.isChecked) {
                    exercise1 = ExerciseData(21, null, checkBox1.text.toString())
                    listExercise.add(exercise1!!)
                }
                if (checkBox2.isChecked) {
                    exercise2 = ExerciseData(22, null, checkBox2.text.toString())
                    listExercise.add(exercise2!!)
                }
                if (checkBox3.isChecked) {
                    exercise3 = ExerciseData(23, null, checkBox3.text.toString())
                    listExercise.add(exercise3!!)
                }
                if (checkBox4.isChecked) {
                    exercise4 = ExerciseData(24, null, checkBox4.text.toString())
                    listExercise.add(exercise4!!)
                }
            }

            if (setNumberTraining == 3000) {
                checkBox1.text = "31 - Caminhada Leve por 30min"
                checkBox2.text = "32 - Flexão de Pernas"
                checkBox3.text = "33 - Flexão de Braços"
                checkBox4.text = "34 - Caminhada Leve na Esteira por 30min"

                if (checkBox1.isChecked) {
                    exercise1 = ExerciseData(31, null, checkBox1.text.toString())
                    listExercise.add(exercise1!!)
                }
                if (checkBox2.isChecked) {
                    exercise2 = ExerciseData(32, null, checkBox2.text.toString())
                    listExercise.add(exercise2!!)
                }
                if (checkBox3.isChecked) {
                    exercise3 = ExerciseData(33, null, checkBox3.text.toString())
                    listExercise.add(exercise3!!)
                }
                if (checkBox4.isChecked) {
                    exercise4 = ExerciseData(34, null, checkBox4.text.toString())
                    listExercise.add(exercise4!!)
                }
            }

            if (setNumberTraining == 4000) {
                checkBox1.text = "41 - Agachamento livre com barra"
                checkBox2.text = "42 - Elevação de paturilha sentado"
                checkBox3.text = "43 - Remada em polia baixa"
                checkBox4.text = "44 - Encolhimneto com barra"

                if (checkBox1.isChecked) {
                    exercise1 = ExerciseData(41, null, checkBox1.text.toString())
                    listExercise.add(exercise1!!)
                }
                if (checkBox2.isChecked) {
                    exercise2 = ExerciseData(42, null, checkBox2.text.toString())
                    listExercise.add(exercise2!!)
                }
                if (checkBox3.isChecked) {
                    exercise3 = ExerciseData(43, null, checkBox3.text.toString())
                    listExercise.add(exercise3!!)
                }
                if (checkBox4.isChecked) {
                    exercise4 = ExerciseData(44, null, checkBox4.text.toString())
                    listExercise.add(exercise4!!)
                }
            }
        }
    }

}

