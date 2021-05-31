package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidsDao {
    //returns liveData of list of databaseAsteroid objects
    @Query("SELECT  * FROM databaseasteroid ORDER BY closeApproachDate DESC")
    fun getAsteroids() : LiveData<List<DatabaseAsteroid>>

    //stores data in cache
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg asteroids: DatabaseAsteroid)

    //returns live data of list of asteroids today
    @Query("SELECT * FROM DatabaseAsteroid WHERE closeApproachDate=:today ORDER BY closeApproachDate ASC")
    fun getAsteroidToday(today: String): LiveData<List<DatabaseAsteroid>>

    //returns liveData of DatabaseAsteroids for the whole week to implement the options menu
    @Query("SELECT * FROM DatabaseAsteroid WHERE closeApproachDate BETWEEN :today AND " +
            ":seventhDay ORDER BY closeApproachDate ASC")
    fun getAsteroidsWeek(today: String, seventhDay: String): LiveData<List<DatabaseAsteroid>>
}

@Dao
interface PictureOfDayDao {
    //returns liveData of latest picture
    @Query("SELECT * FROM databasepictureofday")
    fun getPictureOfDay(): LiveData<DatabasePictureOfDay>

    //inserts new image in cache
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(pictureOfDay: DatabasePictureOfDay)
}

@Database(entities = [DatabaseAsteroid::class, DatabasePictureOfDay::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase: RoomDatabase() {
    abstract val asteroidsDao: AsteroidsDao
    abstract val pictureOfDayDao: PictureOfDayDao
}

private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {
    synchronized(AsteroidDatabase::class.java) {
        if(!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                AsteroidDatabase::class.java,
                "asteroids")
                .fallbackToDestructiveMigration() //migration strategy
                .build()
        }
    }
    return INSTANCE
}