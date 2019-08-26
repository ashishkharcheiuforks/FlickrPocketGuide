package com.piotrek1543.example.flickrpocketguide.model

import com.squareup.moshi.Json

class Photos {

    @Json(name = "page")
    var page: Int? = null
    @Json(name = "pages")
    var pages: Int? = null
    @Json(name = "perpage")
    var perpage: Int? = null
    @Json(name = "total")
    var total: String? = null
    @Json(name = "photo")
    var photo: List<Photo>? = null

}
