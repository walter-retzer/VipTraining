package com.wdretzer.viptraining.inserttraining

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.choosetraining.ChooseTrainingActivity
import com.wdretzer.viptraining.onboarding.OnboardingScreenAdapter


class InsertTrainingActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy { findViewById(R.id.view_pager_insert_training) }

    private val btnNext: Button
        get() = findViewById(R.id.btn_insert)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_training)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()

        setupViewPager()

        btnNext.setOnClickListener { sendToChooseTraining() }
    }


    private fun sendToChooseTraining() {
        val intent = Intent(this, ChooseTrainingActivity::class.java)
        startActivity(intent)
    }


    private fun setupViewPager() {
        val listFragments = listOf(
            TrainingType1Fragment(),
            TrainingType1Fragment(),
            TrainingType1Fragment(),
        )

        viewPager.adapter = OnboardingScreenAdapter(
            this, listFragments
        )
    }
}
