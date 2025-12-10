package com.edu.darvyn.nocap.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.edu.darvyn.nocap.data.remote.Resource
import com.edu.darvyn.nocap.domain.repository.UsuarioRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.IOException


@HiltWorker
class SyncUsuarioWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: UsuarioRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val USUARIO_ID_KEY = "usuario_id"
        const val WORK_NAME = "sync_usuario_worker"
        private const val MAX_ATTEMPTS = 3
    }

    override suspend fun doWork(): Result {
        return try {
            val usuarioId = inputData.getString(USUARIO_ID_KEY)
                ?: return Result.failure()


            when (val result = repository.syncUsuarioToRemote(usuarioId)) {
                is Resource.Success -> {
                    Result.success()
                }
                is Resource.Error -> {
                    if (runAttemptCount < MAX_ATTEMPTS) {
                        Result.retry()
                    } else {
                        Result.failure()
                    }
                }
                is Resource.Loading -> {
                    Result.failure()
                }
            }
        } catch (e: IOException) {
            if (runAttemptCount < MAX_ATTEMPTS) {
                Result.retry()
            } else {
                Result.failure()
            }
        } catch (e: Exception) {
            Result.failure()
        }
    }
}