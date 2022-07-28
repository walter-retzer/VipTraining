package com.wdretzer.viptraining.view.menu

import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.android.material.imageview.ShapeableImageView
import com.wdretzer.viptraining.R


class TrainingType3Fragment : Fragment(R.layout.fragment_training_type1){

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

        imageExercise1?.setImageResource(R.drawable.img_exercise)
        imageExercise2?.setImageResource(R.drawable.img_exercise)
        imageExercise3?.setImageResource(R.drawable.img_exercise)
        imageExercise4?.setImageResource(R.drawable.img_exercise)
        imageCard?.setImageResource(R.drawable.img_man_run)

        titleCard?.text = "Sair do Sedentarismo"
        subtitleCard?.text = "Praticar exercícios pelo menos três vezes por semana."
        subtitleCard2?.text = "Nível Iniciante"
        textDescription?.text = "O mais importante, segundo especialistas, para dar o pontapé inicial e iniciar alguma prática esportiva, é sair da zona de conforto."
    }
}
