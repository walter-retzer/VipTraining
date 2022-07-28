package com.wdretzer.viptraining.view.onboarding

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.wdretzer.viptraining.R


class Onboarding2Fragment : Fragment(R.layout.fragment_onboarding2){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
