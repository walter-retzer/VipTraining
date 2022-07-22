package com.wdretzer.viptraining.firestore

import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.datafirebase.FirestoreData


class ReadDataFromFirestoreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_data_from_firestore)

        insertDataOnFirestore()
    }


    private fun insertDataOnFirestore() {
        val db = Firebase.firestore
        val docRef = db.collection("Treino")
        val listReturn = mutableListOf<FirestoreData>()
        docRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val info = document.toObject<FirestoreData>()
                    listReturn.add(info)
                    Log.d("TAG", "${document.id} => ${document.data}")
                }
                Log.d("Dados", "$listReturn")
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }
}
