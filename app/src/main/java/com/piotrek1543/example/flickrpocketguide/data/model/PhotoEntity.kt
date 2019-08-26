package com.piotrek1543.example.flickrpocketguide.data.model


class PhotoEntity(
    var id: String? = null,
    var owner: String? = null,
    var secret: String? = null,
    var server: String? = null,
    var farm: Int? = null,
    var title: String? = null,
    var ispublic: Int? = null,
    var isfriend: Int? = null,
    var isfamily: Int? = null
)
