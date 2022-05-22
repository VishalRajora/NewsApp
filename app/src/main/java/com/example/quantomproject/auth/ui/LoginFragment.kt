package com.example.quantomproject.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.quantomproject.R
import com.example.quantomproject.databinding.FragmentLoginBinding
import com.example.quantomproject.utils.ChangeTab
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class LoginFragment : Fragment(R.layout.fragment_login), ChangeTab {
    lateinit var binding: FragmentLoginBinding
    private lateinit var adapter: ViewPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)
        requireActivity().window.statusBarColor = resources.getColor(R.color.red);
        initView()
    }

    private fun initView() {
        setupViewPager(binding.viewPager)
        TabLayoutMediator(
            binding.tabLayout, binding.viewPager
        ) { tab: TabLayout.Tab, position: Int ->
            tab.text = adapter.mFragmentTitleList[position]
        }.attach()


        for (i in 0 until binding.tabLayout.tabCount) {
            val tv = LayoutInflater.from(activity)
                .inflate(R.layout.custom_tab, null) as TextView
            binding.tabLayout.getTabAt(i)!!.customView = tv
        }
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
        binding.viewPager.currentItem = 1
    }

    override fun gotoPrvTab() {
        binding.viewPager.currentItem = 0
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