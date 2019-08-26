package com.piotrek1543.example.flickrpocketguide.remote.mapper

import com.piotrek1543.example.flickrpocketguide.data.model.PhotoEntity
import com.piotrek1543.example.flickrpocketguide.remote.model.PhotoModel

/**
 * Map a [PhotoModel] to and from a [PhotoEntity] instance when data is moving between
 * this later and the Data layer
 */
open class RemotePhotoEntityMapper : EntityMapper<PhotoModel, PhotoEntity> {

    /**
     * Map an instance of a [PhotoModel] to a [PhotoEntity] model
     */
    override fun mapFromRemote(type: PhotoModel): PhotoEntity = PhotoEntity(
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