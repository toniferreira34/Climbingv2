package com.example.climbing.ui.home

import android.os.Bundle
import android.text.TextUtils.replace
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.example.climbing.R
import com.example.climbing.databinding.FragmentNewPercursoBinding
import com.example.climbing.databinding.FragmentPercursosBinding


class Percursos : Fragment() {
    private var _binding: FragmentPercursosBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_percursos, container, false)
        _binding = FragmentPercursosBinding.inflate(inflater, container, false)
        val root: View = binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        }
    }

