package com.example.quantomproject

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.quantomproject.databinding.ActivityMainBinding
import com.example.quantomproject.utils.MyProgressBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MyProgressBar {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun showProgressBar() {
        binding.pbLoading.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.pbLoading.visibility = View.GONE
    }
}