package com.saba.gnbtrades.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.saba.gnbtrades.R
import com.saba.gnbtrades.databinding.ActivitySplashBinding
import com.saba.gnbtrades.databinding.ActivityTransactionsBinding
import com.saba.gnbtrades.transaction.ui.TransactionsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModels()

    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.state.observe(this) {
            when {
                it.rates != null -> goToTransactions()
            }
        }
    }

    private fun goToTransactions() {
        startActivity(Intent(this, TransactionsActivity::class.java))
    }


}

