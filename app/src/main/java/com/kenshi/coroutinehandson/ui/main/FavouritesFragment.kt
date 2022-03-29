package com.kenshi.coroutinehandson.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kenshi.coroutinehandson.databinding.FragmentFavouritesBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavouritesFragment : Fragment() {

    private lateinit var imageSearchViewModel: ImageSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // this 로 지정하면 각각의 프래그먼트가 각기 다른 뷰모델을 바라보게 됨
        //imageSearchViewModel = ViewModelProvider(this)[ImageSearchViewModel::class.java]
        imageSearchViewModel = ViewModelProvider(requireActivity())[ImageSearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val root = binding.root

        val adapter = FavouritesAdapter()
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)

        viewLifecycleOwner.lifecycleScope.launch {
            imageSearchViewModel.favoritesFlow.collectLatest {
                adapter.setItems(it)

            }
        }
        return root
    }
}