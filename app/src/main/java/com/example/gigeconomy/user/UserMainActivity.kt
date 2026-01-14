package com.example.gigeconomy.user

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.ActivityUserMainBinding
import com.example.gigeconomy.user.posts.UserPostFragment

class UserMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserMainBinding

    private lateinit var fragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {

        binding= ActivityUserMainBinding.inflate(layoutInflater)


        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.userHome.setOnClickListener{
            goToFragment(UserHomeFragment())
        }

        binding.userProfile.setOnClickListener{
            goToFragment(ProfileFragment())
        }

        binding.ongoingReq.setOnClickListener{
            goToFragment(OngoingReqFragment())
        }

        binding.userCreate.setOnClickListener{
            goToFragment(UserPostFragment())
        }




        supportActionBar?.hide()

    }

    private fun goToFragment(fragment:Fragment){

        fragmentManager=supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.container,fragment).commit()



    }
}