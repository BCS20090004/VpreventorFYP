package com.fypvpreventor.VpreventorFYP.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.fypvpreventor.VpreventorFYP.ContactsApplication
import com.fypvpreventor.VpreventorFYP.R
import com.fypvpreventor.VpreventorFYP.database.Contacts
import com.fypvpreventor.VpreventorFYP.databinding.FragmentDashBoardBinding
import com.fypvpreventor.VpreventorFYP.viewmodels.ContactsViewModel
import com.fypvpreventor.VpreventorFYP.viewmodels.ContactsViewModelFactory

class DashBoardFragment : Fragment() {

    private var _binding : FragmentDashBoardBinding?= null
    private val  binding get() = _binding!!

    private lateinit var contact: Contacts

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
        // Inflate the layout for this fragment
        _binding = FragmentDashBoardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initiateViews()

    }

    private fun initiateViews(){

        viewModel.allContacts.observe(this.viewLifecycleOwner){ myList ->
            if(myList.isNotEmpty()){
               contact = myList[0]
               bind(contact)
                binding.user0.text = myList.elementAtOrNull(0)?.name
                binding.user1.text = myList.elementAtOrNull(1)?.name
                binding.user2.text = myList.elementAtOrNull(2)?.name

            }else{
                binding.messageDisplayed.text = getString(R.string.default_message)
            }

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
