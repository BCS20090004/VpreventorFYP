package com.fypvpreventor.VpreventorFYP.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fypvpreventor.VpreventorFYP.database.Contacts
import com.fypvpreventor.VpreventorFYP.databinding.ContactBinding

class ContactListAdapter (private val onItemClicked: (Contacts) -> Unit) :
    ListAdapter<Contacts, ContactListAdapter.ContactViewHolder>(DiffCallback){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ContactBinding.inflate(
            LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val  current = getItem(position)
        holder.itemView.setOnClickListener{
            onItemClicked(current)
        }
        holder.bind(current)
    }

    //view holder for adapter
    class ContactViewHolder(private var binding: ContactBinding) :
        RecyclerView.ViewHolder(binding.root){

            fun bind(contact : Contacts){
                binding.contactName.text = contact.name
                binding.phoneNumber.text = contact.phoneNumber
            }
    }

    //This is just an object that helps the ListAdapter determine which items in the new and old lists are different when updating the list.
    companion object{
        private val DiffCallback = object : DiffUtil.ItemCallback<Contacts>(){
            override fun areItemsTheSame(oldItem: Contacts, newItem: Contacts): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Contacts, newItem: Contacts): Boolean {
                return oldItem == newItem
            }
        }
    }
}