package com.luizeduardobrandao.mvvmquizjogador.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

    // Guarda a opção clicada para usar no feedback de cor
    private var lastClickedOption: String? = null

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

        // Recupera level atual
        val level = intent.getIntExtra("EXTRA_LEVEL", 1)

        // Carrega as perguntas do level atual e não muda índice após rotações
        if (savedInstanceState == null) {
            viewModel.loadQuestions(level)
        }

        // Configura RecyclerView e observers
        setupRecycler()
        observeViewModel(level)
        setListeners()

        // carrega o banner no container da view binding
        BannerAds.loadBanner(this, binding.frameBanner)
    }

    // Configura os liteners
    private fun setListeners() {
        binding.btnPause.setOnClickListener {
            // pausa o cronometro
            viewModel.pauseTimer()

            // exibe o AlertDialog
            AlertDialog.Builder(this)
                .setMessage(R.string.dialog_exit_message)
                .setPositiveButton(R.string.dialog_positive) { _, _ ->
                    // retorna para LevelsActivity
                    finish()
                }
                .setNegativeButton(R.string.dialog_negative) { dialog, _ ->
                    dialog.dismiss()
                    // retoma o timer de onde parou
                    viewModel.resumeTimer()
                }
                .setCancelable(false)
                .show()
        }
    }

    // Configura RecyclerView com LinearLayout e Adapter
    private fun setupRecycler(){
        // 1) configurar RecyclerView e Adapter
        adapter = QuizAdapter(onOptionClickListener = { button, option ->
            // dispara a checagem
            lastClickedOption = option
            viewModel.submitAnswer(option)
        })
        binding.recyclerOptions.layoutManager = LinearLayoutManager(this)
        binding.recyclerOptions.adapter = adapter
    }

    // Observa e exibe a pergunta e índice
    private fun observeViewModel(level: Int){
        // 2) Observe de pergunta
        viewModel.currentQuestion.observe(this) { q ->
            val clubes = q.clubs.joinToString(", ")
            binding.textviewQuestion.text =
                getString(R.string.label_quiz_card, clubes)
            adapter.setOptions(q.options)
        }

        // 3) Observa índice
        viewModel.questionIndex.observe(this) { index ->
            binding.textviewIndex.text =
                getString(R.string.label_timer, index)
        }

        // 4) Observer do resultado da resposta (somente TRUE ou FALSE)
        viewModel.answerResult.observe(this) { result ->
            if (result != null) {
                // marca a opção clicada como CERTA ou ERRADA (pinta de verde ou vermelho)
                lastClickedOption?.let { opt ->
                    adapter.markOption(opt, result)
                }
                // espera 2s e então avançar/reiniciar via ViewModel
                Handler(Looper.getMainLooper()).postDelayed({
                    adapter.clearMarks()
                    viewModel.moveToNext()
                }, 2000)
            }
        }

        // 5) Observer de navegação ao resultado (resultado == NULL)
        viewModel.navigateToResult.observe(this) { go ->
            if (go) {
                // pinta de verde o ultimo botão (acerto da última pergunta)
                lastClickedOption?.let { adapter.markOption(it, true) }

                // espera 2s e então navega
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this, ResultActivity::class.java)
                        .putExtra("EXTRA_LEVEL", level))
                    finish()
                    viewModel.doneNavigation()
                }, 2000)
            }
        }

        // 6) Observe o cronômetro e atualiza UI
        viewModel.remainingSeconds.observe(this) { secs ->
            binding.textviewTimer.text =
                getString(R.string.label_timer_seconds, secs)
            val colorRes = if (secs <= 10)
                R.color.timer_finish
            else
                R.color.black

            binding.textviewTimer.setTextColor(
                ContextCompat.getColor(this, colorRes)
            )
        }
    }
}