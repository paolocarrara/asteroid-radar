package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.api.NeoApi
import com.udacity.asteroidradar.api.parseImageOfTheDayJsonResult
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainViewModel(application: Application): AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    private val _navigateToAsteroidDetailScreen =  MutableLiveData<Asteroid>()
    val navigateToAsteroidDetailScreen: LiveData<Asteroid>
        get() = _navigateToAsteroidDetailScreen

    private val _asteroids = asteroidRepository.asteroids
    val asteroids: LiveData<List<Asteroid>>
        get() = _asteroids

    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfTheDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    init {
        getFeed()
        getImageOfTheDay()
    }

    private fun getFeed() {
        viewModelScope.launch {
            asteroidRepository.getAllAsteroids()
        }
    }

    private fun getImageOfTheDay() {
        viewModelScope.launch {
            val imageOfTheDayResponse = NeoApi.retrofitService.getImageOfTheDay()
            val jsonResult = JSONObject(imageOfTheDayResponse)
            val imageOfTheDay = parseImageOfTheDayJsonResult(jsonResult)

            _pictureOfTheDay.value = imageOfTheDay
        }
    }

    fun navigateToAsteroidDetailScreen(asteroid: Asteroid) {
        _navigateToAsteroidDetailScreen.value = asteroid
    }

    fun navigateToAsteroidDetailScreenDone() {
        _navigateToAsteroidDetailScreen.value = null
    }
}