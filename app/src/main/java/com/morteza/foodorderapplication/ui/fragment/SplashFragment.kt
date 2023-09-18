package com.morteza.foodorderapplication.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import coil.load
import com.morteza.foodorderapplication.BuildConfig
import com.morteza.foodorderapplication.R
import com.morteza.foodorderapplication.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            bgImg.load(R.drawable.bg_splash)
            versionTxt.text = "${getString(R.string.version)} : ${BuildConfig.VERSION_NAME}"

            lifecycleScope.launchWhenCreated {

            }
        }
    }
}
