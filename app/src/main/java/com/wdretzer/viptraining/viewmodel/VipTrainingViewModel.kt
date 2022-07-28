package com.wdretzer.viptraining.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import com.wdretzer.viptraining.modeldata.extension.DataResult


// Classe com as funções utilizadas pelo ViewModel:
class VipTrainingViewModel() : ViewModel() {

    var firebaseAuth = FirebaseAuth.getInstance()

    // Método responsável por realizar a autenticação do usuário no Firebase Authentication:
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

        firebaseAuth.signInWithEmailAndPassword(
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
                        Log.e(
                            "Auth_Login:",
                            "Signup failed with error ${it.localizedMessage}"
                        )
                        result.value = DataResult.Error(it)
                    }
                }
            }
        return result
    }


    // Método responsável por realizar a criação do usuário no Firebase Authentication:
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

            firebaseAuth.createUserWithEmailAndPassword(
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
                            Log.e(
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


    // Método responsável por realizar a atualização do nome do usuário no Firebase Authentication:
    fun updateUserName(name: String?): MutableLiveData<DataResult<Boolean>> {
        val result = MutableLiveData<DataResult<Boolean>>()
        result.value = DataResult.Loading(true)

        if (firebaseAuth.currentUser != null) {
            firebaseAuth.currentUser?.apply {
                updateProfile(userProfileChangeRequest {
                    name?.let { displayName = it }

                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        result.value =
                            DataResult.Warning("${firebaseAuth.currentUser?.displayName}")
                        result.value = DataResult.Success(true)
                        result.value = DataResult.Loading(false)
                        Log.i(
                            "Current_User_Firebase:",
                            "Current User: ${firebaseAuth.currentUser?.displayName}"
                        )

                    } else {
                        task.exception?.let {
                            Log.i(
                                "Current_User_Firebase:",
                                "Current User in Firebase failed with error ${it.localizedMessage}"
                            )
                            result.value = DataResult.Error(it)
                            result.value = DataResult.Loading(false)
                        }
                    }
                }
            }
        }
        return result
    }


    // Método responsável por realizar a atualização da foto do usuário no Firebase Authentication:
    fun updateUserPhoto(uri: String?): MutableLiveData<DataResult<Boolean>> {
        val result = MutableLiveData<DataResult<Boolean>>()
        result.value = DataResult.Loading(true)

        if (firebaseAuth.currentUser != null) {

            firebaseAuth.currentUser?.apply {
                updateProfile(userProfileChangeRequest {
                    uri?.let { photoUri = Uri.parse(it) }

                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        result.value = DataResult.Warning("${firebaseAuth.currentUser?.photoUrl}")
                        result.value = DataResult.Success(true)
                        result.value = DataResult.Loading(false)
                        Log.i(
                            "Current_User_Firebase:",
                            "Current User: ${firebaseAuth.currentUser?.photoUrl}"
                        )

                    } else {
                        task.exception?.let {
                            Log.e(
                                "Current_User_Firebase:",
                                "Current User in Firebase failed with error ${it.localizedMessage}"
                            )
                            result.value = DataResult.Error(it)
                            result.value = DataResult.Loading(false)
                        }
                    }
                }
            }
        }
        return result
    }


    // Método responsável por retornar o nome do usuário logado no Firebase Authentication:
    fun checkUserName(): MutableLiveData<DataResult<Boolean>> {
        val result = MutableLiveData<DataResult<Boolean>>()
        result.value = DataResult.Loading(true)

        if (firebaseAuth.currentUser != null) {

            firebaseAuth.currentUser?.apply {
                updateProfile(userProfileChangeRequest {

                }).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        result.value =
                            DataResult.Warning("${firebaseAuth.currentUser?.displayName}")
                        result.value = DataResult.Success(true)
                        result.value = DataResult.Loading(false)
                        Log.i(
                            "Current_User_Firebase:",
                            "Current User: ${firebaseAuth.currentUser?.displayName}"
                        )

                    } else {
                        task.exception?.let {
                            Log.e(
                                "Current_User_Firebase:",
                                "Current User in Firebase failed with error ${it.localizedMessage}"
                            )
                            result.value = DataResult.Error(it)
                            result.value = DataResult.Loading(false)
                        }
                    }
                }
            }
        }
        return result
    }


    // Método responsável por enviar arquivo ao Firebase Storage
    fun uploadFileToFirebaseStorage(
        uri: Uri,
        fileName: String,
        localStorage: String
    ): MutableLiveData<DataResult<Boolean>> {

        val result = MutableLiveData<DataResult<Boolean>>()
        val firebaseStorage = FirebaseStorage.getInstance()
        val storage = firebaseStorage.getReference(localStorage)
        val fileReference = storage.child(fileName)

        result.value = DataResult.Loading(true)

        uri.apply {
            fileReference
                .putFile(this)
                .addOnSuccessListener {
                    Log.d("Firestore_Storage:", "Upload Ok. Imagem $fileName")
                    result.value = DataResult.Success(true)
                }
                .addOnFailureListener {
                    Log.e("Firestore_Storage:", "Upload Não Ok. Imagem $fileName")
                    result.value = DataResult.Error(it)
                }
                .addOnProgressListener { result.value = DataResult.Loading(true) }
                .addOnCompleteListener { result.value = DataResult.Loading(false) }
        }
        return result
    }
}
