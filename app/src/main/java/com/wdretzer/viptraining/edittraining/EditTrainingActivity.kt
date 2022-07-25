package com.wdretzer.viptraining.edittraining

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.view.isVisible
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.datafirebase.ExerciseData
import com.wdretzer.viptraining.datafirebase.FirestoreData
import com.wdretzer.viptraining.firestore.ReadDataFromFirestoreActivity
import com.wdretzer.viptraining.inserttraining.InsertTrainingActivity


class EditTrainingActivity : AppCompatActivity() {

    private val btnEdit: Button
        get() = findViewById(R.id.btn_edit)

    private val textWorkoutName: TextView
        get() = findViewById(R.id.text_name_training)

    private val checkBox1: CheckBox
        get() = findViewById(R.id.checkbox_exercise1_edit)

    private val checkBox2: CheckBox
        get() = findViewById(R.id.checkbox_exercise2_edit)

    private val checkBox3: CheckBox
        get() = findViewById(R.id.checkbox_exercise3_edit)

    private val checkBox4: CheckBox
        get() = findViewById(R.id.checkbox_exercise4_edit)

    private val loading: FrameLayout
        get() = findViewById(R.id.loading)

    private var listExercise = mutableListOf<ExerciseData>()
    private lateinit var exercise1: ExerciseData
    private lateinit var exercise2: ExerciseData
    private lateinit var exercise3: ExerciseData
    private lateinit var exercise4: ExerciseData
    private lateinit var item: FirestoreData


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_training)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()

        item = intent.getParcelableExtra("Item")!!
        itemsToEdit()

        btnEdit.setOnClickListener {
            btnEdit.isVisible = false
            loading.isVisible = true
            checkItems(item)
            updateDocumentInFirestore(item)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, InsertTrainingActivity::class.java)
        startActivity(intent)
    }


    private fun sendToReadDataFromFirestore() {
        Toast.makeText(this, "Dados atualizados!", Toast.LENGTH_SHORT).show()
        btnEdit.isVisible = true
        val intent = Intent(this, ReadDataFromFirestoreActivity::class.java)
        startActivity(intent)
    }

    private fun itemsToEdit() {
        textWorkoutName.text = item.descricao
        if (item.status?.checkbox1 == true) checkBox1.isChecked = true
        if (item.status?.checkbox2 == true) checkBox2.isChecked = true
        if (item.status?.checkbox3 == true) checkBox3.isChecked = true
        if (item.status?.checkbox4 == true) checkBox4.isChecked = true

        if (item.name == 1000) {
            checkBox1.text = "Caminhada Leve por 40min"
            checkBox2.text = "Caminhada e Corrida Leve por 40min"
            checkBox3.text = "Pedalada Leve por 50min"
            checkBox4.text = "Caminhada Leve na Esteira por 40min"
        }

        if (item.name == 2000) {
            checkBox1.text = "Abdominal Reto"
            checkBox2.text = "Abdominal bicicleta"
            checkBox3.text = "Extensão de pernas na máquina"
            checkBox4.text = "Puxada de triceps"
        }

        if (item.name == 3000) {
            checkBox1.text = "Caminhada Leve por 30min"
            checkBox2.text = "Flexão de Pernas"
            checkBox3.text = "Flexão de Braços"
            checkBox4.text = "Caminhada Leve na Esteira por 30min"
        }

        if (item.name == 4000) {
            checkBox1.text = "Agachamento livre com barra"
            checkBox2.text = "Elevação de paturilha sentado"
            checkBox3.text = "Remada em polia baixa"
            checkBox4.text = "Encolhimneto com barra"
        }
    }

    private fun checkItems(item: FirestoreData) {
        if (checkBox1.isChecked) {
            exercise1 = ExerciseData(item.name, null, checkBox1.text.toString())
            listExercise.add(exercise1)
        }

        if (checkBox2.isChecked) {
            exercise2 = ExerciseData(item.name, null, checkBox2.text.toString())
            listExercise.add(exercise2)
        }

        if (checkBox3.isChecked) {
            exercise3 = ExerciseData(item.name, null, checkBox3.text.toString())
            listExercise.add(exercise3)
        }

        if (checkBox4.isChecked) {
            exercise4 = ExerciseData(item.name, null, checkBox4.text.toString())
            listExercise.add(exercise4)
        }
    }

    private fun updateDocumentInFirestore(item: FirestoreData) {
        val db = Firebase.firestore
        val docRef = db.collection("Treino").document("${item.data}")
        docRef.update("listExercise", listExercise)
            .addOnSuccessListener {
                Log.d("Update_Firestore", "Document successfully updated!")
                loading.isVisible = false
                sendToReadDataFromFirestore()
            }
            .addOnFailureListener { e ->
                Log.w("Update_Firestore", "Error updating document", e)
                loading.isVisible = false
                btnEdit.isVisible = true
            }
    }
}
