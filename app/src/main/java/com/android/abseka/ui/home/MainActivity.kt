package com.android.abseka.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.abseka.R
import com.android.abseka.databinding.ActivityMainBinding
import com.android.abseka.ui.maps.MapsFragment
import com.android.abseka.ui.profile.ProfileFragment
import com.android.abseka.ui.rekap.RekapFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.apply {
            bottomNavView.background = null

            bottomNavView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.home -> replaceFragment(HomeFragment())
                    R.id.location -> replaceFragment(MapsFragment())
                    R.id.rekap -> replaceFragment(RekapFragment())
                    R.id.profile -> replaceFragment(ProfileFragment())
                }
                true
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }
}