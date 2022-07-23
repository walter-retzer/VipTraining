package com.wdretzer.viptraining.firestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.datafirebase.ExerciseData
import com.wdretzer.viptraining.datafirebase.FirestoreData
import com.wdretzer.viptraining.inserttraining.InsertTrainingActivity


class ReadDataFromFirestoreActivity : AppCompatActivity() {

    private val btnTest: Button
        get() = findViewById(R.id.btn_test)

    private val recycler: RecyclerView
        get() = findViewById(R.id.firestore_recycler)

    private var listReturn = mutableListOf<FirestoreData>()
    private var adp = DataFromFirestoreAdapter(::sendDataToDelete) { }

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


    private fun getDataFromFirestore() {
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
            }
            .addOnFailureListener { exception ->
                Log.d("Read_Firestore", "Fail to try read data from Firestore", exception)
            }
    }


    private fun deleteDocumentInFirestore(item: FirestoreData) {
        val db = Firebase.firestore
        db.collection("Treino").document("${item.data}")
            .delete()
            .addOnSuccessListener {
                adp.updateItem(item)
                Log.d("Delete", "Document successfully deleted!") }
            .addOnFailureListener { e -> Log.w("Delete", "Error deleting document", e) }
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
