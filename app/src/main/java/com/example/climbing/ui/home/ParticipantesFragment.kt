package com.example.climbing.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.climbing.R
import com.example.climbing.databinding.FragmentParticipantesBinding
import com.example.climbing.databinding.RowParticipanteBinding
import com.example.climbing.models.Participantes
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class ParticipantesFragment : Fragment() {

    private var _binding: FragmentParticipantesBinding? = null
    private val binding get() = _binding!!

    var participantes = arrayListOf<Participantes>()
    var adapter = ParticipantesAdapter()

    var idPercurso : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            idPercurso = it.getString("id_percurso")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentParticipantesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = Firebase.firestore
        db.collection("participantes")
            .addSnapshotListener{value, error->
                participantes.clear()
                for (doc in value?.documents!!){
                    participantes.add(Participantes.fromDoc(doc))
                }
                adapter.notifyDataSetChanged()
            }

        binding.recyclerViewPraticipantes.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.recyclerViewPraticipantes.adapter = adapter
        binding.recyclerViewPraticipantes.itemAnimator = DefaultItemAnimator()



        binding.floatingParticipante.setOnClickListener{

        }
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