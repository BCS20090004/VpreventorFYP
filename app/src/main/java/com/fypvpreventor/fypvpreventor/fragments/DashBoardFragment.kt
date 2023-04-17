package com.fypvpreventor.VpreventorFYP.fragments

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fypvpreventor.VpreventorFYP.*
import com.fypvpreventor.VpreventorFYP.R
import com.fypvpreventor.VpreventorFYP.database.Contacts
import com.fypvpreventor.VpreventorFYP.databinding.FragmentDashBoardBinding
import com.fypvpreventor.VpreventorFYP.viewmodels.ContactsViewModel
import com.fypvpreventor.VpreventorFYP.viewmodels.ContactsViewModelFactory
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class DashBoardFragment : Fragment() {
    //google sign in
    private lateinit var gso: GoogleSignInOptions
    private lateinit var gsc: GoogleSignInClient
    private lateinit var googlename: TextView
    private lateinit var googleemail: TextView

    private var _binding : FragmentDashBoardBinding?= null
    private val  binding get() = _binding!!
    private lateinit var contact: Contacts
    private lateinit var buttonTutorial: Button
    private lateinit var buttonHelpCenter: Button
    private lateinit var signoutButton: Button

    private lateinit var user: FirebaseUser
    private lateinit var reference: DatabaseReference
    private lateinit var userID: String
    private lateinit var fullNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var ageTextView: TextView

    //viewModel reference
    private val viewModel: ContactsViewModel by activityViewModels {
        ContactsViewModelFactory(
            (activity?.application as ContactsApplication).database.contactsDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashBoardBinding.inflate(inflater, container, false)

        // Find the buttons by their IDs
        buttonTutorial = binding.root.findViewById(R.id.Tutorialbtn)
        buttonHelpCenter = binding.root.findViewById(R.id.helpcenterbtn)
        signoutButton = binding.root.findViewById(R.id.singout)
        fullNameTextView = binding.root.findViewById(R.id.FullName)
        emailTextView = binding.root.findViewById(R.id.EmailAddress)
        ageTextView = binding.root.findViewById(R.id.Age)
        // Set click listeners for the buttons
        buttonTutorial.setOnClickListener {
            openTutorialActivity()
        }

        buttonHelpCenter.setOnClickListener {
            openHelpCenterActivity()
        }


        // Get the shared preferences
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Check if user is already logged in
        if (sharedPreferences?.contains("email") == true && sharedPreferences.contains("password")) {
            val email = sharedPreferences.getString("email", "")
            val password = sharedPreferences.getString("password", "")

            // Call login method with saved credentials
            userLogin(email, password)
        }

        // Set click listener for sign out button
        signoutButton.setOnClickListener {
            // Clear shared preferences
            val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            editor?.clear()
            editor?.apply()

            // Sign out the user
            FirebaseAuth.getInstance().signOut()

            // Redirect to login activity
            startActivity(Intent(activity, Login::class.java))
            activity?.finish()
        }

        return binding.root
    }

    private fun userLogin(email: String?, password: String?) {
        val mAuth = FirebaseAuth.getInstance()

        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = mAuth.currentUser

                        if (user?.isEmailVerified == true) {
                            // Show user information

                        } else {
                            user?.sendEmailVerification()
                            Toast.makeText(activity, "Check your email to verify your account!", Toast.LENGTH_LONG).show()
                        }

                    } else {
                        Toast.makeText(activity, "Failed to Login! Please check your credentials.", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateViews()

    }


    private fun openTutorialActivity() {
        val intent = Intent(activity, TutorialActivity::class.java)
        startActivity(intent)
    }

    private fun openHelpCenterActivity() {
        val intent = Intent(activity, HelpCenterActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

        private fun initiateViews() {
            try {
                user = FirebaseAuth.getInstance().currentUser!!
                user.email
            } catch (e: NullPointerException) {
                // Handle the exception
            }

            viewModel.allContacts.observe(this.viewLifecycleOwner) { myList ->
                if (myList.isNotEmpty()) {
                    contact = myList[0]
                    bind(contact)
                    binding.user0.text = myList.elementAtOrNull(0)?.name
                    binding.user1.text = myList.elementAtOrNull(1)?.name
                    binding.user2.text = myList.elementAtOrNull(2)?.name

                } else {
                    binding.messageDisplayed.text = getString(R.string.default_message)
                }

            }

            try {
                user = FirebaseAuth.getInstance().currentUser!!
                reference = FirebaseDatabase.getInstance().getReference("Users")
                userID = user.uid

                reference.child(userID).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userProfile = snapshot.getValue(User::class.java)

                        if (userProfile != null) {
                            val fullName = userProfile.fullname
                            val email = userProfile.email
                            val age = userProfile.age

                            fullNameTextView.text = fullName
                            emailTextView.text = email
                            ageTextView.text = age
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(
                            requireContext(),
                            "Something Wrong happened!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            } catch (e: NullPointerException) {
                // Handle the exception
            }
        }
    

        private fun bind (contact : Contacts){
        binding.messageDisplayed.text = contact.message
        binding.customizeBtn.setOnClickListener {
            val action = DashBoardFragmentDirections.actionDashBoardFragmentToAddContactFragment(contact.id)
            findNavController().navigate(action)
        }
    }

}
