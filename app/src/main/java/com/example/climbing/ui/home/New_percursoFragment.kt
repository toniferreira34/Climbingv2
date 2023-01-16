package com.example.climbing.ui.home

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.climbing.MainActivity
import com.example.climbing.R
import com.example.climbing.RegisterActivity
import com.example.climbing.databinding.FragmentHomeBinding
import com.example.climbing.databinding.FragmentNewPercursoBinding
import com.example.climbing.databinding.FragmentPercursosBinding
import com.example.climbing.models.User
import com.example.climbing.models.percurso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import java.io.File


class New_percursoFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var _binding: FragmentNewPercursoBinding? = null

    lateinit var imageView: ImageView
    private var imageUri: Uri? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentNewPercursoBinding.inflate(inflater, container, false)




        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        auth = FirebaseAuth.getInstance()

        binding.imagePercurso.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 1001)
        }

        findNavController().popBackStack(R.id.action_newPercursoFragment_to_navigation_home, false)
        binding.buttonSalvarPercurso.setOnClickListener {

            storeBitmap(imageUri!!){
                percurso(
                    binding.editTextPercursoId.text.toString(),
                    binding.editTextPercursoName.text.toString(),
                    binding.editTextPercursoDuracao.text.toString(),
                    it
                    ).sendPercurso { error ->
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
        if (resultCode == RESULT_OK && requestCode == 1001) {
            imageUri = data?.data
            binding.imagePercurso.setImageURI(imageUri)

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

        const val TAG = "New_percursoFragment"
    }

}





