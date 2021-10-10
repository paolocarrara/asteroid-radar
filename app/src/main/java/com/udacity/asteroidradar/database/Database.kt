package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        Asteroid::class
    ],
    version = 1
)
abstract class Database: RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: com.udacity.asteroidradar.database.Database

fun getDatabase(context: Context): com.udacity.asteroidradar.database.Database {
    synchronized(com.udacity.asteroidradar.database.Database::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext, com.udacity.asteroidradar.database.Database::class.java, "asteroid_radar_app").build()
        }
    }

    return INSTANCE
}