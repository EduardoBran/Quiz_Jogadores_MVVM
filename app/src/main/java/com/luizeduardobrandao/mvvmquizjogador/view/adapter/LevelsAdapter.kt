package com.luizeduardobrandao.mvvmquizjogador.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luizeduardobrandao.mvvmquizjogador.databinding.ItemLevelBinding

/**
 * Adapter que exibe 10 botões de nível em um RecyclerView em grade.
 * @param unlockedLevel nível até onde está desbloqueado
 * @param onClick callback executado ao tocar em um nível válido
 */
class LevelsAdapter(
    private val unlockedLevel: Int,             // armazena até qual nível deve ficar habilitado
    private val onClick: (level: Int) -> Unit   // função que será chamada ao clicar num nível válido
): RecyclerView.Adapter<LevelsAdapter.LevelViewHolder>(){

    // Mét0do que retorna o número total de itens no RecyclerView
    override fun getItemCount(): Int {
        return 10                               // sempre teremos 10 níveis
    }

    // Cria e inicializa cada ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LevelViewHolder {
        // Infla o layout XML (item_level.xml) e retorna um binding
        val binding = ItemLevelBinding.inflate(
            LayoutInflater.from(parent.context), // obtém LayoutInflater a partir do contexto
            parent,                              // quem será o responsável pelo layout inflado
            false                   // não anexa imediatamente ao parent
        )
        return LevelViewHolder(binding)         // cria o ViewHolder com este binding
    }

    // Vincula dados a cada ViewHolder criado
    override fun onBindViewHolder(holder: LevelViewHolder, position: Int) {
        val levelNumber = position + 1               // converte índice (0..9) para nível legível (1..10)
        with(holder.binding.btnLevel) {              // acesso direto ao botão de nível no binding
            text = levelNumber.toString()            // define o texto do botão como número do nível
            isEnabled = levelNumber <= unlockedLevel // habilita o botão apenas se o nível estiver desbloqueado
            setOnClickListener {                     // configura o clique do botão
                onClick(levelNumber)                 // dispara callback com o nível clicado
            }
        }
    }

    // ViewHolder que guarda a referência ao binding de item_level.xml
    class LevelViewHolder(val binding: ItemLevelBinding): RecyclerView.ViewHolder(binding.root)
}

