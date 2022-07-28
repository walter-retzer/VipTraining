package com.wdretzer.viptraining.view.menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.view.addtraining.ChooseTrainingActivity
import com.wdretzer.viptraining.view.readdatafromfirestore.ReadDataFromFirestoreActivity
import com.wdretzer.viptraining.view.onboarding.OnboardingScreenAdapter
import com.wdretzer.viptraining.view.profile.EditProfileActivity
import com.wdretzer.viptraining.view.searchdatainfirestore.SearchDataInFirestoreActivity
import java.text.SimpleDateFormat
import java.util.*


// Classe Responsável pelo Menu Principal do Aplicativo:
class MainMenuActivity : AppCompatActivity() {

    private val viewPager: ViewPager2 by lazy { findViewById(R.id.view_pager_insert_training) }
    private val indicator: TabLayout by lazy { findViewById(R.id.indicator) }

    private val btnAddTraining: ShapeableImageView
        get() = findViewById(R.id.btn_add_exercise)

    private val btnProfile: ShapeableImageView
        get() = findViewById(R.id.btn_profile)

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

        // Exibe a data atual na tela:
        val sdf = SimpleDateFormat("E, dd-MM-yyyy")
        val currentDate = sdf.format(Date())
        textDate.text = currentDate.toString()

        // Verifica clique do usuário:
        btnAddTraining.setOnClickListener { sendToChooseTraining() }
        btnSearchTraining.setOnClickListener { sendToSearchTraining() }
        btnMyTraining.setOnClickListener { sendToMyTraining() }
        btnProfile.setOnClickListener { sendToEditProfile() }
    }


    // Método responsável por iniciar a Activity: MainMenuActivity
    private fun sendToChooseTraining() {
        val intent = Intent(this, ChooseTrainingActivity::class.java)
        startActivity(intent)
    }


    // Método responsável por iniciar a Activity: SearchDataInFirestoreActivity
    private fun sendToSearchTraining() {
        val intent = Intent(this, SearchDataInFirestoreActivity::class.java)
        startActivity(intent)
    }


    // Método responsável por iniciar a Activity: ReadDataFromFirestoreActivity
    private fun sendToMyTraining() {
        val intent = Intent(this, ReadDataFromFirestoreActivity::class.java)
        startActivity(intent)
    }


    // Método responsável por iniciar a Activity: EditProfileActivity
    private fun sendToEditProfile() {
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
    }


    // Método responsável pelo viewPager do Menu com os tipos de Treinos:
    private fun setupViewPager() {
        val listFragments = listOf(
            TrainingType1Fragment(),
            TrainingType2Fragment(),
            TrainingType3Fragment(),
            TrainingType4Fragment()
        )

        // carrega a lista dos fragments para serem exibidos
        viewPager.adapter = OnboardingScreenAdapter(
            this, listFragments
        )

        // exibe o ponto que muda de cor, conforme a posição do ViewPager:
        TabLayoutMediator(indicator, viewPager) { _, _ -> }.attach()
    }
}
