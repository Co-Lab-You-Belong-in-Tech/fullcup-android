package com.cerdenia.android.fullcup.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cerdenia.android.fullcup.databinding.FragmentSignInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private var googleSignInClient: GoogleSignInClient? = null
    private var callbacks: Callbacks? = null

    interface Callbacks {
        fun onSignIn(signInIntent: Intent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = context?.let { GoogleSignIn.getClient(it, gso) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        // Set the dimensions of the sign-in button.
        binding.signInButton.setSize(SignInButton.SIZE_STANDARD)
        binding.signInButton.setColorScheme(SignInButton.COLOR_LIGHT)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signInButton.setOnClickListener { signIn() }
        binding.signOutButton.setOnClickListener { signOut() }
        binding.disconnectButton.setOnClickListener { revokeAccess() }
    }

    override fun onStart() {
        super.onStart()
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(context)
        updateUI(account)
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if (account != null) {
            binding.statusTextView.text = "Signed in as ${account.displayName}"
            binding.signInButton.visibility = View.GONE
            binding.signOutAndDisconnect.visibility = View.VISIBLE
        } else {
            binding.statusTextView.text = "Signed out"
            binding.signInButton.visibility = View.VISIBLE
            binding.signOutAndDisconnect.visibility = View.GONE
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }

    private fun signIn() {
        Log.i(TAG, "Signing in...")
        val signInIntent = googleSignInClient?.signInIntent
        startActivityForResult(signInIntent, 9001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == 9001) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun signOut() {
        googleSignInClient?.signOut()
            ?.addOnCompleteListener(requireActivity()) {
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
    }

    private fun revokeAccess() {
        googleSignInClient?.revokeAccess()
            ?.addOnCompleteListener(requireActivity()) {
                // [START_EXCLUDE]
                updateUI(null)
                // [END_EXCLUDE]
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        private const val TAG = "SignInFragment"

        fun newInstance(): SignInFragment = SignInFragment()
    }
}