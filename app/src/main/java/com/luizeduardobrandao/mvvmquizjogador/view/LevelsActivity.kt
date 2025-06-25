package com.luizeduardobrandao.mvvmquizjogador.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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

        // ◀️ mostra o toast se tiver EXTRA
        handleUnlockIntent(intent)

        // Configura RecyclerView e observers
        setupRecycler()
        observeViewModel()

        // carrega o banner no container da view binding
        BannerAds.loadBanner(this, binding.frameBanner)
    }

    // ◀️ Capture novas intents quando vier com FLAG_ACTIVITY_CLEAR_TOP
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleUnlockIntent(intent)
    }

    // ◀️ Se existir o extra, exibe o toast e limpa para não repetir
    private fun handleUnlockIntent(intent: Intent) {
        if (intent.hasExtra("EXTRA_LEVEL_UNLOCKED")) {
            val lvl = intent.getIntExtra("EXTRA_LEVEL_UNLOCKED", -1)
            if (lvl > 1) {
                Toast.makeText(this, "Level $lvl desbloqueado!", Toast.LENGTH_SHORT)
                    .show()
            }
            intent.extras?.remove("EXTRA_LEVEL_UNLOCKED")
        }
    }

    // 1) Define o GridLayoutManager
    private fun setupRecycler() {
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