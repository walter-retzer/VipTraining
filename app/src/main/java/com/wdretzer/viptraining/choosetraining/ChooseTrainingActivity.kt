package com.wdretzer.viptraining.choosetraining

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.inserttraining.InsertTrainingActivity


class ChooseTrainingActivity : AppCompatActivity() {

    private val textWorkoutList: AppCompatAutoCompleteTextView
        get() = findViewById(R.id.input_name_workout)

    private val btnNext: Button
        get() = findViewById(R.id.btn_next_info)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_training)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()

        // Desabilita a abertura do Teclado quando o item for clicado:
        textWorkoutList.showSoftInputOnFocus = false
        textWorkoutList.isCursorVisible = false

        checkTraining()

        // get reference to the string array that we just created
        val languages = resources.getStringArray(R.array.training_types)
        val arrayAdapter = ArrayAdapter(this, R.layout.list_item, languages)
        textWorkoutList.setAdapter(arrayAdapter)
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

                sendToChooseExercise(textWorkoutList.text.toString(), numberTraining)
            }
        }
    }

    private fun sendToChooseExercise(
        trainingName: String? = null,
        trainingNumber: Int? = null,
    ) {
        val intent = Intent(this, ChooseExerciseActivity::class.java).apply {
            putExtra("Name", trainingName)
            putExtra("Number", trainingNumber)
        }
        startActivity(intent)
    }
}
