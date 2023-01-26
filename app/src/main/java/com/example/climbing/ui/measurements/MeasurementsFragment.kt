package com.example.climbing.ui.measurements

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.climbing.R
import com.example.climbing.databinding.FragmentDashboardBinding
import com.example.climbing.databinding.FragmentSelectPercursoBinding
import com.example.climbing.databinding.RowPercursoBinding
import com.example.climbing.models.Percurso
import com.example.climbing.ui.home.HomeViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class MeasurementsFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var Spercursos = arrayListOf<Percurso>()

    var adapter = PercursosAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val MeasurementsViewModel =
            ViewModelProvider(this).get(MeasurementsViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)
        val db = Firebase.firestore
        db.collection("percursos")
            .addSnapshotListener{value, error->
                Spercursos.clear()
                for (doc in value?.documents!!){
                    Spercursos.add(Percurso.fromDoc(doc))

                }
                adapter.notifyDataSetChanged()
            }

        binding.RecyclerViewPercursoSelec.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.RecyclerViewPercursoSelec.adapter = adapter
        binding.RecyclerViewPercursoSelec.itemAnimator = DefaultItemAnimator()


    }
    inner class PercursosAdapter : RecyclerView.Adapter<PercursosAdapter.ViewHolder>() {

        inner class ViewHolder(binding: RowPercursoBinding) :
            RecyclerView.ViewHolder(binding.root) {
            val textViewId: TextView = binding.textViewId
            val textViewName: TextView = binding.textViewName
            val textViewDuracao: TextView = binding.textViewduracao
            val imagem: ImageView = binding.imageViewPercurso
            val line: ConstraintLayout = binding.linha

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                RowPercursoBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            var percurso = Spercursos[position]
            holder.apply {
                val storage = Firebase.storage

                textViewId.text = percurso.percursoId
                textViewName.text = percurso.name
                textViewDuracao.text = percurso.duracao

                percurso?.photoPercurso?.let {
                    val storage = Firebase.storage
                    val storageRef = storage.reference
                    var islandRef = storageRef.child("images/${it}")

                    val ONE_MEGABYTE: Long = 10024 * 1024
                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                        val inputStream = it.inputStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        imagem.setImageBitmap(bitmap)

                    }
                }

                line.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putString("id_percurso", percurso.percursoId)
                    findNavController().navigate(
                        R.id.action_navigation_measurements_to_participantesPorPercursoFragment,
                        bundle
                    )
                }

            }
        }

        override fun getItemCount(): Int {
            return Spercursos.size
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}