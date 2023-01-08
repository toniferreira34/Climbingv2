package com.example.climbing.ui.home

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils.replace
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.climbing.R
import com.example.climbing.databinding.FragmentNewPercursoBinding
import com.example.climbing.databinding.FragmentPercursosBinding
import com.example.climbing.databinding.RowPercursoBinding
import com.example.climbing.models.percurso
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class Percursos : Fragment() {

    private var _binding: FragmentPercursosBinding? = null
    private val binding get() = _binding!!

    var percursos = arrayListOf<percurso>()

    var adapter = PercursosAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_percursos, container, false)
        _binding = FragmentPercursosBinding.inflate(inflater, container, false)
        val root: View = binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewPercursos.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewPercursos.adapter = adapter
        binding.recyclerViewPercursos.itemAnimator = DefaultItemAnimator()

        binding.floatingActionButtonPercurso.setOnClickListener{
            findNavController().navigate(R.id.action_percursos_to_newPercursoFragment)
        }

    }

    inner class PercursosAdapter : RecyclerView.Adapter<PercursosAdapter.ViewHolder>(){

        inner class ViewHolder(binding: RowPercursoBinding) : RecyclerView.ViewHolder(binding.root){
            val textViewId : TextView = binding.textViewId
            val textViewName : TextView = binding.textViewName
            val textViewDuracao : TextView = binding.textViewduracao

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                RowPercursoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var percursos = percursos[position]
            holder.apply {
                val storage = Firebase.storage

                textViewId.text = percursos.percursoId
                textViewName.text = percursos.name
                textViewDuracao.text = percursos.duracao
            }
        }

        override fun getItemCount(): Int {
            return percursos.size
        }

    }


}

