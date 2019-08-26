package com.piotrek1543.example.flickrpocketguide.model

import com.squareup.moshi.Json

class PhotosEntity {
    @Json(name = "photos")
    var photos: Photos? = null
    @Json(name = "stat")
    var stat: String? = null
}
