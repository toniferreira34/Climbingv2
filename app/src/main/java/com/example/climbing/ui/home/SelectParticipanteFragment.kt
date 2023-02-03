package com.example.climbing.ui.home

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
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
    private var imageUri: Uri? = null


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
              findNavController().navigate(R.id.action_selectParticipanteFragment_to_new_participanteFragment)
        }


    }

    inner class ParticipantesAdapter : RecyclerView.Adapter<ParticipantesAdapter.ViewHolder>(){

        inner class ViewHolder(binding: RowParticipanteBinding) : RecyclerView.ViewHolder(binding.root){

            val textViewName : TextView = binding.textViewNameParticipante
            val textViewNacionalidade : TextView = binding.textViewNacionalidade
            val imagem : ImageView = binding.imageViewParticipanteRow



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


                textViewName.text = participante.name
                textViewNacionalidade.text = participante.nacionalidade

                participante?.photoParticipante?.let {
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
    fun storeBitmap(uri: Uri, callback:(filename:String)->Unit) {
        val storage = Firebase.storage
        val storageRef = storage.reference
        val filename = "${uri.lastPathSegment}"
        val riversRef = storageRef.child("images/${uri.lastPathSegment}")
        val uploadTask = riversRef.putFile(uri)
        uploadTask.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Não foi possivel guardar a imagem",
                Toast.LENGTH_LONG
            ).show()
        }.addOnSuccessListener { taskSnapshot ->
            callback.invoke(filename)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1001) {
            imageUri = data?.data

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





