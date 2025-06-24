package com.luizeduardobrandao.mvvmquizjogador.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.luizeduardobrandao.mvvmquizjogador.R
import com.luizeduardobrandao.mvvmquizjogador.databinding.ItemQuizBinding

/** Estado de cada opção para renderizar cor */
enum class OptionState { NORMAL, CORRECT, WRONG }

/**
 * UI model para cada opção: texto + estado
 * Adapter decide a cor do botão com base em state
 */
data class OptionUiModel(val text: String, var state: OptionState = OptionState.NORMAL)

class QuizAdapter(
    private var items: List<OptionUiModel> = emptyList(),
    private val onOptionClickListener: (MaterialButton, String) -> Unit
) : RecyclerView.Adapter<QuizAdapter.OptionViewHolder>() {

    inner class OptionViewHolder(val binding: ItemQuizBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val binding = ItemQuizBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OptionViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val item = items[position]
        holder.binding.btnOption.text = item.text

        // aplica cor conforme estado
        val colorRes = when (item.state) {
            OptionState.NORMAL  -> R.color.button_option
            OptionState.CORRECT -> R.color.quiz_option_correct
            OptionState.WRONG   -> R.color.quiz_option_wrong
        }
        holder.binding.btnOption.backgroundTintList =
            holder.binding.btnOption.context.getColorStateList(colorRes)

        holder.binding.btnOption.setOnClickListener {
            onOptionClickListener(holder.binding.btnOption, item.text)
        }
    }

    /** Atualiza t0do o conjunto de opções (ao mudar de pergunta) */
    fun setOptions(texts: List<String>) {
        items = texts.map { OptionUiModel(it, OptionState.NORMAL) }
        notifyDataSetChanged()
    }

    /** Marca apenas aquela opção como CORRECT ou WRONG */
    fun markOption(text: String, correct: Boolean) {
        items.forEach {
            if (it.text == text) {
                it.state = if (correct) OptionState.CORRECT else OptionState.WRONG
            }
        }
        notifyDataSetChanged()
    }

    /** Volta todas para NORMAL (após delay) */
    fun clearMarks() {
        items.forEach { it.state = OptionState.NORMAL }
        notifyDataSetChanged()
    }
}