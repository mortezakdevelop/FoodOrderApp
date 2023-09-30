package com.morteza.foodorderapplication.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.morteza.foodorderapplication.BuildConfig
import com.morteza.foodorderapplication.R
import com.morteza.foodorderapplication.databinding.FragmentSplashBinding
import com.morteza.foodorderapplication.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding
    private val viewModel: RegisterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                delay(2500)
                viewModel.readData.asLiveData().observe(viewLifecycleOwner) {
                    //findNavController().popBackStack(R.id.splashFragment, true)
                    if (it.username.isNotEmpty()) {
                        findNavController().navigate(R.id.action_splashFragment_to_recipeFragment)
                    } else {
                        findNavController().navigate(R.id.action_splashFragment_to_registerFragment)
                    }
                }
            }
        }
    }
}
