package com.example.satmattask.view.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.satmattask.R
import com.example.satmattask.auth.SignInActivity
import com.example.satmattask.databinding.ActivityDashboardBinding
import com.example.satmattask.databinding.ChooseImageIntentBinding
import com.example.satmattask.databinding.DialogForgotPasswordBinding
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
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.log

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityDashboardBinding
    var userImageURI: Uri? = null
    private lateinit var profilePic: ImageView
    private lateinit var photoCaptureUri: Uri
    private lateinit var photoFile: File

    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: $uri")
                saveProfileImage(FirebaseAuth.getInstance().currentUser?.uid, uri)
            } else {
                Log.d("PhotoPicker", "No media selected")
            }
        }


    private val selectedImage = registerForActivityResult(ActivityResultContracts.GetContent())
    {
        userImageURI = it
        saveProfileImage(FirebaseAuth.getInstance().currentUser?.uid, userImageURI)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        statusBarColor()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(v.paddingLeft, statusBarInsets.top, v.paddingRight, v.paddingBottom)
            insets
        }

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
        profilePic = headerView.findViewById(R.id.drawerLayoutProfileIV)
        val phoneTV: TextView = headerView.findViewById(R.id.drawerLayoutPhoneTV)
        val emailTV: TextView = headerView.findViewById(R.id.drawerLayoutEmailTV)
        val userProfileChange: ImageView = headerView.findViewById(R.id.changeProfile)

        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid!!
        val storageReference =
            FirebaseStorage.getInstance().getReference("Profile")
                .child(currentUserID)
                .child("Profile.jpg")

        lifecycleScope.launch {
            try {
                val imageURL = storageReference.downloadUrl.await()
                Glide.with(this@DashboardActivity).load(imageURL).into(profilePic)
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

        userProfileChange.setOnClickListener {
//            selectedImage.launch("image/*")

            //pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            photoCaptureUri = Uri.EMPTY
            openImageChooser()
        }

    }

    private fun openImageChooser() {
        val dialog = ChooseImageIntentBinding.inflate(LayoutInflater.from(this@DashboardActivity))
        val actionDialog =
            AlertDialog.Builder(this@DashboardActivity).setView(dialog.root)
                .create()


        val chooseImageCamera = dialog.chooseImageCamera
        val chooseImageGallery = dialog.chooseImageGallery
        val chooseImageCancel = dialog.chooseImageCancel

        chooseImageCamera.setOnClickListener {
            openCamera()
            actionDialog.dismiss()
        }

        chooseImageGallery.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            actionDialog.dismiss()
        }

        chooseImageCancel.setOnClickListener {
            actionDialog.dismiss()
        }
        actionDialog.show()
    }

    private fun openCamera() {
        photoFile = createImageFile()

        val photoURI: Uri = FileProvider.getUriForFile(
            this,
            "com.example.satmattask.fileprovider",
            photoFile
        )
        photoCaptureUri = photoURI

        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoURI)
        startActivityForResult(cameraIntent, 110)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                110 -> {
                    if (photoCaptureUri != null) {
                        saveProfileImage(
                            FirebaseAuth.getInstance().currentUser?.uid,
                            photoCaptureUri
                        )
                    }
                }
            }
        }
    }

    private fun saveProfileImage(currentUserID: String?, userImageURI: Uri?) {
        Utils.showDialog(this@DashboardActivity)

        val storageReference =
            FirebaseStorage.getInstance().getReference("Profile").child(currentUserID!!)
                .child("Profile.jpg")
        lifecycleScope.launch {
            try {
                val uploadTask = storageReference.putFile(userImageURI!!).await()
                if (uploadTask.task.isSuccessful) {
                    Glide.with(this@DashboardActivity).load(uploadTask.storage.downloadUrl.await())
                        .into(profilePic)
                    Utils.hideDialog()
                    Toast.makeText(
                        this@DashboardActivity,
                        "Profile updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()


                } else {
                    Utils.hideDialog()
                    Toast.makeText(
                        this@DashboardActivity,
                        "Failed to change profile",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            } catch (e: Exception) {
                Utils.hideDialog()
                Toast.makeText(this@DashboardActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun deleteImage(photoUri: Uri) {
        val myContentResolver = this@DashboardActivity.contentResolver
        val deletedFile = myContentResolver.delete(photoUri, null, null)
        if (deletedFile > 0) {
            Log.d("Delete File", "Profile Pic deleted from gallery")
        }
    }

    //Setting Navigation Drawer Item Clicks
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.changeProfile -> Utils.showToast(this, "Change Profile")
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

    fun statusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = this.getWindow()
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = this.resources.getColor(R.color.colorPrimaryStatusBar)
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String =
            SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File? = getExternalFilesDir(null)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            photoCaptureUri = Uri.fromFile(this)
        }
    }
}