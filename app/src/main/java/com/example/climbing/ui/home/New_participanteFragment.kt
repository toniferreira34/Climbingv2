package com.example.climbing.ui.home

import android.app.Activity
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
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.climbing.R
import com.example.climbing.databinding.FragmentNewParticipanteBinding
import com.example.climbing.models.Participantes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage


class New_participanteFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentNewParticipanteBinding? = null

    lateinit var imageView: ImageView
    private var imageUri: Uri? = null

    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findNavController().popBackStack(R.id.action_new_participanteFragment_to_navigation_home, false)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNewParticipanteBinding.inflate(inflater, container, false)

        binding.imageViewParticipante.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 1001)
        }


        binding.buttonSalvarParticipante.setOnClickListener {

            storeBitmap(imageUri!!){
                Participantes(
                    binding.editTextIdParticipante.text.toString(),
                    binding.editTextParticipanteName.text.toString(),
                    binding.editTextParticipanteIdade.text.toString(),
                    binding.editTextNacionalidade.text.toString(),
                    it
                ).sendParticipantes { error ->
                    error?.let {
                        Toast.makeText(requireContext(), "Ocurreu algum erro!", Toast.LENGTH_LONG)
                            .show()
                    } ?: kotlin.run {
                        Toast.makeText(requireContext(), "Guardado com sucesso!", Toast.LENGTH_LONG)
                            .show()

                    }
                }
            }

        }


        return binding.root



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
            binding.imageViewParticipante.setImageURI(imageUri)

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
        const val TAG = "New_participanteFragment"
    }
}