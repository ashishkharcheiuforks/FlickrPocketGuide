package com.piotrek1543.example.flickrpocketguide.cache.mapper

import com.piotrek1543.example.flickrpocketguide.cache.model.CachedPhoto
import com.piotrek1543.example.flickrpocketguide.data.model.PhotoEntity

open class CachedPhotoEntityMapper :
    EntityMapper<CachedPhoto, PhotoEntity> {

    /**
     * Map a [PhotoEntity] instance to a [CachedPhoto] instance
     */
    override fun mapToCached(type: PhotoEntity): CachedPhoto = CachedPhoto(
        id = type.id,
        owner = type.owner,
        secret = type.secret,
        server = type.server,
        farm = type.farm,
        title = type.title,
        ispublic = type.ispublic,
        isfriend = type.isfriend,
        isfamily = type.isfamily
    )

    /**
     * Map a [CachedPhoto] instance to a [PhotoEntity] instance
     */
    override fun mapFromCached(type: CachedPhoto): PhotoEntity =
        PhotoEntity(
            id = type.id,
            owner = type.owner,
            secret = type.secret,
            server = type.server,
            farm = type.farm,
            title = type.title,
            ispublic = type.ispublic,
            isfriend = type.isfriend,
            isfamily = type.isfamily
        )
}