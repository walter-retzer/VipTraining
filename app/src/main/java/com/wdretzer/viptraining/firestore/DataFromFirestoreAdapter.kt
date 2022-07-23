package com.wdretzer.viptraining.firestore

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.datafirebase.FirestoreData


class DataFromFirestoreAdapter(
    private val action: (FirestoreData) -> Unit,
    private val detailAction: (FirestoreData) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffUtil = AsyncListDiffer<FirestoreData>(this, DIFF_UTIL)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return ImagesFirestoreViewHolder(
            inflater.inflate(R.layout.item_training, parent, false), detailAction, action
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImagesFirestoreViewHolder -> holder.bind(diffUtil.currentList[position])
        }
    }

    override fun getItemCount(): Int = diffUtil.currentList.size

    override fun getItemViewType(position: Int): Int {
        return position
    }


    fun updateList(newlist: MutableList<FirestoreData>) {
        diffUtil.submitList(diffUtil.currentList.plus(newlist))
    }

    companion object {
        val DIFF_UTIL = object : DiffUtil.ItemCallback<FirestoreData>() {
            override fun areItemsTheSame(oldItem: FirestoreData, newItem: FirestoreData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: FirestoreData,
                newItem: FirestoreData
            ): Boolean {
                return oldItem.data == newItem.data
            }
        }
    }
}


class ImagesFirestoreViewHolder(
    view: View,
    private val action: (FirestoreData) -> Unit,
    private val detailAction: (FirestoreData) -> Unit
) : RecyclerView.ViewHolder(view) {

    var imageExerise: ImageView = view.findViewById(R.id.image_training_firestore)
    var nameExercise1: TextView = view.findViewById(R.id.name_exercise1_firestore)
    var nameExercise2: TextView = view.findViewById(R.id.name_exercise2_firestore)
    var nameExercise3: TextView = view.findViewById(R.id.name_exercise3_firestore)
    var nameExercise4: TextView = view.findViewById(R.id.name_exercise4_firestore)
    var nameTraining: TextView = view.findViewById(R.id.name_training_firestore)
    var btnDelete: ShapeableImageView = view.findViewById(R.id.btn_delete)
    var btnEdit: ShapeableImageView = view.findViewById(R.id.btn_edit)

    private var itemCorrente: FirestoreData? = null

    init {
        view.setOnClickListener {
            itemCorrente?.let {
                action.invoke(it)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: FirestoreData) {
        nameTraining.text = item.descricao
        try {
            nameExercise1.text = item.listExercise?.get(0)?.observacoes
            nameExercise2.text = item.listExercise?.get(1)?.observacoes
            nameExercise3.text = item.listExercise?.get(2)?.observacoes
            nameExercise4.text = item.listExercise?.get(3)?.observacoes
        } catch (e: Exception) {
            Log.d("Item_Firestore", "Indice não encontrado.")
        }

        btnDelete.setOnClickListener {
            detailAction.invoke(item)
        }

        btnEdit.setOnClickListener {
            detailAction.invoke(item)
        }

        Glide.with(imageExerise.context)
            //.load(item.listExercise?.first()?.imagem)
            .load(R.drawable.img_3_fragment_onboarding)
            .placeholder(R.drawable.img_exercise)
            .error(R.drawable.icon_error)
            .into(imageExerise)

        itemCorrente = item
    }
}
