package com.fypvpreventor.VpreventorFYP.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fypvpreventor.VpreventorFYP.ContactsApplication
import com.fypvpreventor.VpreventorFYP.R
import com.fypvpreventor.VpreventorFYP.adapters.ContactListAdapter
import com.fypvpreventor.VpreventorFYP.databinding.FragmentContactsListBinding
import com.fypvpreventor.VpreventorFYP.viewmodels.ContactsViewModel
import com.fypvpreventor.VpreventorFYP.viewmodels.ContactsViewModelFactory


class ContactsListFragment : Fragment() {

    //set up binding
    private var _binding : FragmentContactsListBinding ?= null
    private val binding get() = _binding!!

    //declare views
    private lateinit var recyclerView: RecyclerView

    //reference to viewModel
    private val viewModel: ContactsViewModel by activityViewModels {
        ContactsViewModelFactory(
            (activity?.application as ContactsApplication).database.contactsDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       _binding = FragmentContactsListBinding.inflate(inflater, container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.recView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //adapter property
        val adapter = ContactListAdapter{
            val action = ContactsListFragmentDirections.actionContactsFragmentToAddContactFragment(it.id)
            findNavController().navigate(action)
        }
        recyclerView.adapter = adapter

        viewModel.allContacts.observe(this.viewLifecycleOwner){contacts ->
            contacts.let {
                adapter.submitList(it)
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_contactsFragment_to_addContactFragment)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}