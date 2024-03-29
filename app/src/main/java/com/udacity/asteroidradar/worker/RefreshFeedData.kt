package com.udacity.asteroidradar.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshFeedData(
    appContext: Context,
    params: WorkerParameters
): CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshFeedWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.getAllAsteroids()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}