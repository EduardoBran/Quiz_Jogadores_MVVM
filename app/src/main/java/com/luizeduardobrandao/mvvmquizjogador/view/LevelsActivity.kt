package com.luizeduardobrandao.mvvmquizjogador.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.luizeduardobrandao.mvvmquizjogador.R
import com.luizeduardobrandao.mvvmquizjogador.databinding.ActivityLevelsBinding
import com.luizeduardobrandao.mvvmquizjogador.repository.BannerAds
import com.luizeduardobrandao.mvvmquizjogador.view.adapter.LevelsAdapter
import com.luizeduardobrandao.mvvmquizjogador.viewmodel.LevelsViewModel

class LevelsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLevelsBinding
    private val viewModel: LevelsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLevelsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) Configura a Toolbar com botão de voltar
        setSupportActionBar(binding.toolbarLevels)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarLevels.setNavigationOnClickListener { finish() }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configura RecyclerView e observers
        setupRecycler()
        observeViewModel()

        // carrega o banner no container da view binding
        BannerAds.loadBanner(this, binding.frameBanner)
    }

    private fun setupRecycler() {
        // 1) Define o GridLayoutManager
        binding.recyclerLevels.layoutManager = GridLayoutManager(this, 5)
    }

    private fun observeViewModel() {
        // 2) Observer do unlockedLevel: aqui criamos o Adapter
        viewModel.unlockedLevel.observe(this) { unlocked ->
            binding.recyclerLevels.adapter =
                LevelsAdapter(unlockedLevel = unlocked) { level ->
                    viewModel.onLevelClicked(level)
                }
        }

        // 3) Observer de navegação para QuizActivity
        viewModel.navigateToQuiz.observe(this) { level ->
            level?.let {
                startActivity(
                    Intent(this, QuizActivity::class.java)
                        .putExtra("EXTRA_LEVEL", it)
                )
                viewModel.onNavigationDone()
            }
        }
    }
}