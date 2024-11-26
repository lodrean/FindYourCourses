package com.lodrean.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lodrean.auth.databinding.FragmentRegistrationBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RegistrationFragment : Fragment() {
    lateinit var auth: FirebaseAuth
    private var _binding: FragmentRegistrationBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        val root: View = binding.root
        auth = Firebase.auth
        return root
    }

    private fun registerUser() {
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth
                        .createUserWithEmailAndPassword(email, password)
                        .await()
                    navigateToMainScreen()
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireActivity(), e.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding.enterText.setOnClickListener {
            findNavController().navigate(R.id.action_registrationFragment_to_signInFragment)
        }
        binding.buttonRegistration.setOnClickListener {
            registerUser()
        }
    }

    private fun navigateToMainScreen() {
        val request =
            NavDeepLinkRequest.Builder
                .fromUri(SEARCH_DEEP_LINK.toUri())
                .build()
        findNavController().navigate(request)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val SEARCH_DEEP_LINK = "stepic-app://com.lodrean.search/search"
    }
}
