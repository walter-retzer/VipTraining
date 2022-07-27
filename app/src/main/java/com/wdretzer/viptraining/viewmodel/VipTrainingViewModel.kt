package com.wdretzer.viptraining.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.wdretzer.viptraining.data.extension.DataResult


class VipTrainingViewModel() : ViewModel() {

    fun login(email: String, password: String): MutableLiveData<DataResult<Boolean>> {
        val result = MutableLiveData<DataResult<Boolean>>()
        result.value = DataResult.Loading(true)

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result.value = DataResult.Warning("Email inválido!")
            return result
        }

        if (email.isEmpty()) {
            result.value = DataResult.Warning("Por favor, insira o seu Email!")
            return result
        }

        if (password.length <= 5 && email.isNotEmpty()) {
            result.value = DataResult.Warning("Senha digitada esta incorreta!")
            return result
        }

        FirebaseAuth.getInstance().signInWithEmailAndPassword(
            email, password
        )
            .addOnCompleteListener { task ->
                result.value = DataResult.Loading(false)
                if (task.isSuccessful) {
                    Log.i("Auth_Login:", "Email signup is successful")
                    result.value = DataResult.Success(true)
                }

                else if (task.isCanceled){
                    Log.i("Auth_Login:", "Auth was canceled!")
                    result.value = DataResult.Empty
                }

                else {
                    task.exception?.let {
                        Log.i(
                            "Auth_Login:",
                            "Signup failed with error ${it.localizedMessage}"
                        )
                        result.value = DataResult.Error(it)
                    }
                }
            }
        return result
    }
}
