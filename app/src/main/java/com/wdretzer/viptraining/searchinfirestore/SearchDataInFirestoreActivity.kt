package com.wdretzer.viptraining.searchinfirestore

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.datafirebase.FirestoreData
import com.wdretzer.viptraining.edittraining.EditTrainingActivity
import com.wdretzer.viptraining.firestore.DataFromFirestoreAdapter
import com.wdretzer.viptraining.firestore.ReadDataFromFirestoreActivity
import com.wdretzer.viptraining.mainmenu.MainMenuActivity


class SearchDataInFirestoreActivity : AppCompatActivity() {

    private val textWorkoutList: AppCompatAutoCompleteTextView
        get() = findViewById(R.id.search_name_workout)

    private val btnSearch: Button
        get() = findViewById(R.id.btn_search)

    private val recycler: RecyclerView
        get() = findViewById(R.id.firestore_recycler_search)

    private val loading: FrameLayout
        get() = findViewById(R.id.loading)

    private val btnReturn: ShapeableImageView
        get() = findViewById(R.id.btn_return)

    private var listReturn = mutableListOf<FirestoreData>()
    private var listReturnAux = mutableListOf<FirestoreData>()
    private var adp = DataFromFirestoreAdapter(::sendDataToDelete) {
        sendToEditTraining(it)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_data_in_firestore)

        supportActionBar?.hide()
        recycler.adapter = adp
        recycler.layoutManager = LinearLayoutManager(this)

        // Desabilita a abertura do Teclado quando o item for clicado:
        textWorkoutList.showSoftInputOnFocus = false
        textWorkoutList.isCursorVisible = false

        val languages = resources.getStringArray(R.array.training_types)
        val arrayAdapter = ArrayAdapter(this, R.layout.list_item, languages)
        textWorkoutList.setAdapter(arrayAdapter)

        btnSearch.setOnClickListener {
            if (textWorkoutList.text.isEmpty()) {
                Toast.makeText(this, "Por favor escolha um Treino!", Toast.LENGTH_LONG).show()
            }
            if (textWorkoutList.text.isNotEmpty()) {
                searchTrainingInFirestore(textWorkoutList.text.toString())
            }
        }

        btnReturn.setOnClickListener {
            val intent = Intent(this, ReadDataFromFirestoreActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onBackPressed() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }


    private fun sendToEditTraining(item: FirestoreData) {
        val intent = Intent(this, EditTrainingActivity::class.java)
        intent.putExtra("Item", item)
        startActivity(intent)
    }


    private fun sendDataToDelete(item: FirestoreData) {
        showDialogDeleteItem(item)
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


    private fun searchTrainingInFirestore(training: String) {
        adp.deleteList(listReturnAux)
        btnSearch.isVisible = false

        val db = Firebase.firestore
        val docRef = db.collection("Treino")
        docRef
            .whereEqualTo("descricao", training)
            .get()
            .addOnSuccessListener { result ->
                loading.isVisible = true

                for (document in result) {
                    val info = document.toObject<FirestoreData>()
                    listReturn.add(info)
                    listReturnAux.add(info)
                }

                if (result.isEmpty)
                    Toast.makeText(
                        this,
                        "Não há informações sobre o item pesquisado!",
                        Toast.LENGTH_SHORT
                    ).show()

                adp.updateList(listReturn)
                listReturn.clear()

                Log.d("Read_Firestore", "$listReturn")
                loading.isVisible = false
                btnSearch.isVisible = true
            }
            .addOnFailureListener { exception ->
                Log.d("Read_Firestore", "Fail to try read data from Firestore", exception)
                loading.isVisible = false
                btnSearch.isVisible = true
            }
    }


    @SuppressLint("SetTextI18n")
    private fun showDialogDeleteItem(item: FirestoreData) {
        val dialog = Dialog(this)
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.fragment_dialog_option)

        val btnCancel = dialog.findViewById(R.id.btn_cancel) as Button
        val btnDelete = dialog.findViewById(R.id.btn_delete_item) as Button

        val body = dialog.findViewById(R.id.frag_title) as TextView
        body.text = "Deseja realmente apagar os dados deste treino?"

        btnCancel.setOnClickListener { dialog.dismiss() }
        btnDelete.setOnClickListener {
            deleteDocumentInFirestore(item)
            dialog.dismiss()
        }
        dialog.show()
    }
}
