package com.wdretzer.viptraining.inserttraining

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wdretzer.nasaprojetointegrador.util.HorizontalMarginItemDecoration
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.choosetraining.ChooseTrainingActivity
import com.wdretzer.viptraining.createaccount.CreateUserAccountActivity
import com.wdretzer.viptraining.datafirebase.ExerciseData
import com.wdretzer.viptraining.datafirebase.FirestoreData
import com.wdretzer.viptraining.onboarding.Onboarding1Fragment
import com.wdretzer.viptraining.onboarding.Onboarding2Fragment
import com.wdretzer.viptraining.onboarding.Onboarding3Fragment
import com.wdretzer.viptraining.onboarding.OnboardingScreenAdapter


class InsertTrainingActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy { findViewById(R.id.view_pager_insert_training) }

    private val btnNext: Button
        get() = findViewById(R.id.btn_insert)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_training)

        //insertDataOnFirestore()

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()
        setupViewPager()
        btnNext.setOnClickListener { sendToChooseTraining() }

    }

    private fun sendToChooseTraining() {
        val intent = Intent(this, ChooseTrainingActivity::class.java)
        startActivity(intent)
    }


    private fun insertDataOnFirestore() {

        val db = Firebase.firestore
        val trainingDocumentRef = db.collection("treino").document("exercicios")


        val exercise = ExerciseData(1000, null, "N/A")
        val exercise2 = ExerciseData(1009, null, "Ok")
        val timeFirebase = Timestamp.now()

        val data =
            FirestoreData(0, "Treino Master", timeFirebase, mutableListOf(exercise, exercise2))

        trainingDocumentRef.collection("treino")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Log.d("Firebase", "DocumentSnapshot added with ID: ${documentReference.id}")
                Toast.makeText(this, "Dados enviados ao Firestore!!", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener { e ->
                Log.w("Firebase", "Error adding document", e)
            }

    }

    private fun setupViewPager() {
        val listFragments = listOf(
            TrainingType1Fragment(),
            TrainingType1Fragment(),
            TrainingType1Fragment(),
        )

        viewPager.adapter = OnboardingScreenAdapter(
            this, listFragments
        )

    }
}