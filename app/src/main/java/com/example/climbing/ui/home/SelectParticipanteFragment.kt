package com.example.climbing.ui.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.climbing.R
import com.example.climbing.databinding.FragmentNewPercursoBinding
import com.example.climbing.databinding.FragmentSelectParticipanteBinding
import com.example.climbing.databinding.RowParticipanteBinding
import com.example.climbing.models.Participantes
import com.example.climbing.models.Percurso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class SelectParticipanteFragment : Fragment() {

    var participantes = arrayListOf<Participantes>()
    var adapter = ParticipantesAdapter()
    private var _binding: FragmentSelectParticipanteBinding? = null
    private val binding get() = _binding!!


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
        _binding = FragmentSelectParticipanteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

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

        binding.floatingNewParticipante.setOnClickListener {

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
            var participante = participantes[position]
            holder.apply {
                val storage = Firebase.storage

                textViewId.text = participante.participanteId
                textViewName.text = participante.name
                textViewNacionalidade.text = participante.nacionalidade

                itemView.setOnClickListener {
                    val db = Firebase.firestore
                    db.collection("percursos")
                        .document(idPercurso!!)
                        .collection("participantes")
                        .document(participante.participanteId!!).set(
                            participante.toHashMap()
                        )
                    findNavController().popBackStack()
                }
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

        const val TAG = "SelectParticipanteFragment"
    }

}





