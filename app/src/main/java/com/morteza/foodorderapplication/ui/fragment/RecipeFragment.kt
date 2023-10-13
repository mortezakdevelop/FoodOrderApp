package com.morteza.foodorderapplication.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.morteza.foodorderapplication.R
import com.morteza.foodorderapplication.databinding.FragmentRecipeBinding
import com.morteza.foodorderapplication.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private lateinit var binding: FragmentRecipeBinding

    private val registerViewModel: RegisterViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecipeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            showUserName()
        }
    }

    @SuppressLint("SetTextI18n")
    suspend fun showUserName() {
        registerViewModel.readData.collect {
            binding.titleAvatarTv.text = "${getString(R.string.hello)}, ${it.username} ${getEmojiByUnicode()}"
        }
    }

    private fun getEmojiByUnicode(): String {
        return String(Character.toChars(0x1f44b))
    }
}