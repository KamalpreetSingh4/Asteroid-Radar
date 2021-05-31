package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.getSeventhDay
import com.udacity.asteroidradar.api.getToday
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.asDomainModelPicture
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.NetworkAsteroid
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidDatabase){

    //methods for options menu

    // list of today asteroids
    val asteroids: LiveData<List<Asteroid>> =
            Transformations.map(database.asteroidsDao.getAsteroidToday(getToday())) {
        it?.asDomainModel()
    }

    // picture of day
    val pictureOfDay: LiveData<PictureOfDay> =
            Transformations.map(database.pictureOfDayDao.getPictureOfDay()) {
        it?.asDomainModelPicture()
    }

    //returns asteroids for the week to implement the options menu
    val asteroidsWeek: LiveData<List<Asteroid>> =
            Transformations.map(database.asteroidsDao.getAsteroidsWeek(getToday(), getSeventhDay())) {
        it?.asDomainModel()
    }

    //return saved asteroids in the database to use it in the options menu
    val asteroidsSaved: LiveData<List<Asteroid>> =
            Transformations.map(database.asteroidsDao.getAsteroids()) {
        it?.asDomainModel()
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val jsonResult = Network.asteroids.getAsteroids(getToday(), getSeventhDay(), API_KEY)
            val asteroids = parseAsteroidsJsonResult(JSONObject(jsonResult))
            //converting list of Asteroids(domain objects) to list of network objects
            val networkAsteroidList = asteroids.map {
                NetworkAsteroid(
                        it.id,
                        it.codename,
                        it.closeApproachDate,
                        it.absoluteMagnitude,
                        it.estimatedDiameter,
                        it.relativeVelocity,
                        it.distanceFromEarth,
                        it.isPotentiallyHazardous)
            }
            //adding to database and converting the network objects to database objects using .asDatabaseModel() method
            database.asteroidsDao.insertAll(*networkAsteroidList.asDatabaseModel())
        }
    }
    suspend fun refreshPictureOfDay() {
        withContext(Dispatchers.IO) {
            val pictureOfDay = Network.asteroids.getPictureOfDay(API_KEY)
            database.pictureOfDayDao.insertPicture(pictureOfDay.asDatabaseModel())
        }
    }
}

