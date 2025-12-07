package com.example.gigeconomy.provider

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.gigeconomy.R
import com.example.gigeconomy.databinding.ProviderActivityMainBinding

class ProviderMainActivity : AppCompatActivity() {
    private lateinit var binding: ProviderActivityMainBinding

    private lateinit var fragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {

        binding= ProviderActivityMainBinding.inflate(layoutInflater)


        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.providerHome.setOnClickListener{
            goToFragment(HomeFragment())
        }

        binding.providerAdd.setOnClickListener{
            goToFragment(AddFragment())
        }



    }

    private fun goToFragment(fragment:Fragment){

        fragmentManager=supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.container,fragment).commit()



    }

}