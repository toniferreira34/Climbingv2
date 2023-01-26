package com.example.climbing.ui.home

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.climbing.R
import com.example.climbing.databinding.FragmentPercursosBinding
import com.example.climbing.databinding.RowPercursoBinding
import com.example.climbing.models.Percurso
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class Percursos : Fragment() {

    private var _binding: FragmentPercursosBinding? = null
    private val binding get() = _binding!!

    var percursos = arrayListOf<Percurso>()

    var adapter = PercursosAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPercursosBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        val db = Firebase.firestore
        db.collection("percursos")
            .addSnapshotListener{value, error->
                percursos.clear()
                for (doc in value?.documents!!){
                    percursos.add(Percurso.fromDoc(doc))

                }
                adapter.notifyDataSetChanged()
            }

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
            val imagem : ImageView = binding.imageViewPercurso
            val line : ConstraintLayout = binding.linha

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                RowPercursoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var percurso = percursos[position]
            holder.apply {
                val storage = Firebase.storage

                textViewId.text = percurso.percursoId
                textViewName.text = percurso.name
                textViewDuracao.text = percurso.duracao

                percurso?.photoPercurso?.let {
                    val storage = Firebase.storage
                    val storageRef = storage.reference
                    var islandRef = storageRef.child("images/${it}")

                    val ONE_MEGABYTE: Long = 10024*1024
                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                        val inputStream = it.inputStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        imagem.setImageBitmap(bitmap)

                    }
                }

                line.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("id_percurso", percurso.percursoId)
                    findNavController().navigate(R.id.action_percursos_to_percursoDetailFrafment,bundle)
                }

            }
        }

        override fun getItemCount(): Int {
            return percursos.size
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

}

