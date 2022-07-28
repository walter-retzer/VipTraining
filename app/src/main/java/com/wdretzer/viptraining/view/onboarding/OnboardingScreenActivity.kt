package com.wdretzer.viptraining.view.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.window.SplashScreen
import androidx.viewpager2.widget.ViewPager2
import com.wdretzer.viptraining.view.util.CustomPageTransformer
import com.wdretzer.viptraining.view.util.HorizontalMarginItemDecoration
import com.wdretzer.viptraining.R
import com.wdretzer.viptraining.view.login.LoginActivity


// Classe com as Informações que serão visualizadas para sa Telas de Bem Vindo
class OnboardingScreenActivity : AppCompatActivity() {
    private val viewPager: ViewPager2 by lazy { findViewById(R.id.view_pager) }
    private val buttonNext: Button by lazy { findViewById(R.id.btn_next) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_screen)

        setupViewPager()
        checkPage()
    }


    // Método que retorna para a Activity: SplashScreen
    override fun onBackPressed() {
        val intent = Intent(this, SplashScreen::class.java)
        startActivity(intent)
    }


    // Método respponsável pelo setup do ViewPager
    private fun setupViewPager() {
        val listFragments = listOf(
            Onboarding1Fragment(),
            Onboarding2Fragment(),
            Onboarding3Fragment(),
        )

        viewPager.adapter = OnboardingScreenAdapter(
            this, listFragments
        )

        // Customização para que haja um efeito de animação, de modo a ver um pequena área da próxima tela.
        viewPager.addItemDecoration(
            HorizontalMarginItemDecoration(
                this,
                R.dimen.viewpager_current_item_horizontal_margin
            )
        )

        // Customização para alterar o tamanho do card da próxima tela, quando arrastar para ambos os lados
        viewPager.setPageTransformer(CustomPageTransformer(this))
        viewPager.offscreenPageLimit = 1
    }


    // Método que inicia a Activity: LoginActivity quando o botão "Próximo" for clicado.
    private fun checkButton() {
        buttonNext.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }


    // Método que muda para o próximo fragmento a ser exibido quando o botão "Próximo" for clicado.
    private fun checkButtonNext() {
        buttonNext.setOnClickListener {
            viewPager.currentItem = viewPager.currentItem + 1
        }
    }


    // Método responsável por verificar qual posição do viewPager e alterar a função do botão Next:
    private fun checkPage() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position == 2) {
                    buttonNext.text = getString(R.string.entendi)
                    checkButton()
                } else {
                    buttonNext.text = getString(R.string.proximo)
                    checkButtonNext()
                }
            }
        })
    }
}
