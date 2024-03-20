package com.harissabil.fisch.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "fish",
    indices = [Index(value = ["price"], unique = false), Index(value = ["weight"], unique = false)]
)
@Parcelize
data class FishEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int = 0,

    @ColumnInfo(name = "location")
    var location: String,

    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "price")
    var price: Int,

    @ColumnInfo(name = "weight")
    var weight: Int

) : Parcelable