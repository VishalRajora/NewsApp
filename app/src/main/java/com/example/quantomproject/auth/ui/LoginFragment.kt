package com.example.quantomproject.auth.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.quantomproject.R
import com.example.quantomproject.auth.viewmodel.AuthViewModel
import com.example.quantomproject.databinding.FragmentLoginBinding
import com.example.quantomproject.utils.ChangeTab
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login), ChangeTab {

    lateinit var binding: FragmentLoginBinding
    private lateinit var adapter: ViewPagerAdapter
    var viewPager: ViewPager2? = null
    var tab: TabLayout? = null
    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        requireActivity().window.statusBarColor = resources.getColor(R.color.red)
        initView()
        viewPager = binding.viewPager
        tab = binding.tabLayout

        Log.i("LoginFragment", "getNextTab: ${tab?.tabCount}")

        authViewModel.changeTab.observe(viewLifecycleOwner) {
            Log.i("LoginFragment", "onViewCreated: $it ")
            Log.i("LoginFragment", "getNextTab: ${tab?.tabCount}")
            if (it == tab?.tabCount) {
                viewPager?.currentItem = 0
            } else {
                viewPager?.currentItem = 1
            }
        }

    }


    private fun initView() {
        setupViewPager(binding.viewPager)
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = adapter.mFragmentTitleList[position]
        }.attach()

    }

    private fun setupViewPager(viewPager: ViewPager2) {
        adapter = ViewPagerAdapter(
            activity!!.supportFragmentManager,
            activity!!.lifecycle
        )
        adapter.addFragment(Login(), "login")
        adapter.addFragment(SignupFragment(), "sign up")
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = 1
    }

    override fun getNextTab() {

        Log.i("LoginFragment", "getNextTab: ${tab?.selectedTabPosition}")
    }

    override fun gotoPrvTab() {

    }


    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
        FragmentStateAdapter(fragmentManager, lifecycle) {

        val mFragmentList: MutableList<Fragment> = ArrayList()
        val mFragmentTitleList: MutableList<String> = ArrayList()

        fun addFragment(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun createFragment(position: Int): Fragment {
            return mFragmentList[position]
        }

        override fun getItemCount(): Int {
            return mFragmentList.size
        }
    }

}