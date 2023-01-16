package com.example.climbing.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.climbing.R
import com.example.climbing.databinding.FragmentParticipantesBinding
import com.example.climbing.databinding.FragmentPercursosBinding
import com.example.climbing.databinding.RowParticipanteBinding
import com.example.climbing.databinding.RowPercursoBinding
import com.example.climbing.models.participantes
import com.example.climbing.models.percurso
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class ParticipantesFragment : Fragment() {

    private var _binding: FragmentParticipantesBinding? = null
    private val binding get() = _binding!!

    var participantes = arrayListOf<participantes>()

    var adapter = ParticipantesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentParticipantesBinding.inflate(inflater, container, false)
        return binding.root
    }

    inner class ParticipantesAdapter : RecyclerView.Adapter<ParticipantesAdapter.ViewHolder>(){

        inner class ViewHolder(binding: RowParticipanteBinding) : RecyclerView.ViewHolder(binding.root){
            val textViewId : TextView = binding.textViewId
            val textViewName : TextView = binding.textViewNameParticipante
            val textViewNacionalidade : TextView = binding.textViewNacionalidade


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                RowParticipanteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var participantes = participantes[position]
            holder.apply {
                val storage = Firebase.storage

                textViewId.text = participantes.participanteId
                textViewName.text = participantes.name
                textViewNacionalidade.text = participantes.nacionalidade
            }
        }

        override fun getItemCount(): Int {
            return participantes.size
        }

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {

    }
}