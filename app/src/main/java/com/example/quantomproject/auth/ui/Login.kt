package com.example.quantomproject.auth.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.quantomproject.databinding.FragmentMainLoginBinding
import com.example.quantomproject.utils.HandelEvents
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class Login : Fragment(R.layout.fragment_main_login) {

    private lateinit var binding: FragmentMainLoginBinding
    private lateinit var navController: NavController
    private lateinit var parentLogin: LoginFragment
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val rCSignIn = 89
    private val callbackManager = CallbackManager.Factory.create()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainLoginBinding.bind(view)
        navController = findNavController()
        googleSignIn()
        onClicksFuntions()
        observer()
        facebookLogin()
    }

    private fun facebookLogin() {
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    val credential =
                        FacebookAuthProvider.getCredential(loginResult.accessToken.token)
                    authViewModel.googleSignIn(credential)
                }

                override fun onCancel() {
                    Log.d("Login", "facebook:onCancel")
                }

                override fun onError(error: FacebookException) {
                    Log.d("Login", "facebook:onError", error)
                }
            })
    }

    private fun googleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_auth))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun onClicksFuntions() {

        binding.loginButton.setOnClickListener {
            authViewModel.logIn(
                binding.etEmail,
                binding.etPassword
            )
        }

        binding.ivGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, rCSignIn)
        }

        binding.ivFacebook.setOnClickListener {
            LoginManager.getInstance()
                .logInWithReadPermissions(this, listOf("email", "public_profile"))
        }

        binding.signUp.setOnClickListener {
            parentLogin.getNextTab()
        }
    }

    private fun observer() {
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
                                snackbar(it.messages)
                                activity.hideProgressBar()
                                navController.navigate(R.id.action_loginFragment_to_homepageFragment)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentLogin = LoginFragment()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
        if (requestCode == rCSignIn) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                authViewModel.googleSignIn(credential)
                Log.d("Login", "Google:onSuccess:${account.idToken!!}")
            } catch (e: ApiException) {
                e.localizedMessage
            }

        }
    }

}
