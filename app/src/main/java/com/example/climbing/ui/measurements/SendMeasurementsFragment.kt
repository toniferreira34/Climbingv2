package com.example.climbing.ui.measurements

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.climbing.R
import com.example.climbing.databinding.FragmentNewParticipanteBinding
import com.example.climbing.databinding.FragmentSendMeasurementsBinding
import com.example.climbing.databinding.RowParticipanteBinding
import com.example.climbing.models.Measurement
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.lang.Math.random
import kotlin.random.Random


class SendMeasurementsFragment : Fragment() {
     var randomBlood: Int = 0
    var randomHeartbeat: Int = 0

    private var _binding: FragmentSendMeasurementsBinding? = null
    private val binding get() = _binding!!
    var idPercurso: String? = null
    var idParticipante: String?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            arguments?.let {
                idPercurso = it.getString("id_percurso")
                idParticipante = it.getString("Id_participante")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSendMeasurementsBinding.inflate(inflater, container, false)

        binding.buttonRandomValue.setOnClickListener {

            randomBlood = (87..100).random()
            randomHeartbeat = (50..110).random()

            if (randomBlood< 94 || randomHeartbeat>105 || randomHeartbeat<58){
                showAlertDialogNegativos()
            }
            else{
                showAlertDialogPositivos()
            }

           binding.textViewBlood.setText(randomBlood.toString())
            binding.textViewHeadbeat.setText(randomHeartbeat.toString())
        }


        binding.RegisterMeasurementButton.setOnClickListener {
            if (binding.checkBoxTired.isChecked){

            }else{

            }


        }


        return binding.root
    }

    private fun showAlertDialogNegativos(){
        AlertDialog.Builder(requireContext())
            .setMessage("Os Valores estão anormais e é aconselhavel que nao avance no percurso!")
            .setTitle("Resultados Negativos")
            .setPositiveButton("OK", DialogInterface.OnClickListener{ dialogInterface, i ->

            })
            .create()
            .show()
    }
    private fun showAlertDialogPositivos(){
        AlertDialog.Builder(requireContext())
            .setMessage("Os Resultados dos batimentos cardíacos e do oxigénio presente no sangue estão normais!")
            .setTitle("Resultados Positivos")
            .setPositiveButton("OK", DialogInterface.OnClickListener{ dialogInterface, i ->

            })
            .create()
            .show()
    }


    companion object {

    }
}