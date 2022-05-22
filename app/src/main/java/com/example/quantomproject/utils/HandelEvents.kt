package com.example.quantomproject.utils

sealed class HandelEvents {
    object ShowLoading : HandelEvents()
    class ShowSuccessMessages(val messages: String) : HandelEvents()
    class ShowErrorMessages(val messages: String) : HandelEvents()
}