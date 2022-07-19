package com.wdretzer.viptraining.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.wdretzer.nasaprojetointegrador.util.CustomPageTransformer
import com.wdretzer.nasaprojetointegrador.util.HorizontalMarginItemDecoration
import com.wdretzer.viptraining.MainActivity
import com.wdretzer.viptraining.R


class OnboardingScreenActivity : AppCompatActivity() {
    private val viewPager: ViewPager2 by lazy { findViewById(R.id.view_pager) }
    private val buttonNext: Button by lazy { findViewById(R.id.btn_next) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_screen)

        setupViewPager()
        checkPage()
    }

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

    private fun checkButton() {
        buttonNext.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun checkButtonNext() {
        buttonNext.setOnClickListener {
            viewPager.currentItem = viewPager.currentItem + 1
        }
    }

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
