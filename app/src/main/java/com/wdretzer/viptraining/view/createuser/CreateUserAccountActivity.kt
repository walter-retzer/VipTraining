package com.wdretzer.viptraining.view.createuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.modeldata.extension.DataResult
import com.wdretzer.viptraining.view.profile.MyProfileActivity
import com.wdretzer.viptraining.viewmodel.VipTrainingViewModel


class CreateUserAccountActivity : AppCompatActivity() {

    private val textName: EditText
        get() = findViewById(R.id.input_name_account)

    private val textEmail: EditText
        get() = findViewById(R.id.input_email_account)

    private val textPassword: EditText
        get() = findViewById(R.id.input_password_login)

    private val textPasswordConfirm: EditText
        get() = findViewById(R.id.input_confirm_password)

    private val btnCreateAccount: Button
        get() = findViewById(R.id.btn_create_account)

    private val progressBar: FrameLayout
        get() = findViewById(R.id.progress_bar_login)

    private val viewModel: VipTrainingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user_account)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()

        btnCreateAccount.setOnClickListener {
            createUser(
                textName.text.toString(),
                textEmail.text.toString(),
                textPassword.text.toString(),
                textPasswordConfirm.text.toString()
            )
        }
    }


    private fun createUser(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {

        btnCreateAccount.visibility= View.INVISIBLE

        viewModel.createUser(name, email, password, confirmPassword).observe(this) {
            when (it) {
                is DataResult.Loading -> {
                    progressBar.isVisible = it.isLoading
                }

                is DataResult.Warning -> {
                    Toast.makeText(this, "${it.message}!", Toast.LENGTH_LONG).show()
                }

                is DataResult.Empty -> {
                    Toast.makeText(this, "Criação de Usuário Cancelado!", Toast.LENGTH_LONG).show()
                }

                is DataResult.Error -> {
                    Toast.makeText(this, "Não foi possível criar um usuário!!", Toast.LENGTH_LONG)
                        .show()
                }

                is DataResult.Success -> {
                    Log.d("Login_Firebase:", "Usuario criado com Sucesso! ")
                    authUserLogin(email, password)
                }
            }
        }
    }


    private fun authUserLogin(email: String, password: String) {
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
                    Toast.makeText(this, "Criando perfil do Usuario!", Toast.LENGTH_LONG)
                        .show()
                    Handler().postDelayed({
                        updateUserName(textName.text.toString(), null  )
                    }, 4000)

                }
            }
        }
    }

    private fun updateUserName(name: String, uri: String?) {
        viewModel.updateUserName(name, null).observe(this) {
            if (it is DataResult.Loading) {
                progressBar.isVisible = it.isLoading
            }

            if (it is DataResult.Success) {
                btnCreateAccount.visibility= View.VISIBLE
                sendToMyProfile()
            }
        }
    }


    private fun sendToMyProfile() {
        val intent = Intent(this, MyProfileActivity::class.java)
        startActivity(intent)
    }

}
