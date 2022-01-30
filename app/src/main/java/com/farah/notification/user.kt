package com.farah.notification

import com.google.firebase.database.IgnoreExtraProperties

class user {
    var name1 : String? = null
    var email1: String? = null
    var uid : String? = null

    constructor(){}
    constructor(name1: String?,email1:String?,uid:String?){
        this.name1 = name1
        this.email1 = email1
        this.uid = uid

    }

}