package com.lodrean.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.lodrean.auth.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.registrationText.setOnClickListener {
            findNavController().navigateUp()
        }
        val request =
            NavDeepLinkRequest.Builder
                .fromUri(SEARCH_DEEP_LINK.toUri())
                .build()
        binding.signInButton.setOnClickListener {
            Log.d("TAG", "onViewCreated:")
            findNavController().navigate(request)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SEARCH_DEEP_LINK = "stepic-app://com.lodrean.search/search"
    }
}
