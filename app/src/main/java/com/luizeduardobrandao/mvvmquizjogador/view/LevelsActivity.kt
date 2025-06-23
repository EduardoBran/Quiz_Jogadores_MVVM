package com.luizeduardobrandao.mvvmquizjogador.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luizeduardobrandao.mvvmquizjogador.R
import com.luizeduardobrandao.mvvmquizjogador.databinding.ActivityLevelsBinding
import com.luizeduardobrandao.mvvmquizjogador.repository.BannerAds

class LevelsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLevelsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLevelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura a Toolbar (binding.toolbarMain) como ActionBar da Activity
        setSupportActionBar(binding.toolbarLevels)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // carrega o banner no container da view binding
        BannerAds.loadBanner(this, binding.frameBanner)
    }
}