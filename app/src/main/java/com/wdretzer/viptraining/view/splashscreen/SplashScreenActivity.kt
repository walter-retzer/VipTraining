package com.wdretzer.viptraining.view.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.view.onboarding.OnboardingScreenActivity


// Classe responsável pela Tela de Abertura do Aplicativo com a animação
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sendToOnboardingScreen()
    }

    // Método responsável por iniciar a Activity: OnboardingScreenActivity
    private fun sendToOnboardingScreen() {
        Handler().postDelayed({
            val intent = Intent(this, OnboardingScreenActivity::class.java)
            startActivity(intent)
        }, 3000)
    }
}
