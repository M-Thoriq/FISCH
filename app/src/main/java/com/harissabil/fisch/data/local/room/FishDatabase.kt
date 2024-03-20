package com.harissabil.fisch.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.harissabil.fisch.data.local.entity.FishEntity

@Database(
    entities = [FishEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class FishDatabase : RoomDatabase() {
    abstract fun fishDao(): FishDao

    companion object {
        @Volatile
        private var INSTANCE: FishDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FishDatabase {
            if (INSTANCE == null) {
                synchronized(FishDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FishDatabase::class.java,
                        "fish_database"
                    ).build()
                }
            }
            return INSTANCE as FishDatabase
        }
    }
}