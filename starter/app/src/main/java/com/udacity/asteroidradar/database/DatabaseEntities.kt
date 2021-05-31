package com.udacity.asteroidradar.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

//DatabaseAsteroid refers to the Database Object




@Entity
data class DatabaseAsteroid(
    @PrimaryKey
    val id: Long,
    val codeName: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

// Converts the Database Objects to Domain Objects
fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid>{
    return map{
        Asteroid(
            id = it.id,
            codename = it.codeName,
            closeApproachDate =  it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}



//creates database entity i.e. the database object
@Entity
data class DatabasePictureOfDay(
    @PrimaryKey
    val url: String,
    val mediaType: String,
    val title: String)






//converts the database object for pictureOfDay to domain Object
fun DatabasePictureOfDay.asDomainModelPicture(): PictureOfDay {
    return PictureOfDay(
        url = this.url,
        mediaType = this.mediaType,
        title = this.title
    )

}
