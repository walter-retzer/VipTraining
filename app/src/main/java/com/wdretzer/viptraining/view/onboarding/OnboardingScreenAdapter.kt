package com.wdretzer.viptraining.view.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


// Classe Adapter das Telas de Bem-Vindo:
class OnboardingScreenAdapter(
    fragmentManager: FragmentActivity,
    private val views: List<Fragment>
) : FragmentStateAdapter(fragmentManager) {

    override fun getItemCount(): Int = views.size

    override fun createFragment(position: Int): Fragment {
        return if (views.isEmpty()) {
            throw IllegalArgumentException("The view is empty")
        } else {
            views[position]
        }
    }
}
