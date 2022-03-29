package com.kenshi.coroutinehandson.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kenshi.coroutinehandson.databinding.FragmentMainBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ImageSearchFragment : Fragment() {

    private lateinit var imageSearchViewModel: ImageSearchViewModel

    private val adapter: ImageSearchAdapter = ImageSearchAdapter {
        imageSearchViewModel.toggle(it)
    }

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

        val binding = FragmentMainBinding.inflate(inflater, container, false)
        val root = binding.root

        //Fragment의 lifecycle scope 와 view의 lifecyclescope 가 다름
//        Fragment 이므로 그냥 lifecycleScope 면 안됨 -> 얘는 fragment 의 lifecycle 에 의존

//        lifecycleScope.launch {
//            //collectLatest 를 이용해서 어댑터를 갱신
//        }

//      view 의 lifecycle 에 의존, fragment 가 사라져도 (더이상 보이지 않게 될때도) lifecycleScope 가 남아있을 수 있기 때문
        viewLifecycleOwner.lifecycleScope.launch {
            //collectLatest 를 이용해서 어댑터를 갱신
            imageSearchViewModel.pagingDataFlow
                .collectLatest { items ->
                    adapter.submitData(items)
                }
        }

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 4)
        binding.search.setOnClickListener {
            val query = binding.editText.text.trim().toString()
            imageSearchViewModel.handleQuery(query)
        }

        return root
    }
}