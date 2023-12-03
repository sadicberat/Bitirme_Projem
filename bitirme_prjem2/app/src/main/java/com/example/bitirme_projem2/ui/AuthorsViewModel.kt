package com.example.bitirme_projem2.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bitirme_projem2.data.Author
import com.example.bitirme_projem2.data.NODE_AUTHORS
import com.google.firebase.database.FirebaseDatabase

class AuthorsViewModel : ViewModel(){

    private val _result = MutableLiveData<Exception?>()

    val result : LiveData<Exception?>
        get() = _result

    fun addAuthor (author: Author){
        val dpAuthors = FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
        author.id = dpAuthors.push().key

        dpAuthors.child(author.id!!).setValue(author)
            .addOnCompleteListener(){
                if (it.isSuccessful){
                    _result.value = null
                }else{
                    _result.value = it.exception!!
                }
            }
    }

}