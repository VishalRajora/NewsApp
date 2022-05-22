package com.example.quantomproject.homepage.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quantomproject.homepage.repository.HomepageRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomepageViewModel @Inject constructor(val repo: HomepageRepo) : ViewModel() {

    fun getNews(country: String, apiKey: String) = viewModelScope.launch {
        repo.getNews(country, apiKey)
    }

    val getNews = repo.getNews

}