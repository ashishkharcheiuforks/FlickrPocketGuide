package com.piotrek1543.example.flickrpocketguide.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
class CachedPhoto(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String,
    @ColumnInfo(name = "owner")
    var owner: String? = null,
    @ColumnInfo(name = "secret")
    var secret: String? = null,
    @ColumnInfo(name = "server")
    var server: String? = null,
    @ColumnInfo(name = "farm")
    var farm: Int? = null,
    @ColumnInfo(name = "title")
    var title: String? = null,
    @ColumnInfo(name = "ispublic")
    var ispublic: Int? = null,
    @ColumnInfo(name = "isfriend")
    var isfriend: Int? = null,
    @ColumnInfo(name = "isfamily")
    var isfamily: Int? = null
)
