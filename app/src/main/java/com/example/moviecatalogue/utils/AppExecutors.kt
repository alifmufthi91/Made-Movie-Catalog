package com.example.moviecatalogue.utils

import androidx.annotation.VisibleForTesting
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Named

class AppExecutors @VisibleForTesting @Inject constructor(
    @Named("diskIoExecutor")
    val diskIO: Executor,
    @Named("networkIoExecutor")
    val networkIO: Executor,
    @Named("mainThreadExecutor")
    val mainThread: Executor
)
