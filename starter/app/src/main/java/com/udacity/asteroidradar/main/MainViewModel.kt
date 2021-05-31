package com.udacity.asteroidradar.main

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception


class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    val asteroids = asteroidsRepository.asteroids //returns asteroids for today
    val pictureOfDay = asteroidsRepository.pictureOfDay //returns picture of day

    //mutable live data to change in this file
    private val _navigateToDetailFragment = MutableLiveData<Asteroid>()

    //live data to observe in fragments
    val navigateToDetailFragment: LiveData<Asteroid>
        get() = _navigateToDetailFragment

    private val _optionsSelected = MutableLiveData<OptionsSelected>()
    val optionSelected: LiveData<OptionsSelected>
        get() = _optionsSelected

    init {
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshPictureOfDay()
                displayOptionsSelected(OptionsSelected.TODAY)
                asteroidsRepository.refreshAsteroids()
            } catch (e: Exception) {
                displayOptionsSelected(OptionsSelected.SAVED)
                Log.e("MainViewModel",e.message!!)
            }
        }
    }

    val asteroidsList = Transformations.switchMap(_optionsSelected) {
        when(it) {
            OptionsSelected.WEEK -> asteroidsRepository.asteroidsWeek
            OptionsSelected.TODAY -> asteroidsRepository.asteroids
            OptionsSelected.SAVED -> asteroidsRepository.asteroidsSaved
        }
    }

    fun displayOptionsSelected(optionsSelected: OptionsSelected) {
        _optionsSelected.value = optionsSelected
    }

    //when we click an asteroid item view we set the _navigateToDetailFragment to the selected property from the adapter
    fun displayPropertyDetails(asteroid: Asteroid) {
        _navigateToDetailFragment.value = asteroid
    }

    // after navigation we call this immediately
    fun displayPropertyDetailsComplete() {
        _navigateToDetailFragment.value = null
    }

    /**
     * Factory for constructing MainViewModel with parameter
     */
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(application) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel")
        }
    }
}