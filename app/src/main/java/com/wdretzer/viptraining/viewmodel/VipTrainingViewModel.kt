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
                } else if (task.isCanceled) {
                    Log.i("Auth_Login:", "Auth was canceled!")
                    result.value = DataResult.Empty
                } else {
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


    fun createUser(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): MutableLiveData<DataResult<Boolean>> {

        val result = MutableLiveData<DataResult<Boolean>>()
        result.value = DataResult.Loading(true)

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            result.value = DataResult.Warning("Há campos não preenchidos!")
            return result
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            result.value = DataResult.Warning("Email inválido!")
            return result
        }

        if (password.length <= 5 || confirmPassword.length <= 5) {
            result.value = DataResult.Warning("As senhas devem conter 6 digitos!")
            return result
        }

        if (password != confirmPassword) {
            result.value = DataResult.Warning("As senhas digitadas são diferentes!")
            return result
        }

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() && name.isNotEmpty() && password == confirmPassword
        ) {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                email, password
            )
                .addOnCompleteListener { task ->
                    result.value = DataResult.Loading(false)

                    if (task.isSuccessful) {
                        Log.i("Create_User_Firebase:", "Create User is successful!")
                        result.value = DataResult.Success(true)

                    } else if (task.isCanceled) {
                        Log.i("Create_User_Firebase:", "Create User was canceled!")
                        result.value = DataResult.Empty

                    } else {
                        task.exception?.let {
                            Log.i(
                                "Create_User_Firebase",
                                "Create user in Firebase failed with error ${it.localizedMessage}"
                            )
                            result.value = DataResult.Error(it)
                        }
                    }
                }
        }
        return result
    }


}
