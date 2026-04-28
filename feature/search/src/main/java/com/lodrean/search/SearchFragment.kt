package com.lodrean.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lodrean.search.databinding.FragmentSearchBinding
import com.lodrean.uikit.CourseAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels()
    private var adapter: CourseAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchInput()
        collectUiState()
    }

    private fun setupRecyclerView() {
        adapter = CourseAdapter(
            onFavoriteClick = { course -> viewModel.toggleFavorite(course) },
            onItemClick = { course ->
                val bundle = android.os.Bundle().apply { putLong("courseId", course.id) }
                findNavController().navigate(R.id.action_searchFragment_to_detailsFragment, bundle)
            },
        )
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    viewModel.loadNextPage()
                }
            }
        })
    }

    private fun setupSearchInput() {
        val editText = binding.root.findViewById<EditText>(com.lodrean.search.R.id.search_edit_frame)
        editText.doAfterTextChanged { text ->
            viewModel.onQueryChanged(text?.toString().orEmpty())
        }
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter?.submitList(state.courses)
                    binding.progressBar.isVisible = state.isLoading
                    binding.errorText.isVisible = state.error != null
                    binding.errorText.text = state.error

                    val showEmpty = !state.isLoading && state.error == null && state.courses.isEmpty()
                    binding.emptyText.isVisible = showEmpty
                    binding.emptyText.text = if (viewModel.query.value.isBlank()) {
                        getString(R.string.empty_search)
                    } else {
                        getString(R.string.no_results)
                    }
                    binding.recyclerView.isVisible = state.courses.isNotEmpty()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
