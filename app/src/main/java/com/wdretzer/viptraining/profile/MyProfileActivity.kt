package com.wdretzer.viptraining.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.data.extension.DataResult
import com.wdretzer.viptraining.mainmenu.MainMenuActivity
import com.wdretzer.viptraining.util.SharedPrefVipTraining
import com.wdretzer.viptraining.viewmodel.VipTrainingViewModel

class MyProfileActivity : AppCompatActivity() {

    private val textName: TextView
        get() = findViewById(R.id.text_name_user)

    private val textBirthday: TextInputEditText
        get() = findViewById(R.id.input_birthday)

    private val textWeigth: TextInputEditText
        get() = findViewById(R.id.input_weight)

    private val textHeigth: TextInputEditText
        get() = findViewById(R.id.input_height)

    private val btnSaveProfile: Button
        get() = findViewById(R.id.btn_save_profile)

    private val progressBar: FrameLayout
        get() = findViewById(R.id.progress_bar_login)

    private val viewModel: VipTrainingViewModel by viewModels()

    private val sharedPref: SharedPrefVipTraining = SharedPrefVipTraining.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()
        checkUser()

        btnSaveProfile.setOnClickListener {
            saveName(textName.text.toString())
            saveBirthday(textBirthday.text.toString())
            saveWeight(textWeigth.text.toString())
            saveHeight(textHeigth.text.toString())
            Toast.makeText(this, "Dados Salvos!", Toast.LENGTH_SHORT).show()
            sendToMainMenu()
        }
    }


    private fun checkUser() {
        viewModel.checkUserName().observe(this) {
            if (it is DataResult.Loading) {
                progressBar.isVisible = it.isLoading
            }

            if (it is DataResult.Warning) {
                textName.text = it.message
            }
        }
    }

    private fun saveName(name: String) {
        sharedPref.saveString("Name", name)
    }

    private fun saveBirthday(name: String) {
        sharedPref.saveString("Name", name)
    }

    private fun saveWeight(weight: String) {
        sharedPref.saveString("Weight", weight)
    }

    private fun saveHeight(height: String) {
        sharedPref.saveString("Height", height)
    }

    private fun sendToMainMenu() {
        Handler().postDelayed({
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        }, 2000)
    }
}
