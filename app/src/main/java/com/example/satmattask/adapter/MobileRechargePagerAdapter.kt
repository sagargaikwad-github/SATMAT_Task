package com.example.satmattask.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.satmattask.view.fragments.PostpaidRechargeFragment
import com.example.satmattask.view.fragments.PrepaidRechargeFragment

class MobileRechargePagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 2;
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return PrepaidRechargeFragment()
            1 -> return PostpaidRechargeFragment()
            else -> return PrepaidRechargeFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "PREPAID"
            1 -> return "POSTPAID"
            else -> return "PREPAID"
        }

        return super.getPageTitle(position)
    }
}