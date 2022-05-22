package com.example.quantomproject.homepage.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.developers.shopapp.utils.snackbar
import com.example.quantomproject.MainActivity
import com.example.quantomproject.R
import com.example.quantomproject.databinding.FragmentHomepageBinding
import com.example.quantomproject.homepage.adapter.NewsAdapter
import com.example.quantomproject.homepage.viewmodel.HomepageViewModel
import com.example.quantomproject.utils.Constant
import com.example.quantomproject.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomepageFragment : Fragment(R.layout.fragment_homepage) {
    private lateinit var binding: FragmentHomepageBinding
    private lateinit var navController: NavController
    private lateinit var newsAdapter: NewsAdapter
    private val homePageViewModel: HomepageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomepageBinding.bind(view)
        navController = Navigation.findNavController(view)
        requireActivity().window.statusBarColor = resources.getColor(R.color.blue)
        homePageViewModel.getNews("in", Constant.API_KEY)
        observables()
    }

    private fun observables() {
        lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    homePageViewModel.getNews.collect {
                        val activity = activity as MainActivity
                        when (it) {
                            is Resource.Error -> {
                                activity.hideProgressBar()
                                snackbar(it.message!!)
                            }
                            is Resource.Init -> {

                            }
                            is Resource.Loading -> {
                                activity.showProgressBar()
                            }
                            is Resource.Success -> {
                                activity.hideProgressBar()
                                newsAdapter = NewsAdapter()
                                newsAdapter.submitList(it.data?.articles)
                                binding.rvNews.adapter = newsAdapter
                            }
                        }
                    }
                }
            }
        }
    }

}