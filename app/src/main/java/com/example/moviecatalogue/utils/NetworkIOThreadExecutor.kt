package com.example.moviecatalogue.utils

import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject

class NetworkIOThreadExecutor @Inject constructor() : Executor {
    private val networkIO = Executors.newSingleThreadExecutor()

    override fun execute(command: Runnable) {
        networkIO.execute(command)
    }
}