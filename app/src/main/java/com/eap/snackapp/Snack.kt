package com.eap.snackapp

class Snack {
    var NAME: String = ""
    var WEIGHT: Long = 0
    var CALS_PER100g: Long = 0
    var KJ_PER100G: Long = 0
    var LOCATION: HashMap<String, Boolean>? = null
    var INGREDIENTS: HashMap<String, Int>? = null
    var IMAGE_LINK: String = ""
    var SEARCH_KEYWORDS: MutableList<String>? = null

    constructor() {}

    constructor(
        NAME: String,
        WEIGHT: Long,
        CALS_PER100g: Long,
        KJ_PER100G: Long,
        LOCATION: HashMap<String, Boolean>?,
        INGREDIENTS: HashMap<String, Int>?,
        IMAGE_LINK: String,
        SEARCH_KEYWORDS: MutableList<String>?
    ) {
        this.NAME = NAME
        this.WEIGHT = WEIGHT
        this.CALS_PER100g = CALS_PER100g
        this.KJ_PER100G = KJ_PER100G
        this.LOCATION = LOCATION
        this.INGREDIENTS = INGREDIENTS
        this.IMAGE_LINK = IMAGE_LINK
        this.SEARCH_KEYWORDS = SEARCH_KEYWORDS
    }

}