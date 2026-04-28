package com.lodrean.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.lodrean.search.databinding.FragmentDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    @Suppress("ktlint:standard:backing-property-naming")
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.beginCourseButton.setOnClickListener {
            viewModel.toggleFavorite()
        }
        collectUiState()
    }

    private fun collectUiState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    binding.progressBar.isVisible = state.isLoading
                    binding.errorText.isVisible = state.error != null
                    binding.errorText.text = state.error

                    val course = state.course
                    if (course != null) {
                        binding.titleName.text = course.title
                        binding.contentDescription.text = course.summary
                        Glide.with(binding.titleImage)
                            .load(course.coverUrl)
                            .placeholder(com.lodrean.uikit.R.drawable.cover)
                            .into(binding.titleImage)
                        binding.beginCourseButton.text = if (course.isFavorite) {
                            getString(com.lodrean.search.R.string.remove_from_favorites)
                        } else {
                            getString(com.lodrean.search.R.string.add_to_favorites)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
