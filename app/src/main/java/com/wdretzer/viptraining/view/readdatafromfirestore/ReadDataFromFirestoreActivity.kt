package com.wdretzer.viptraining.view.readdatafromfirestore

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.view.addtraining.ChooseTrainingActivity
import com.wdretzer.viptraining.modeldata.firebase.FirestoreData
import com.wdretzer.viptraining.view.edittraining.EditTrainingActivity
import com.wdretzer.viptraining.view.menu.MainMenuActivity
import com.wdretzer.viptraining.view.searchdatainfirestore.SearchDataInFirestoreActivity


// Clasee responsável pela leitura dos Dados Salvos no Firebase Firestore:
class ReadDataFromFirestoreActivity : AppCompatActivity() {

    private val btnSearch: ShapeableImageView
        get() = findViewById(R.id.btn_send_search)

    private val btnSendToMainMenu: ShapeableImageView
        get() = findViewById(R.id.btn_home)

    private val btnAddTraining: ShapeableImageView
        get() = findViewById(R.id.btn_add_exercise)

    private val btnMyTraining: ShapeableImageView
        get() = findViewById(R.id.btn_training)

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

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()
        getDataFromFirestore()
        btnSearch.setOnClickListener { sendToSearchTraining() }
        btnAddTraining.setOnClickListener { sendToChooseTraining() }
        btnMyTraining.setOnClickListener { sendToMyTraining() }
        btnSendToMainMenu.setOnClickListener { sendToMainMenu() }
    }

    // Método responsável pelo retorno a Activity:  MainMenuActivity
    override fun onBackPressed() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }


    // Método responsável por iniciar a Activity: ChooseTrainingActivity
    private fun sendToChooseTraining() {
        val intent = Intent(this, ChooseTrainingActivity::class.java)
        startActivity(intent)
    }


    // Método responsável por iniciar a Activity: ReadDataFromFirestoreActivity
    private fun sendToMyTraining() {
        val intent = Intent(this, ReadDataFromFirestoreActivity::class.java)
        startActivity(intent)
    }


    // Método responsável por iniciar a Activity: MainMenuActivity
    private fun sendToMainMenu() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }


    // Método responsável por iniciar um dialog com a confirmação ou cancelamento para ação deletar um item:
    private fun sendDataToDelete(item: FirestoreData) {
        showDialogDeleteItem(item)
    }


    // Método responsável por iniciar a Activity: EditTrainingActivity
    private fun sendToEditTraining(item: FirestoreData) {
        val intent = Intent(this, EditTrainingActivity::class.java)
        intent.putExtra("Item", item)
        startActivity(intent)
    }

    // Método responsável por iniciar a Activity: SearchDataInFirestoreActivity
    private fun sendToSearchTraining() {
        val intent = Intent(this, SearchDataInFirestoreActivity::class.java)
        startActivity(intent)
    }


    // Método responsável por realizar a leitura dos dados ao Firebase Firestore:
    private fun getDataFromFirestore() {
        loading.isVisible = true
        val db = Firebase.firestore
        val docRef = db.collection("Training")
        docRef.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val info = document.toObject<FirestoreData>()
                    listReturn.add(info)
                    Log.d("Item_Firestore", "${document.id} => ${document.data}")
                }

                if (result.isEmpty)
                    Toast.makeText(
                        this,
                        "Não há informações salvas no banco de dados!",
                        Toast.LENGTH_SHORT
                    ).show()

                adp.updateList(listReturn)
                recycler.adapter = adp
                recycler.layoutManager = LinearLayoutManager(this)

                Log.d("Read_Firestore", "$listReturn")
                loading.isVisible = false
            }
            .addOnFailureListener { exception ->
                Log.e("Read_Firestore", "Fail to try read data from Firestore", exception)
                loading.isVisible = false
            }
    }


    // Método responsável por deletar um item do Firebase Firestore:
    private fun deleteDocumentInFirestore(item: FirestoreData) {
        loading.isVisible = true
        val db = Firebase.firestore
        db.collection("Training").document("${item.data}")
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


    // Método responsável por iniciar o dialog para deletar item:
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
