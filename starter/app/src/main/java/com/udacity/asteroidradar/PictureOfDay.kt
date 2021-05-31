package com.udacity.asteroidradar

import com.squareup.moshi.Json

//domain object for picture of day
data class PictureOfDay(@Json(name = "media_type") val mediaType: String, val title: String,
                        val url: String)