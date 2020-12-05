package com.example.moviecatalogue.utils


import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject

class DiskIOThreadExecutor @Inject constructor(): Executor {
    private val diskIO = Executors.newSingleThreadExecutor()

    override fun execute(command: Runnable) {
        diskIO.execute(command)
    }
}