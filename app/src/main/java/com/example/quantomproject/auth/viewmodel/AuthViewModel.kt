package com.example.quantomproject.auth.viewmodel

import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quantomproject.auth.repository.AuthRepo
import com.example.quantomproject.utils.HandelEvents
import com.example.quantomproject.utils.MyValidation
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: AuthRepo) : ViewModel() {

    private val _viewStatus = Channel<HandelEvents>()
    val viewStatus = _viewStatus.receiveAsFlow()

    private val _changeTab = MutableLiveData<Int>()
    val changeTab: LiveData<Int> = _changeTab

    fun signUp(
        etEmail: EditText,
        etPassword: EditText,
        etName: EditText,
        etNumber: EditText,
        ckTerms: CheckBox
    ) = viewModelScope.launch {
        Dispatchers.IO
        val userEmail = etEmail.text.toString().trim()
        val userPassword = etPassword.text.toString().trim()
        val userName = etName.text.toString().trim()
        val userNumber = etNumber.text.toString().trim()

        when {
            userName.isEmpty() -> {
                etName.error = "Name can't be empty"
                _viewStatus.send(HandelEvents.ShowErrorMessages("Name can't be empty"))
            }
            userEmail.isEmpty() -> {
                etEmail.error = "Email can't be empty"
                _viewStatus.send(HandelEvents.ShowErrorMessages("Email can't be empty"))
            }
            !MyValidation.isValidEmail(email = userEmail) -> {
                etEmail.error = "Email is not valid"
                _viewStatus.send(HandelEvents.ShowErrorMessages("Email is not valid"))
            }
            userNumber.isEmpty() -> {
                etNumber.error = "Number can't be empty"
                _viewStatus.send(HandelEvents.ShowErrorMessages("Number can't be empty"))
            }
//            !MyValidation.validateMobile(mobile = userNumber) -> {
//                etNumber.error = "Number is not valid"
//                _viewStatus.send(HandelEvents.ShowErrorMessages("Number is not valid"))
//            }
            userPassword.isEmpty() -> {
                etPassword.error = "Password can't be empty"
                _viewStatus.send(HandelEvents.ShowErrorMessages("Password can't be empty"))
            }
            !ckTerms.isChecked -> {
                _viewStatus.send(HandelEvents.ShowErrorMessages("Please check terms and condition"))
            }
            else -> {
                repo.signUp(userEmail, userPassword)
            }
        }


    }

    fun logIn(email: EditText, password: EditText) = viewModelScope.launch {
        Dispatchers.IO
        val userEmail = email.text.toString().trim()
        val userPassword = password.text.toString().trim()
        when {
            userEmail.isEmpty() -> {
                email.error = "Email can't be empty"
                _viewStatus.send(HandelEvents.ShowErrorMessages("Email can't be empty"))
            }

            !MyValidation.isValidEmail(email = userEmail) -> {
                email.error = "Email is not valid"
                _viewStatus.send(HandelEvents.ShowErrorMessages("Email is not valid"))
            }

            userPassword.isEmpty() -> {
                password.error = "Password can't be empty"
                _viewStatus.send(HandelEvents.ShowErrorMessages("Password can't be empty"))
            }
            else -> {
                repo.logIn(userEmail, userPassword)
            }
        }
    }

    fun googleSignIn(credential: AuthCredential) = viewModelScope.launch {
        Dispatchers.IO
        repo.googleSignIn(credential)
    }

    fun changeTab() = viewModelScope.launch {
        Dispatchers.IO
        _changeTab.value = 1
        Log.i("AuthView", "changeTab:${_changeTab.value} ")
    }

    val signUpStatus = repo.signUpStatus
}