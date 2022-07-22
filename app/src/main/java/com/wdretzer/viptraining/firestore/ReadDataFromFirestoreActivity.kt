package com.wdretzer.viptraining.firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.datafirebase.ExerciseData
import com.wdretzer.viptraining.datafirebase.FirestoreData


class ReadDataFromFirestoreActivity : AppCompatActivity() {

    private val btnTest: Button
        get() = findViewById(R.id.btn_test)

    private var listReturn = mutableListOf<FirestoreData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_data_from_firestore)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()

        btnTest.setOnClickListener { getDataFromFirestore() }
    }


    private fun getDataFromFirestore(): MutableList<FirestoreData> {
        val db = Firebase.firestore
        val docRef = db.collection("Treino")
        docRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val info = document.toObject<FirestoreData>()
                    listReturn.add(info)
                    Log.d("Item_Firestore", "${document.id} => ${document.data}")
                }
                Log.d("Read_Firestore", "$listReturn")
            }
            .addOnFailureListener { exception ->
                Log.d("Read_Firestore", "Fail to try read data from Firestore", exception)
            }
        return listReturn
    }


    private fun deleteDocumentInFirestore(date: Timestamp) {
        val db = Firebase.firestore
        db.collection("Treino").document("$date")
            .delete()
            .addOnSuccessListener { Log.d("Delete", "Document successfully deleted!") }
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
