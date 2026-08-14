package com.mevi.lasheslam.domain.repository

/** Deduplicación en memoria: evita reprocesar el mismo id en la sesión actual. */
interface NotificationCache {
    /** Devuelve true la primera vez que se ve el id; false en llamadas posteriores. */
    fun shouldProcess(id: String): Boolean
}
