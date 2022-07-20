package com.wdretzer.viptraining.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.*
import androidx.core.view.isVisible
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.createaccount.CreateUserAccountActivity


class LoginActivity : AppCompatActivity() {

    private val textEmail: EditText
        get() = findViewById(R.id.input_email_login)

    private val forgotPassword: TextView
        get() = findViewById(R.id.text_forget_password)

    private val textPassword: EditText
        get() = findViewById(R.id.input_password_login)

    private val btnGoogle: ShapeableImageView
        get() = findViewById(R.id.btn_icon_google)

    private val btnFacebook: ShapeableImageView
        get() = findViewById(R.id.btn_icon_facebook)

    private val btnLogin: Button
        get() = findViewById(R.id.btn_login)

    private val progressBar: FrameLayout
        get() = findViewById(R.id.progress_bar_login)

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        btnLogin.setOnClickListener { checkDados() }
        btnGoogle.setOnClickListener { sendToCreateAccount() }
        btnFacebook.setOnClickListener { sendToCreateAccount() }
        forgotPassword.setOnClickListener { sendToCreateAccount() }
    }


    private fun checkDados() {
        progressBar.isVisible = true

        if ((textEmail.text?.isEmpty() == true) || (textPassword.text?.isEmpty() == true)) {
            Toast.makeText(this, "Há Campos não Preenchidos!", Toast.LENGTH_LONG).show()
            progressBar.isVisible = false

        } else if (textPassword.text.length <= 5 && textEmail.text?.isNotEmpty() == true) {
            Toast.makeText(this, "A Senha deve conter 6 Números!", Toast.LENGTH_LONG).show()
            progressBar.isVisible = false

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(textEmail.text.toString())
                .matches()
        ) {
            Toast.makeText(this, "O Email digitado não é válido!", Toast.LENGTH_LONG).show()
            progressBar.isVisible = false

        } else {
            checkLoginUser()
        }
    }


    private fun checkLoginUser() {
        auth.signInWithEmailAndPassword(
            textEmail.text.toString(),
            textPassword.text.toString()
        )
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    Toast.makeText(this, "Autenticando Login...", Toast.LENGTH_LONG).show()

                } else {
                    Toast.makeText(
                        this,
                        "Login não realizado! Check seu e-mail e senha!",
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.isVisible = false
                }
            }
    }

    private fun sendToCreateAccount() {
        Handler().postDelayed({
            val intent = Intent(this, CreateUserAccountActivity::class.java)
            startActivity(intent)
        }, 2000)
    }
}
