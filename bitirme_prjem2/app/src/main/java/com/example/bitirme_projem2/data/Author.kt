package com.example.bitirme_projem2.data

import com.google.firebase.database.Exclude

class Author {

    @get : Exclude
    var id: String? = null
    var name: String? = null

}