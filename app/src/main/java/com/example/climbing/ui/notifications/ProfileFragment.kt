package com.example.climbing.ui.notifications

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.climbing.LoginActivity
import com.example.climbing.models.User
import com.example.climbing.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // This property is only valid between onCreateView and
    // onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Firebase.auth.signOut()
            val intent = Intent(getActivity(), LoginActivity::class.java)
            getActivity()
            startActivity(intent)
            getActivity()?.finish()

        }

        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val db = Firebase.firestore
        db.collection("users").document(uid)
            .addSnapshotListener { value, error ->

                val user = value?.let {
                    User.fromDoc(it)
                }
                binding.textNameUser.setText(user?.name)
                binding.textEmailUser.setText(user?.email)

                user?.photoFilename?.let {
                    val storage = Firebase.storage
                    val storageRef = storage.reference
                    var islandRef = storageRef.child("images/${it}")

                    val ONE_MEGABYTE: Long = 10024*1024
                    islandRef.getBytes(ONE_MEGABYTE).addOnSuccessListener {
                        val inputStream = it.inputStream()
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.imageProfile.setImageBitmap(bitmap)

                    }
                }

            }


        val focusChange = object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if (!hasFocus) {
                    when (view) {
                        binding.textEmailUser -> {
                            User.postField(binding.textEmailUser.text.toString(), "name")
                        }
                        binding.textNameUser -> {
                            User.postField(binding.textNameUser.text.toString(), "email")
                        }
                    }
                }
            }
        }
    }
}





