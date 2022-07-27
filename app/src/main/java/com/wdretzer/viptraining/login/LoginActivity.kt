package com.wdretzer.viptraining.login

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
import com.wdretzer.viptraining.createaccount.CreateUserAccountActivity
import com.wdretzer.viptraining.data.extension.DataResult
import com.wdretzer.viptraining.dialogfragment.ForgotPasswordDialogFragment
import com.wdretzer.viptraining.mainmenu.MainMenuActivity
import com.wdretzer.viptraining.profile.MyProfileActivity
import com.wdretzer.viptraining.viewmodel.VipTrainingViewModel


class LoginActivity : AppCompatActivity() {

    private val textEmail: TextInputEditText
        get() = findViewById(R.id.input_email_login)

    private val forgotPassword: TextView
        get() = findViewById(R.id.text_forget_password)

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

       // btnGoogle.setOnClickListener { sendToMainMenu() }
        btnGoogle.setOnClickListener { sendToMyProfile() }
        btnFacebook.setOnClickListener { sendToCreateAccount() }
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
                    Log.d("Login_Firebase:", "Autenticação Realizada com Sucesso! ")
                    sendToMainMenu()
                }
            }
        }
    }


    private fun sendToCreateAccount() {
        val intent = Intent(this, CreateUserAccountActivity::class.java)
        startActivity(intent)
    }


    private fun sendToMainMenu() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }

    private fun sendToMyProfile() {
        val intent = Intent(this, MyProfileActivity::class.java)
        startActivity(intent)
    }
}
