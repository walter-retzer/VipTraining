package com.wdretzer.viptraining.view.menu

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.imageview.ShapeableImageView
import com.wdretzer.viptraining.R


// Classe com as Informações do Treino Ganhar Músculos:
class TrainingType1Fragment : Fragment(R.layout.fragment_training_type1){
    private val imageCard: ShapeableImageView?
        get() = view?.findViewById(R.id.image_training_info)

    private val titleCard: TextView?
        get() = view?.findViewById(R.id.name_training_info)

    private val subtitleCard: TextView?
        get() = view?.findViewById(R.id.name_exercise1_info)

    private val subtitleCard2: TextView?
        get() = view?.findViewById(R.id.name_exercise2_info)

    private val textDescription: TextView?
        get() = view?.findViewById(R.id.text_details_training)

    private val imageExercise1: ShapeableImageView?
        get() = view?.findViewById(R.id.img_exercise1)

    private val imageExercise2: ShapeableImageView?
        get() = view?.findViewById(R.id.img_exercise2)

    private val imageExercise3: ShapeableImageView?
        get() = view?.findViewById(R.id.img_exercise3)

    private val imageExercise4: ShapeableImageView?
        get() = view?.findViewById(R.id.img_exercise4)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageExercise1?.setImageResource(R.drawable.img_muscle)
        imageExercise2?.setImageResource(R.drawable.img_muscle)
        imageExercise3?.setImageResource(R.drawable.img_muscle)
        imageExercise4?.setImageResource(R.drawable.img_muscle)
        imageCard?.setImageResource(R.drawable.img_muscle2)

        titleCard?.text = "Ganhar Músculos"
        subtitleCard?.text = "Exercicios que ajudam na definição dos seus músculos."
        subtitleCard2?.text = "Nível Avançado*"
        textDescription?.text = "Para ganhar massa muscular, é importante fazer atividade física de forma regular e seguir uma alimentação adequada"
    }
}
