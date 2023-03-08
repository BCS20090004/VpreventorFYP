package com.fypvpreventor.VpreventorFYP.viewmodels

import androidx.lifecycle.*
import com.fypvpreventor.VpreventorFYP.database.ContactDao
import com.fypvpreventor.VpreventorFYP.database.Contacts
import kotlinx.coroutines.launch

class ContactsViewModel(private val contactsDao : ContactDao) : ViewModel() {

    //retrieve all phone numbers
    val allContacts : LiveData<List<Contacts>> = contactsDao.getAll().asLiveData()

    fun isEntryValid(contactName: String, phoneNumber: String): Boolean {
        if(contactName.isBlank() || phoneNumber.isBlank() ){
            return false
        }
        return true
    }

    //Inserts New Contact to database
    fun addNewContact(contactName : String, phoneNumber: String){
        val newContact = getNewContactEntry(contactName,phoneNumber)
        insertContact(newContact)
    }

    //launch coroutine to insert object to database
    private fun insertContact(contact : Contacts){
        viewModelScope.launch {
            contactsDao.insert(contact)
        }
    }

    private fun getNewContactEntry(contactName: String, phone: String): Contacts{
        return Contacts(name = contactName, phoneNumber = phone)
    }

    fun retrieveContact(id : Int) : LiveData<Contacts>{
        return contactsDao.getContact(id).asLiveData()
    }

    fun updateContact(contactId : Int, contactName: String, contactNumber: String, message : String){
        val updatedContact = getUpdatedEntry(contactId,contactName,contactNumber,message)
        updateContact(updatedContact)
    }
    private fun getUpdatedEntry(id: Int, name:String,number:String, message: String) : Contacts{
        return Contacts(
            id = id,
            name = name,
            phoneNumber = number,
            message = message
        )
    }
    private fun updateContact(contact : Contacts){
        viewModelScope.launch {
            contactsDao.update(contact)
        }
    }

     fun deleteContact(contact : Contacts){
        viewModelScope.launch {
            contactsDao.delete(contact)
        }
    }

}

// factory, that will instantiate view model objects for you.
class ContactsViewModelFactory( private val contactsDao: ContactDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
       if(modelClass.isAssignableFrom(ContactsViewModel::class.java)){
           @Suppress("UNCHECKED_CAST")
           return ContactsViewModel(contactsDao) as T
       }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }

}