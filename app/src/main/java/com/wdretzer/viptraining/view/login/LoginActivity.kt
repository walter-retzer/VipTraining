package com.wdretzer.viptraining.view.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.view.createuser.CreateUserAccountActivity
import com.wdretzer.viptraining.modeldata.extension.DataResult
import com.wdretzer.viptraining.view.dialogs.ForgotPasswordDialogFragment
import com.wdretzer.viptraining.view.menu.MainMenuActivity
import com.wdretzer.viptraining.viewmodel.VipTrainingViewModel


// Classe responsável pelo Login do Usuário:
class LoginActivity : AppCompatActivity() {

    private val textEmail: TextInputEditText
        get() = findViewById(R.id.input_email_login)

    private val forgotPassword: TextView
        get() = findViewById(R.id.text_forget_password)

    private val createAccount: TextView
        get() = findViewById(R.id.text_register)

    private val textPassword: TextInputEditText
        get() = findViewById(R.id.input_password_login)

    private val btnGoogle: ShapeableImageView
        get() = findViewById(R.id.btn_icon_google)

    private val btnFacebook: ShapeableImageView
        get() = findViewById(R.id.btn_icon_facebook)

    private val btnLogin: Button
        get() = findViewById(R.id.btn_login)

    private val progressBar: FrameLayout
        get() = findViewById(R.id.progress_bar_login)

    private val dialogForgetPassword = ForgotPasswordDialogFragment()

    private val viewModel: VipTrainingViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()

        btnGoogle.setOnClickListener {  }
        btnFacebook.setOnClickListener {  }
        createAccount.setOnClickListener { sendToCreateAccount() }
        btnLogin.setOnClickListener {
            checkInfoFromFields(
                textEmail.text.toString(),
                textPassword.text.toString()
            )
        }

        forgotPassword.setOnClickListener {
            dialogForgetPassword.show(
                supportFragmentManager,
                dialogForgetPassword.tag
            )
        }
    }


    // Método responsável por checar os campos digitados de email e senha:
    private fun checkInfoFromFields(email: String, password: String) {
        viewModel.login(email, password).observe(this) {
            when (it) {
                is DataResult.Loading -> {
                    progressBar.isVisible = it.isLoading
                }

                is DataResult.Warning -> {
                    Toast.makeText(this, "${it.message}!", Toast.LENGTH_LONG).show()
                }

                is DataResult.Empty -> {
                    Toast.makeText(this, "Login Cancelado!", Toast.LENGTH_LONG).show()
                }

                is DataResult.Error -> {
                    Toast.makeText(this, "Verique o email e a senha digitada!", Toast.LENGTH_LONG)
                        .show()
                }

                is DataResult.Success -> {
                    Log.i("Login_Firebase:", "Autenticação Realizada com Sucesso! ")
                    sendToMainMenu()
                }
            }
        }
    }


    // Método responsável por iniciar a Activity: CreateUserAccountActivity
    private fun sendToCreateAccount() {
        val intent = Intent(this, CreateUserAccountActivity::class.java)
        startActivity(intent)
    }


    // Método responsável por iniciar a Activity: MainMenuActivity
    private fun sendToMainMenu() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }
}
