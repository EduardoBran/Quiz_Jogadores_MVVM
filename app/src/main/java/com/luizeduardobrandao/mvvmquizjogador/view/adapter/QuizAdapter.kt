package com.luizeduardobrandao.mvvmquizjogador.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luizeduardobrandao.mvvmquizjogador.databinding.ItemQuizBinding

class QuizAdapter(
    private var options: List<String> = emptyList(),
    private val onOptionClicked: (String) -> Unit
): RecyclerView.Adapter<QuizAdapter.OptionViewHolder>() {


    inner class OptionViewHolder(val binding: ItemQuizBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        val binding = ItemQuizBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OptionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return options.size
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        val option = options[position]
        holder.binding.btnOption.text = option
        holder.binding.btnOption.setOnClickListener {
            onOptionClicked(option)
        }
    }

    // atualiza as opções exibidas
    fun setOptions(newOptions: List<String>) {
        options = newOptions
        notifyDataSetChanged()
    }
}