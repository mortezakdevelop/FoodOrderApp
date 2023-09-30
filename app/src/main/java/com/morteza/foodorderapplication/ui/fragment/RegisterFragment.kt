package com.morteza.foodorderapplication.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.morteza.foodorderapplication.R
import com.morteza.foodorderapplication.databinding.FragmentRegisterBinding
import com.morteza.foodorderapplication.models.BodyRegister
import com.morteza.foodorderapplication.utils.Constants
import com.morteza.foodorderapplication.utils.NetworkRequest
import com.morteza.foodorderapplication.utils.showSnackBar
import com.morteza.foodorderapplication.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    lateinit var binding: FragmentRegisterBinding

    @Inject
    lateinit var body: BodyRegister

//    @Inject
//    lateinit var networkChecker: NetworkChecker

    //Other
    private val viewModel: RegisterViewModel by viewModels()
    private var email = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        binding.apply {
            coverImg.load(R.drawable.register_logo)
            //Email
            emailEdt.addTextChangedListener {
                if (it.toString().contains("@")) {
                    email = it.toString()
                    emailTxtLay.error = ""
                } else {
                    emailTxtLay.error = getString(R.string.emailNotValid)
                }
            }

            //Click
            submitBtn.setOnClickListener {
                val firstname = nameEdt.text.toString()
                val lastName = lastNameEdt.text.toString()
                val username = usernameEdt.text.toString()
                //Body
                body.email = email
                body.firstName = firstname
                body.lastName = lastName
                body.username = username
                //Check network
                lifecycleScope.launchWhenStarted {
                    //Call api
                    viewModel.callRegisterApi(Constants.MY_API_KEY, body)

                }
            }
            //Load data
            loadRegisterData()
        }
        return binding.root
    }

    private fun loadRegisterData() {
        viewModel.registerData.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkRequest.Loading -> {

                }

                is NetworkRequest.Success -> {
                    response.data?.let { data ->
                        viewModel.saveData(data.username.toString(), data.hash.toString())
                        findNavController().popBackStack(R.id.registerFragment, true)
                        findNavController().navigate(R.id.action_registerFragment_to_recipeFragment)
                    }
                }

                is NetworkRequest.Error -> {
                    binding.root.showSnackBar(response.message!!)

                }
            }
        }
    }
}