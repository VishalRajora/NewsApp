package com.example.quantomproject.auth.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.developers.shopapp.utils.snackbar
import com.example.quantomproject.MainActivity
import com.example.quantomproject.R
import com.example.quantomproject.auth.viewmodel.AuthViewModel
import com.example.quantomproject.databinding.FragmentSignupBinding
import com.example.quantomproject.utils.HandelEvents
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment(R.layout.fragment_signup) {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var navController: NavController
    private val authViewModel: AuthViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignupBinding.bind(view)
        navController = findNavController()


        binding.loginButton.setOnClickListener {
            authViewModel.signUp(
                binding.etEmail,
                binding.etPassword,
                binding.etName,
                binding.etNumber,
                binding.ckTerms

            )

            lifecycleScope.launchWhenCreated {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    launch {
                        authViewModel.viewStatus.collect {
                            when (it) {
                                is HandelEvents.ShowErrorMessages -> {
                                    snackbar(it.messages)
                                }
                                else -> {
                                    return@collect
                                }
                            }
                        }
                    }
                    launch {
                        authViewModel.signUpStatus.collect {
                            val activity = activity as MainActivity
                            when (it) {
                                is HandelEvents.ShowErrorMessages -> {
                                    activity.hideProgressBar()
                                    snackbar(it.messages)
                                }
                                HandelEvents.ShowLoading -> {
                                    activity.showProgressBar()

                                }
                                is HandelEvents.ShowSuccessMessages -> {
                                    activity.hideProgressBar()
                                    snackbar(it.messages)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}