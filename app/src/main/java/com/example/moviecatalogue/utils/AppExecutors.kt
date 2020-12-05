package com.example.moviecatalogue.utils

import android.os.Handler
import android.os.Looper
import androidx.annotation.VisibleForTesting
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Inject
import javax.inject.Named

class AppExecutors @VisibleForTesting @Inject constructor(
    @Named("diskIoExecutor")
    val diskIO: Executor,
    @Named("networkIoExecutor")
    val networkIO: Executor,
    @Named("mainThreadExecutor")
    val mainThread: Executor
) {
}
