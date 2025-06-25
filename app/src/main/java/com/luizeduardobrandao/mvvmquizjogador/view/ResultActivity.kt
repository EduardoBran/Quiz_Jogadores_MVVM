package com.luizeduardobrandao.mvvmquizjogador.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.luizeduardobrandao.mvvmquizjogador.R
import com.luizeduardobrandao.mvvmquizjogador.databinding.ActivityResultBinding
import com.luizeduardobrandao.mvvmquizjogador.repository.BannerAds
import com.luizeduardobrandao.mvvmquizjogador.viewmodel.LevelsViewModel
import com.luizeduardobrandao.mvvmquizjogador.viewmodel.ResultViewModel

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    // ViewModel que controla o unlocked
    private val levelsViewModel: LevelsViewModel by viewModels()

    // ViewModel de Result
    private val viewModel: ResultViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1) Recebe o nível da Intent e informa ao ViewModel
        val level = intent.getIntExtra("EXTRA_LEVEL", 1)

        // Próximo level para exibir no Toast em LevelsActivity quando level desbloqueado
        val nextLevel = (level + 1)

        // 2) Toolbar com botão de voltar para LevelsActivity
        setSupportActionBar(binding.toolbarLevels)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarLevels.setNavigationOnClickListener {
            val intent = Intent(this, LevelsActivity::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra("EXTRA_LEVEL_UNLOCKED", nextLevel)
                }
            startActivity(intent)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        // ⬇️ Destrava o próximo nível e persiste (só destrava na primeira criação, não em rotações)
        if (savedInstanceState == null) {
            levelsViewModel.unlockNextLevel()
        }

        setListeners(level, nextLevel)
        setObservers(level)

        // Carrega banner de anúncios
        BannerAds.loadBanner(this, binding.frameBanner)
    }

    // 3) Observa o nível e atualiza o texto
    private fun setObservers(level: Int){
        viewModel.setLevel(level)
        viewModel.level.observe(this) { lvl ->
            binding.textviewFinishedLevel.text =
                getString(R.string.label_text_result, lvl)
        }
    }

    // Botão “Continuar” (voltar para LevelsActivity) e "Compartilhar"
    private fun setListeners(level: Int, nextLevel: Int){
        binding.btnBackToLevels.setOnClickListener {
            val intent = Intent(this, LevelsActivity::class.java)
                .apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    putExtra("EXTRA_LEVEL_UNLOCKED", nextLevel)
                }
            startActivity(intent)
            finish()
        }

        // Compartilhar
        binding.btnShare.setOnClickListener {
            // monta a mensagem
            val shareText = """
                Zerei o Level $level do Quiz Jogadores de Futebol
        Vem jogar também!
        https://play.google.com/store/apps/details?id=com.luizeduardobrandao.quizjogadoresdefutebol
        """.trimIndent()

            // Cria a Intent de compartilhamento
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, shareText)
            }

            // Exibe o chooser
            startActivity(Intent.createChooser(shareIntent, "Compartilhar via"))
        }
    }
}