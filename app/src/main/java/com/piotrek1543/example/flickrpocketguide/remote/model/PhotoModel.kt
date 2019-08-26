package com.piotrek1543.example.flickrpocketguide.remote.model

import com.squareup.moshi.Json

class PhotoModel {
    @Json(name = "id")
    var id: String? = null
    @Json(name = "owner")
    var owner: String? = null
    @Json(name = "secret")
    var secret: String? = null
    @Json(name = "server")
    var server: String? = null
    @Json(name = "farm")
    var farm: Int? = null
    @Json(name = "title")
    var title: String? = null
    @Json(name = "ispublic")
    var ispublic: Int? = null
    @Json(name = "isfriend")
    var isfriend: Int? = null
    @Json(name = "isfamily")
    var isfamily: Int? = null

}
