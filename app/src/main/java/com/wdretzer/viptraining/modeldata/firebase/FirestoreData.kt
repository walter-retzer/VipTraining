package com.wdretzer.viptraining.modeldata

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize


@Parcelize
data class FirestoreData(
    val name: Int? = null,
    val descricao: String? = null,
    val data: Timestamp? = null,
    val listExercise: MutableList<ExerciseData>? = null,
    val status: CheckBoxStatus? = null
) : Parcelable


@Parcelize
data class ExerciseData(
    val nome: Int? = null,
    val imagem: String? = null,
    val observacoes: String? = null,
) : Parcelable

@Parcelize
data class CheckBoxStatus(
    val checkbox1: Boolean = false,
    val checkbox2: Boolean = false,
    val checkbox3: Boolean = false,
    val checkbox4: Boolean = false,
) : Parcelable
