package com.dmm.cheappcgames.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dmm.cheappcgames.MainActivity
import com.dmm.cheappcgames.R
import com.dmm.cheappcgames.databinding.FragmentSearchBinding
import com.dmm.cheappcgames.ui.OffersViewModel

class FragmentSearch : Fragment() {

    private lateinit var _binding : FragmentSearchBinding
    private val binding get() = _binding

    lateinit var viewModel: OffersViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchBinding.bind(view)

        viewModel = (activity as MainActivity).viewModel
    }
}