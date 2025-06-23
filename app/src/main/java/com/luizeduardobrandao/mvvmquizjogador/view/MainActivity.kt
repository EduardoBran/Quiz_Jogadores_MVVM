package com.luizeduardobrandao.mvvmquizjogador.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luizeduardobrandao.mvvmquizjogador.R
import com.luizeduardobrandao.mvvmquizjogador.databinding.ActivityMainBinding
import com.luizeduardobrandao.mvvmquizjogador.repository.BannerAds
import com.luizeduardobrandao.mvvmquizjogador.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 2) Configura a Toolbar (binding.toolbarMain) como ActionBar da Activity
        setSupportActionBar(binding.toolbarMain)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // inicializa SDK
        BannerAds.initialize(this)

        setListeners()
        setObservers()

        // carrega o banner no container da view binding
        BannerAds.loadBanner(this, binding.frameBanner)
    }

    // Disparar evento no ViewModel
    private fun setListeners(){
        binding.btnStart.setOnClickListener {
            viewModel.onStartQuizClicked()
        }
    }

    // Observar o evento e navegar
    private fun setObservers() {
        viewModel.navigateToLevels.observe(this) { shouldNavigate ->
            if (shouldNavigate){
                startActivity(Intent(this, LevelsActivity::class.java))
                viewModel.onNavigationDone()
            }
        }
    }

}