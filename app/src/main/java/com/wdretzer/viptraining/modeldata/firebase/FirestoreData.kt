package com.wdretzer.viptraining.modeldata.firebase

import android.os.Parcelable
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize


// Modelo Utilizado para salvar os dados no Firebase Firestore:
@Parcelize
data class FirestoreData(
    val name: Int? = null,
    val descricao: String? = null,
    val data: Timestamp? = null,
    val listExercise: MutableList<ExerciseData>? = null,
    val status: CheckBoxStatus? = null
) : Parcelable


// Modelo Utilizado para salvar os dados no Firebase Firestore:
@Parcelize
data class ExerciseData(
    val nome: Int? = null,
    val imagem: String? = null,
    val observacoes: String? = null,
) : Parcelable


// Classe responsável pelo estado dos Checkboxes dos Exercícios que são selecionados pelo usuário.
@Parcelize
data class CheckBoxStatus(
    val checkbox1: Boolean = false,
    val checkbox2: Boolean = false,
    val checkbox3: Boolean = false,
    val checkbox4: Boolean = false,
) : Parcelable
