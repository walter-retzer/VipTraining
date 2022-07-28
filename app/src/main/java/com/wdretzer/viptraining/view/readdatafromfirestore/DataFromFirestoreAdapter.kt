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

    fun updateItem(item: FirestoreData) {
        val list = mutableListOf(item)
        diffUtil.submitList(diffUtil.currentList.minus(list.toSet()))
    }

    fun updateList(newlist: MutableList<FirestoreData>) {
        diffUtil.submitList(diffUtil.currentList.plus(newlist))
    }

    fun deleteList(list: MutableList<FirestoreData>) {
        diffUtil.submitList(diffUtil.currentList.minus(list.toSet()))
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
                return oldItem.data == newItem.data || oldItem.descricao == newItem.descricao
            }
        }
    }
}


class ImagesFirestoreViewHolder(
    view: View,
    private val action: (FirestoreData) -> Unit,
    private val detailAction: (FirestoreData) -> Unit
) : RecyclerView.ViewHolder(view) {

    private val imageExerise: ImageView = view.findViewById(R.id.image_training_firestore)
    private val nameExercise1: TextView = view.findViewById(R.id.name_exercise1_firestore)
    private val nameExercise2: TextView = view.findViewById(R.id.name_exercise2_firestore)
    private val nameExercise3: TextView = view.findViewById(R.id.name_exercise3_firestore)
    private val nameExercise4: TextView = view.findViewById(R.id.name_exercise4_firestore)
    private val nameTraining: TextView = view.findViewById(R.id.name_training_firestore)
    private val btnDelete: ShapeableImageView = view.findViewById(R.id.btn_delete)
    private val btnEdit: ShapeableImageView = view.findViewById(R.id.btn_edit)

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

        btnDelete.setOnClickListener { detailAction.invoke(item) }
        btnEdit.setOnClickListener { action.invoke(item) }

        Glide.with(imageExerise.context)
            .load(item.listExercise?.first()?.imagem)
            .placeholder(R.drawable.img_woman_training)
            .error(R.drawable.img_woman_training)
            .into(imageExerise)
    }
}
