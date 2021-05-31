package com.udacity.asteroidradar.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.DatabasePictureOfDay

//network data class for holding results from retrofit network call
@JsonClass(generateAdapter = true)
data class NetworkAsteroid(
    val id: Long,
    val codeName: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
)

//Convert network object to domain object i.e. the Asteroid object
fun List<NetworkAsteroid>.asDomainModel(): List<Asteroid>{
    return this.map {
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

//Converts the network object to database object
fun List<NetworkAsteroid>.asDatabaseModel(): Array<DatabaseAsteroid>{
    return this.map {
        DatabaseAsteroid(
            id = it.id,
            codeName = it.codeName,
            closeApproachDate =  it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}


//picture of the day network object
data class NetworkPictureOfDay(
    val url: String,
    @Json(name = "media_type") val mediaType: String,
    val title: String
)

//Convert network picture object to domain object
fun NetworkPictureOfDay.asDomainModel(): PictureOfDay {
    return PictureOfDay(
        url = this.url,
        mediaType = this.mediaType,
        title = this.title
    )

}

//Convert picture network object to database object
fun NetworkPictureOfDay.asDatabaseModel(): DatabasePictureOfDay {
    return DatabasePictureOfDay(
        url = this.url,
        mediaType = this.mediaType,
        title = this.title
    )

}



