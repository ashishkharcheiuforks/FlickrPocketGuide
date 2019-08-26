package com.piotrek1543.example.flickrpocketguide.remote.model

import com.squareup.moshi.Json

class PhotosModel {
    @Json(name = "photos")
    var photos: Photos? = null
    @Json(name = "stat")
    var stat: String? = null
}
