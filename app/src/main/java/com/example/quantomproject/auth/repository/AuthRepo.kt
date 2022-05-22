package com.example.quantomproject.auth.repository

import android.util.Log
import com.example.quantomproject.utils.HandelEvents
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepo @Inject constructor(private var auth: FirebaseAuth) {


    private val _signUpStatus = Channel<HandelEvents>()
    val signUpStatus = _signUpStatus.receiveAsFlow()

    suspend fun signUp(email: String, password: String) {
        _signUpStatus.send(HandelEvents.ShowLoading)
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

            CoroutineScope(Dispatchers.IO).launch {
                if (it.isSuccessful) {
                    _signUpStatus.send(HandelEvents.ShowSuccessMessages("Done"))
                } else {
                    _signUpStatus.send(HandelEvents.ShowErrorMessages("Failed"))
                }
            }

        }.addOnFailureListener {
            CoroutineScope(Dispatchers.IO).launch {
                _signUpStatus.send(HandelEvents.ShowErrorMessages("Failed"))
            }
        }
    }

    suspend fun logIn(email: String, password: String) {
        _signUpStatus.send(HandelEvents.ShowLoading)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {

            CoroutineScope(Dispatchers.IO).launch {
                if (it.isSuccessful) {
                    _signUpStatus.send(HandelEvents.ShowSuccessMessages("Done"))
                } else {
                    _signUpStatus.send(HandelEvents.ShowErrorMessages("Failed"))
                }
            }

        }.addOnFailureListener {
            CoroutineScope(Dispatchers.IO).launch {
                _signUpStatus.send(HandelEvents.ShowErrorMessages("Failed"))
            }

        }
    }


    suspend fun googleSignIn(credential: AuthCredential) {
        try {
            _signUpStatus.send(HandelEvents.ShowLoading)
            auth.signInWithCredential(credential).addOnCompleteListener {
                CoroutineScope(Dispatchers.IO).launch {
                    if (it.isSuccessful) {
                        _signUpStatus.send(HandelEvents.ShowSuccessMessages("Done"))
                    } else {
                        _signUpStatus.send(HandelEvents.ShowErrorMessages("Failed"))
                    }
                }

            }.addOnFailureListener {
                CoroutineScope(Dispatchers.IO).launch {
                    _signUpStatus.send(HandelEvents.ShowErrorMessages(it.localizedMessage))
                }

            }

        } catch (e: Exception) {
            _signUpStatus.send(HandelEvents.ShowErrorMessages("Failed"))
            Log.i("RepoAuth", "googleSignIn: catch${e.localizedMessage} ")
        }

    }
}
