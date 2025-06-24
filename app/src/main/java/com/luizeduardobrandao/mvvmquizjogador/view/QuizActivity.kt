package com.luizeduardobrandao.mvvmquizjogador.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.luizeduardobrandao.mvvmquizjogador.R
import com.luizeduardobrandao.mvvmquizjogador.databinding.ActivityQuizBinding
import com.luizeduardobrandao.mvvmquizjogador.repository.BannerAds
import com.luizeduardobrandao.mvvmquizjogador.view.adapter.QuizAdapter
import com.luizeduardobrandao.mvvmquizjogador.viewmodel.QuizViewModel

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private val viewModel: QuizViewModel by viewModels()
    private lateinit var adapter: QuizAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

    // Configura RecyclerView com LinearLayout e Adapter
    private fun setupRecycler(){
        binding.recyclerOptions.layoutManager = LinearLayoutManager(this)
        adapter = QuizAdapter { option ->
            // TODO: tratar clique na opção
        }
        binding.recyclerOptions.adapter = adapter
    }

    // Observa e exibe a pergunta e opções
    private fun observeViewModel(){
        viewModel.currentQuestion.observe(this) { question ->
            // 1) Une os nomes dos clubes em uma única string separados por vírgula
            val clubesText = question.clubs.joinToString(separator = ", ")
            // 2) Atribui ao TextView no formato desejado
            binding.textviewQuestion.text = "Joguei por: $clubesText"
            // 3) Continua populando as opções normalmente
            adapter.setOptions(question.options)
        }
        viewModel.loadQuestions(level = 1)
    }
}