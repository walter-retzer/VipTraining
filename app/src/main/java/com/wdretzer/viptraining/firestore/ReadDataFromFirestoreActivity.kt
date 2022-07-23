package com.wdretzer.viptraining.firestore

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.datafirebase.ExerciseData
import com.wdretzer.viptraining.datafirebase.FirestoreData
import com.wdretzer.viptraining.edittraining.EditTrainingActivity
import com.wdretzer.viptraining.inserttraining.InsertTrainingActivity


class ReadDataFromFirestoreActivity : AppCompatActivity() {

    private val btnTest: ShapeableImageView
        get() = findViewById(R.id.btn_home)

    private val recycler: RecyclerView
        get() = findViewById(R.id.firestore_recycler)

    private val loading: FrameLayout
        get() = findViewById(R.id.loading)

    private var listReturn = mutableListOf<FirestoreData>()
    private var adp = DataFromFirestoreAdapter(::sendDataToDelete) {
        sendToEditTraining(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_data_from_firestore)

        getDataFromFirestore()

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()
        btnTest.setOnClickListener { }
    }


    override fun onBackPressed() {
        val intent = Intent(this, InsertTrainingActivity::class.java)
        startActivity(intent)
    }


    private fun sendDataToDelete(item: FirestoreData) {
        deleteDocumentInFirestore(item)
    }

    private fun sendToEditTraining(item: FirestoreData) {
        val intent = Intent(this, EditTrainingActivity::class.java)
        intent.putExtra("Item", item)
        startActivity(intent)
    }


    private fun getDataFromFirestore() {
        loading.isVisible = true
        val db = Firebase.firestore
        val docRef = db.collection("Treino")
        docRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val info = document.toObject<FirestoreData>()
                    listReturn.add(info)
                    Log.d("Item_Firestore", "${document.id} => ${document.data}")
                }
                adp.updateList(listReturn)
                recycler.adapter = adp
                recycler.layoutManager = LinearLayoutManager(this)

                Log.d("Read_Firestore", "$listReturn")
                loading.isVisible = false
            }
            .addOnFailureListener { exception ->
                Log.d("Read_Firestore", "Fail to try read data from Firestore", exception)
                loading.isVisible = false
            }
    }


    private fun deleteDocumentInFirestore(item: FirestoreData) {
        loading.isVisible = true
        val db = Firebase.firestore
        db.collection("Treino").document("${item.data}")
            .delete()
            .addOnSuccessListener {
                adp.updateItem(item)
                Log.d("Delete", "Document successfully deleted!")
                loading.isVisible = false
            }
            .addOnFailureListener { e ->
                Log.w("Delete", "Error deleting document", e)
                loading.isVisible = false
            }
    }


    private fun deleteItemInFirestore(date: Timestamp) {
        val db = Firebase.firestore
        val docRef = db.collection("Treino").document("$date")
        val updates = hashMapOf<String, Any>(
            "descricao" to FieldValue.delete()
        )
        docRef.update(updates)
            .addOnCompleteListener { Log.d("Del_Firestore", "Item successfully delete!") }
            .addOnFailureListener { e -> Log.w("Del_Firestore", "Error deleting document", e) }
    }


    private fun updateDocumentInFirestore(date: Timestamp) {
        val db = Firebase.firestore
        val docRef = db.collection("Treino").document("$date")
        val exercise: ExerciseData? = null
        docRef.update("listExercise", mutableListOf(exercise))
            .addOnSuccessListener { Log.d("Update_Firestore", "Document successfully updated!") }
            .addOnFailureListener { e -> Log.w("Update_Firestore", "Error updating document", e) }
    }
}
