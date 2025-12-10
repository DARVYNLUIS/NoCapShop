package com.edu.darvyn.nocap.worker

import android.content.Context
import androidx.compose.ui.unit.Constraints
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WorkManagerHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val baseConstraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()

    private val baseBackoff = BackoffPolicy.LINEAR
    private val baseBackoffDelay = 10L

    fun schedulePeriodicSyncWork(usuarioId: String) {
        val syncRequest = PeriodicWorkRequestBuilder<SyncUsuarioWorker>(
            24, TimeUnit.HOURS,
            15, TimeUnit.MINUTES
        )
            .setConstraints(baseConstraints)
            .setInputData(workDataOf(SyncUsuarioWorker.USUARIO_ID_KEY to usuarioId))
            .setBackoffCriteria(
                baseBackoff,
                baseBackoffDelay, TimeUnit.MINUTES
            )
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SyncUsuarioWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }

    fun scheduleOneTimeSync(usuarioId: String) {
        val syncRequest = OneTimeWorkRequestBuilder<SyncUsuarioWorker>()
            .setInputData(workDataOf(SyncUsuarioWorker.USUARIO_ID_KEY to usuarioId))
            .setConstraints(baseConstraints)
            .setBackoffCriteria(
                baseBackoff,
                baseBackoffDelay, TimeUnit.MINUTES
            )
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "${SyncUsuarioWorker.WORK_NAME}_$usuarioId",
            ExistingWorkPolicy.REPLACE,
            syncRequest
        )
    }

    fun cancelPeriodicSyncWork() {
        WorkManager.getInstance(context).cancelUniqueWork(SyncUsuarioWorker.WORK_NAME)
    }
}