package com.wdretzer.viptraining.datafirebase

import android.net.Uri
import com.google.firebase.Timestamp

data class FirestoreData(
    val name: Int? = null,
    val descricao: String? = null,
    val data: Timestamp? = null,
    val listExercise: MutableList<ExerciseData>? = null
)

data class ExerciseData(
    val nome: Int? = null,
    val imagem: Uri? = null,
    val observacoes: String? = null
)
