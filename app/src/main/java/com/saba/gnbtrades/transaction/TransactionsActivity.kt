package com.saba.gnbtrades.transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.saba.gnbtrades.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
