package com.dmm.cheappcgames.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dmm.cheappcgames.data.StoreItem

@Database(entities = [StoreItem::class], version = 1)
@TypeConverters(ImagesConverter::class)
abstract class CheapPcDataBase : RoomDatabase() {

    abstract fun storeDao(): StoreDao

    companion object {
        @Volatile
        private var INSTANCE: CheapPcDataBase?= null

        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: createDatabase(context).also { INSTANCE = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                CheapPcDataBase::class.java,
                "deals_games_database.db"
            ).build()
    }
}