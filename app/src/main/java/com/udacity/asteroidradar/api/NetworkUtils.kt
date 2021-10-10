package com.udacity.asteroidradar.api

import android.util.Log
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.model.PictureOfDay
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun parseAsteroidsJsonResult(jsonResult: JSONObject): ArrayList<Asteroid> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")
    val asteroidList = ArrayList<Asteroid>()
    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()

    Log.i("FEED", nearEarthObjectsJson.toString())

    for (formattedDate in nextSevenDaysFormattedDates) {
        val dateAsteroidJsonArray = nearEarthObjectsJson.getJSONArray(formattedDate)

        for (i in 0 until dateAsteroidJsonArray.length()) {
            val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)

            val id = asteroidJson.getLong("id")
            val codename = asteroidJson.getString("name")
            val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
            val estimatedDiameter = asteroidJson.getJSONObject("estimated_diameter")
                .getJSONObject("kilometers").getDouble("estimated_diameter_max")

            val closeApproachData = asteroidJson
                .getJSONArray("close_approach_data").getJSONObject(0)
            val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                .getDouble("kilometers_per_second")
            val distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
                .getDouble("astronomical")
            val isPotentiallyHazardous = asteroidJson
                .getBoolean("is_potentially_hazardous_asteroid")

            val asteroid = Asteroid(id, codename, formattedDate, absoluteMagnitude,
                estimatedDiameter, relativeVelocity, distanceFromEarth, isPotentiallyHazardous)
            asteroidList.add(asteroid)
        }
    }

    return asteroidList
}

private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()

    val calendar = Calendar.getInstance()

    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}

fun parseImageOfTheDayJsonResult(jsonResult: JSONObject): PictureOfDay {
    val imageOfTheDayUrl = jsonResult.getString("url")
    val imageOfTheDayMediaType = jsonResult.getString("media_type")
    val imageOfTheDayTitle = jsonResult.getString("title")

    val pictureOfDay = PictureOfDay(
        imageOfTheDayMediaType,
        imageOfTheDayTitle,
        imageOfTheDayUrl
    )

    return pictureOfDay
//    {
//    "copyright": "Jean-Yves Letellier",
//    "date": "2020-12-05",
//    "explanation": "Mons Rumker, a 70 kilometer wide complex of volcanic domes, rises some 1100 meters above the vast, smooth lunar mare known as Oceanus Procellarum, the Ocean of Storms. Daylight came to the area late last month. The lunar terminator, the shadow line between night and day, runs diagonally across the left side in this telescopic close-up of a waxing gibbous Moon from November 27. China's Chang'e-5 mission landing site is also in the frame. The probe's lander-ascender combination touch down on the lunar surface within a region right of center and north of Mons Rumker's domes on December 1. On December 3 the ascender left the Ocean of Storms carrying 2 kilograms of lunar material for return to planet Earth.",
//    "hdurl": "https://apod.nasa.gov/apod/image/2012/MonsRumker_Letellier.jpg",
//    "media_type": "image",
//    "service_version": "v1",
//    "title": "Mons Rumker in the Ocean of Storms",
//    "url": "https://apod.nasa.gov/apod/image/2012/MonsRumker_Letellier.jpg"
//}

}