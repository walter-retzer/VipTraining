package com.wdretzer.viptraining.createaccount

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.wdretzer.viptraining.R


class CreateUserAccountActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user_account)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        btnCreateAccount.setOnClickListener { cadastrarDados() }
    }


    private fun cadastrarDados() {
        progressBar.isVisible = true

        if ((textEmail.text?.isEmpty() == true) ||
            (textPassword.text?.isEmpty() == true) ||
            (textName.text?.isEmpty() == true) ||
            (textPasswordConfirm.text?.isEmpty() == true)
        ) {
            Toast.makeText(this, "Há Campos não Preenchidos!", Toast.LENGTH_LONG).show()
            progressBar.isVisible = false

        } else if (textPassword.text?.toString() != textPasswordConfirm.text?.toString()) {
            Toast.makeText(this, "Verifique as Senhas Digitadas!", Toast.LENGTH_LONG).show()
            progressBar.isVisible = false

        } else if (textPassword.text.length <= 5 || textPasswordConfirm.text.length <= 5) {
            Toast.makeText(this, "As Senhas devem conter 6 Números!", Toast.LENGTH_LONG).show()
            progressBar.isVisible = false

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(textEmail.text.toString())
                .matches()
        ) {
            Toast.makeText(this, "O Email digitado não é válido!", Toast.LENGTH_LONG).show()
            progressBar.isVisible = false

        } else if ((textPassword.text?.toString() == textPasswordConfirm.text?.toString()) &&
            (android.util.Patterns.EMAIL_ADDRESS.matcher(textEmail.text.toString())
                .matches())
        ) {

            auth.createUserWithEmailAndPassword(
                textEmail.text.toString(),
                textPassword.text.toString()

            ).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Cadastro Realizado com Sucesso!", Toast.LENGTH_LONG)
                        .show()

                    Handler().postDelayed({
                        userAuth()
                    }, 5000)

                    Handler().postDelayed({
                        checkUser()
                    }, 10000)

                } else {
                    Toast.makeText(this, "Deu erro ao Fazer o Cadastro!!", Toast.LENGTH_LONG)
                        .show()
                    progressBar.isVisible = false
                }
            }
        }
    }

    private fun userAuth() {
        auth.signInWithEmailAndPassword(
            textEmail.text.toString(),
            textPassword.text.toString()
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Autenticando Login...", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Não foi possível realizar o Login!", Toast.LENGTH_LONG)
                        .show()
                }
            }
    }

    private fun checkUser() {
        if (auth.currentUser != null) {
            auth.currentUser?.apply {
                updateProfile(userProfileChangeRequest {

                    displayName = textName.text.toString()
                    photoUri =
                        Uri.parse("https://img.freepik.com/free-vector/cute-astronaut-jumping-with-metal-hands-cartoon-vector-icon-illustration-science-technology-icon-concept-isolated-premium-vector-flat-cartoon-style_138676-4189.jpg?t=st=1650992946~exp=1650993546~hmac=5f1baeadf83b886a56d751df0bce8ebc501b4ccc661e192158703c34e2d8d019&w=740")

                }).addOnCompleteListener {

                    if (it.isSuccessful) {
                        Toast.makeText(
                            this@CreateUserAccountActivity,
                            "Usuário: ${auth.currentUser?.displayName} incluído(a) no Firebase!!",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {
                        Toast.makeText(
                            this@CreateUserAccountActivity,
                            "Deu erro ao atualizar o cadastro!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        progressBar.isVisible = false
    }
}
