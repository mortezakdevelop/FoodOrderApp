package com.morteza.foodorderapplication.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.morteza.foodorderapplication.R
import com.morteza.foodorderapplication.adapter.PopularAdapter
import com.morteza.foodorderapplication.adapter.RecentAdapter
import com.morteza.foodorderapplication.databinding.FragmentRecipeBinding
import com.morteza.foodorderapplication.models.ResponseRecipes
import com.morteza.foodorderapplication.utils.Constants
import com.morteza.foodorderapplication.utils.NetworkRequest
import com.morteza.foodorderapplication.utils.setupRecyclerview
import com.morteza.foodorderapplication.utils.showSnackBar
import com.morteza.foodorderapplication.viewmodel.RecipeViewModel
import com.morteza.foodorderapplication.viewmodel.RegisterViewModel
import com.todkars.shimmer.ShimmerRecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import javax.inject.Inject

@AndroidEntryPoint
class RecipeFragment : Fragment() {

    private lateinit var binding: FragmentRecipeBinding
    private val viewModel:RecipeViewModel by viewModels()

    @Inject
    lateinit var popularAdapter:PopularAdapter

    @Inject
    lateinit var recentAdapter: RecentAdapter
    private var autoScrollIndex = 0



    private val registerViewModel: RegisterViewModel by viewModels()

    private val args:RecipeFragmentArgs by navArgs()
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
        loadPopularData()
        loadRecentData()
        callPopularData()
        callRecentData()
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

    private fun callPopularData(){
        initPopularRecycler()
        viewModel.readPopularDataFromDb.observe(viewLifecycleOwner){database ->
            if(database.isNotEmpty()){
                database[0].response.results?.let { result ->
                    setupLoading(false, binding.popularListRv)
                    fillPopularAdapter(result.toMutableList())
                }
            } else {
                viewModel.callPopularApi(viewModel.popularQueries())
            }
        }
    }

    private fun loadPopularData(){
        binding.apply {
            viewModel.popularData.observe(viewLifecycleOwner){ response ->
                when(response){
                    is NetworkRequest.Loading ->{
                        setupLoading(true,popularListRv)
                    }
                    is NetworkRequest.Success -> {
                        setupLoading(false,popularListRv)
                        response.data?.let { data ->
                            if (data.results!!.isNotEmpty()) {
                                fillPopularAdapter(data.results.toMutableList())
                            }
                        }
                    }
                    is NetworkRequest.Error -> {
                        setupLoading(false, popularListRv)
                        root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }

    private fun fillPopularAdapter(result: MutableList<ResponseRecipes.Result>) {
        popularAdapter.setData(result)
        //autoScrollPopular(result)
    }

    private fun autoScrollPopular(list: List<ResponseRecipes.Result>) {
        lifecycleScope.launchWhenCreated {
            repeat(Constants.REPEAT_TIME) {
                delay(Constants.DELAY_TIME)
                if (autoScrollIndex < list.size) {
                    autoScrollIndex += 1
                } else
                    autoScrollIndex  = 0

                binding.popularListRv.smoothScrollToPosition(autoScrollIndex)
            }
        }
    }


    private fun initPopularRecycler() {
        val snapHelper = LinearSnapHelper()
        binding.popularListRv.setupRecyclerview(
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false),
            popularAdapter
        )
        //Snap
        snapHelper.attachToRecyclerView(binding.popularListRv)
        //Click
        popularAdapter.setOnItemClickListener {
            //gotoDetailPage(it)
        }
    }

    private fun setupLoading(isShownLoading: Boolean, shimmer: ShimmerRecyclerView) {
        shimmer.apply {
            if (isShownLoading) showShimmer() else hideShimmer()
        }
    }

    //recent

    private fun callRecentData() {
        initRecentRecycler()
        viewModel.readRecentFromDb.observe(viewLifecycleOwner) { database ->
            if (database.isNotEmpty() && database.size > 1 && !args.isUpdateData) {
                database[1].response.results?.let { result ->
                    setupLoading(false, binding.recipesList)
                    recentAdapter.setData(result)
                }
            } else {
                viewModel.callRecentApi(viewModel.recentQueries())
            }
        }
    }
    private fun loadRecentData() {
        initRecentRecycler()
        viewModel.callRecentApi(viewModel.recentQueries())
        binding.apply {
            viewModel.recentData.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is NetworkRequest.Loading -> {
                        setupLoading(true, recipesList)
                    }
                    is NetworkRequest.Success -> {
                        setupLoading(false, recipesList)
                        response.data?.let { data ->
                            if (data.results!!.isNotEmpty()) {
                                recentAdapter.setData(data.results)
                            }
                        }
                    }
                    is NetworkRequest.Error -> {
                        setupLoading(false, recipesList)
                        binding.root.showSnackBar(response.message!!)
                    }
                }
            }
        }
    }

    private fun initRecentRecycler() {
        binding.recipesList.setupRecyclerview(
            LinearLayoutManager(requireContext()),
            recentAdapter
        )
        //Click
        recentAdapter.setOnItemClickListener {
           // gotoDetailPage(it)
        }
    }
}