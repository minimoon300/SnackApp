package com.eap.snackapp

class User {
    var NAME: String = ""
    var LIKES:  MutableList<String>? = null
    var DISLIKES: MutableList<String>? = null

    constructor() {}

    constructor(
        NAME: String,
        LIKES: MutableList<String>?,
        DISLIKES: MutableList<String>?,
    ) {
        this.NAME = NAME
        this.LIKES = LIKES
        this.DISLIKES = DISLIKES
    }

}