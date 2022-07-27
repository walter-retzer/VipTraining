package com.wdretzer.viptraining.mainmenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.choosetraining.ChooseTrainingActivity
import com.wdretzer.viptraining.firestore.ReadDataFromFirestoreActivity
import com.wdretzer.viptraining.onboarding.OnboardingScreenAdapter
import com.wdretzer.viptraining.profile.MyProfileActivity
import com.wdretzer.viptraining.searchinfirestore.SearchDataInFirestoreActivity
import java.text.SimpleDateFormat
import java.util.*


class MainMenuActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy { findViewById(R.id.view_pager_insert_training) }
    private val indicator: TabLayout by lazy { findViewById(R.id.indicator) }

    private val btnAddTraining: ShapeableImageView
        get() = findViewById(R.id.btn_add_exercise)

    private val btnProfile: ShapeableImageView
        get() = findViewById(R.id.btn_home)

    private val btnSearchTraining: ShapeableImageView
        get() = findViewById(R.id.btn_send_search)

    private val btnMyTraining: ShapeableImageView
        get() = findViewById(R.id.btn_training)

    private val textDate: TextView
        get() = findViewById(R.id.text_date_training)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        // Desabilita a Action Bar que exibe o nome do Projeto:
        supportActionBar?.hide()
        setupViewPager()

        val sdf = SimpleDateFormat("E, dd-MM-yyyy")
        val currentDate = sdf.format(Date())
        textDate.text = currentDate.toString()

        btnAddTraining.setOnClickListener { sendToChooseTraining() }
        btnSearchTraining.setOnClickListener { sendToSearchTraining() }
        btnMyTraining.setOnClickListener { sendToMyTraining() }
        btnProfile.setOnClickListener { sendToMyProfile() }
    }


    private fun sendToChooseTraining() {
        val intent = Intent(this, ChooseTrainingActivity::class.java)
        startActivity(intent)
    }

    private fun sendToSearchTraining() {
        val intent = Intent(this, SearchDataInFirestoreActivity::class.java)
        startActivity(intent)
    }

    private fun sendToMyTraining() {
        val intent = Intent(this, ReadDataFromFirestoreActivity::class.java)
        startActivity(intent)
    }

    private fun sendToMyProfile() {
        val intent = Intent(this, MyProfileActivity::class.java)
        startActivity(intent)
    }


    private fun setupViewPager() {
        val listFragments = listOf(
            TrainingType1Fragment(),
            TrainingType2Fragment(),
            TrainingType3Fragment(),
            TrainingType4Fragment()
        )

        viewPager.adapter = OnboardingScreenAdapter(
            this, listFragments
        )

        TabLayoutMediator(indicator, viewPager) { _, _ -> }.attach()
    }
}
