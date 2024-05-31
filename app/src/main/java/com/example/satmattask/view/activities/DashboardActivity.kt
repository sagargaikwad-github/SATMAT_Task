package com.example.satmattask.view.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.satmattask.R
import com.example.satmattask.auth.SignInActivity
import com.example.satmattask.databinding.ActivityDashboardBinding
import com.example.satmattask.model.Users
import com.example.satmattask.utils.Utils
import com.example.satmattask.view.fragments.HomeFragment
import com.example.satmattask.view.fragments.MenuFragment
import com.example.satmattask.view.fragments.ReportsFragment
import com.example.satmattask.view.fragments.SettlementsFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val toggle = ActionBarDrawerToggle(
            this,
            binding.dashboardDrawerLayout,
            binding.toolbar,
            R.string.nav_open,
            R.string.nav_close
        )

        binding.dashboardDrawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        binding.navigationDrawer.setNavigationItemSelectedListener(this)
        binding.toolbar.setNavigationIcon(R.drawable.ic_profile_icon)
        binding.toolbar.inflateMenu(R.menu.toolbar_menu);


        toolBarItemClick()
        bottomNavigationClick()
        openFragment(HomeFragment())
        setNavigationDrawerData()
    }

    private fun toolBarItemClick() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.toolbar_refresh -> {
                    Utils.showToast(this@DashboardActivity, "Refresh")
                }

                R.id.toolbar_volume -> {
                    Utils.showToast(this@DashboardActivity, "Volume")
                }

                R.id.toolbar_notifications -> {
                    Utils.showToast(this@DashboardActivity, "Notifications")
                }
            }
            false
        }
    }

    private fun bottomNavigationClick() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> openFragment(HomeFragment())
                R.id.bottom_settlements -> openFragment(SettlementsFragment())
                R.id.bottom_reports -> openFragment(ReportsFragment())
                R.id.bottom_menu -> openFragment(MenuFragment())
            }
            true
        }

    }


    //Setting Navigation Drawer header - Image,Phone and Email
    private fun setNavigationDrawerData() {
        val headerView: View = binding.navigationDrawer.getHeaderView(0)
        val profilePIC: ImageView = headerView.findViewById(R.id.drawerLayoutProfileIV)
        val phoneTV: TextView = headerView.findViewById(R.id.drawerLayoutPhoneTV)
        val emailTV: TextView = headerView.findViewById(R.id.drawerLayoutEmailTV)

        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid!!
        val storageReference =
            FirebaseStorage.getInstance().getReference("Profile")
                .child(currentUserID)
                .child("Profile.jpg")

        lifecycleScope.launch {
            try {
                val imageURL = storageReference.downloadUrl.await()
                Glide.with(this@DashboardActivity).load(imageURL).into(profilePIC)
            } catch (e: Exception) {
                Log.d("ProfilePic", e.toString())
            }
        }

        FirebaseDatabase.getInstance().getReference("Users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    for (item in snapshot.children) {
                        val user = item.getValue(Users::class.java)
                        if (user != null) {
                            if (user.userId == currentUserID) {
                                phoneTV.text = user.userPhone
                                emailTV.text = user.userEmail
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.d("Phone:Email", error.toString())
                }
            })


    }

    //Setting Navigation Drawer Item Clicks
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.drawer_profile -> Utils.showToast(this, "Profile")
            R.id.drawer_kyc -> Utils.showToast(this, "KYC")
            R.id.drawer_support -> Utils.showToast(this, "Support")
            R.id.drawer_settings -> Utils.showToast(this, "Settings")
            R.id.drawer_language -> Utils.showToast(this, "Language")
            R.id.drawer_termsCondtions -> Utils.showToast(this, "Terms and Conditions")
            R.id.drawer_privacyPolicy -> Utils.showToast(this, "Privacy Policy")
            R.id.drawer_faq -> Utils.showToast(this, "FAQ")
            R.id.drawer_share -> Utils.showToast(this, "Share")
            R.id.drawer_logout -> logout()
        }
        binding.dashboardDrawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    //LogOut User
    private fun logout() {
        val builder = AlertDialog.Builder(this)
        val alertDialog = builder.create()
        builder.setTitle("Log Out")
            .setMessage("Are you sure ? Do you want to logout?")
            .setPositiveButton("Yes") { _, _ ->
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, SignInActivity::class.java))
                finish()
            }
            .setNegativeButton("No") { _, _ ->
                alertDialog.dismiss()
            }
            .show()
            .setCancelable(false)
    }

    override fun onBackPressed() {
        if (binding.dashboardDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.dashboardDrawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun openFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.dashboardFrameLayout, fragment)
        fragmentTransaction.commit()
    }
}