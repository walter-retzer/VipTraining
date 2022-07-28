package com.wdretzer.viptraining.view.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.wdretzer.viptraining.R


// Classe do Bottom Sheet para enviar o resete da senha ao email digitado:
class ForgotPasswordDialogFragment : BottomSheetDialogFragment() {

    private val btnContinue: Button?
        get() = view?.findViewById(R.id.forgot_password_btn_continue)

    private val textEmailReset: EditText?
        get() = view?.findViewById(R.id.forgot_password_input_text)

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password_dialog, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        btnContinue?.setOnClickListener {
            if (textEmailReset?.text?.isNotEmpty() == true) {
                val email = textEmailReset?.text.toString()
                resetPasswordFirebase(email)
            } else {
                Toast.makeText(context, "Digite um e-mail.", Toast.LENGTH_LONG).show()
            }
        }
    }


    // Método responsável por enviar o resete da senha por e-mail:
    private fun resetPasswordFirebase(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    context,
                    "Link com o resete da sua senha foi enviado ao email digitado!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    context,
                    "Falha em enviar o link de resete da senha!",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        const val TAG = "ForgotPasswordDialog"
    }
}
