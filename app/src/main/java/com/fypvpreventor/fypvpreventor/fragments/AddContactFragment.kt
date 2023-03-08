package com.fypvpreventor.VpreventorFYP.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder

import com.fypvpreventor.VpreventorFYP.ContactsApplication
import com.fypvpreventor.VpreventorFYP.R
import com.fypvpreventor.VpreventorFYP.database.Contacts
import com.fypvpreventor.VpreventorFYP.databinding.FragmentAddContactBinding
import com.fypvpreventor.VpreventorFYP.viewmodels.ContactsViewModel
import com.fypvpreventor.VpreventorFYP.viewmodels.ContactsViewModelFactory


class AddContactFragment : Fragment() {

    private var _binding : FragmentAddContactBinding ?= null
    private val  binding get() = _binding!!

     lateinit var contact : Contacts

    //arguments received
    private val navigationArgs : AddContactFragmentArgs by navArgs()

    //view model reference
    private val viewModel: ContactsViewModel by activityViewModels {
        ContactsViewModelFactory(
            (activity?.application as ContactsApplication).database.contactsDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddContactBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = navigationArgs.contactId
        if(id > 0){
            viewModel.retrieveContact(id).observe(this.viewLifecycleOwner){selectedItem ->
                contact = selectedItem
                bind(contact)
            }
        }else {
            binding.addContact.setOnClickListener {
                addNewContact()
            }
        }
    }

    private fun addNewContact(){
        if(isEntryValid()){
            viewModel.addNewContact(
                binding.personName.text.toString(),
                binding.phone.text.toString()
            )
            val action = AddContactFragmentDirections.actionAddContactFragmentToContactsFragment()
            findNavController().navigate(action)
        }
    }

    private fun isEntryValid(): Boolean{
        return viewModel.isEntryValid(
            binding.personName.text.toString(),
            binding.phone.text.toString()
        )
    }

    private fun bind(contact: Contacts){
        binding.apply {
            personName.setText(contact.name)
            phone.setText(contact.phoneNumber)
            addContact.setOnClickListener{ updateContact() }
            deleteBtn.isVisible = true
            messageTitle.isVisible = true
            personalMessage.isVisible = true
            personalMessage.setText(contact.message)
            deleteBtn.setOnClickListener{showConfirmDialog()}
        }
    }

    private fun showConfirmDialog(){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.dialog_alert_title))
            .setMessage("Are you sure you want to delete?")
            .setCancelable(false)
            .setNegativeButton("No"){ _, _ -> }
            .setPositiveButton("Yes"){ _, _-> deleteContact()}
            .show()
    }

    private fun deleteContact(){
        viewModel.deleteContact(contact)
        findNavController().navigateUp()
    }

    private fun updateContact(){
        if(isEntryValid()){
            viewModel.updateContact(
                this.navigationArgs.contactId,
                this.binding.personName.text.toString(),
                this.binding.phone.text.toString(),
                this.binding.personalMessage.text.toString()
            )
        }
        findNavController().navigateUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //hide keyboard after user clicks enter
        val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null

    }
}