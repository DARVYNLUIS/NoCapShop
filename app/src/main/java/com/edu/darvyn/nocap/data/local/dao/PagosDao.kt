package com.edu.darvyn.nocap.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.edu.darvyn.nocap.data.local.entities.PagosEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PagosDao {

    @Upsert()
    suspend fun savePagos(pagos: PagosEntity)

    @Query(
        """Select * From
            pagosentities where pagoId=:id
        """
    )
    suspend fun observeById(id: Int) : PagosEntity

    @Query("""
        Select * from
        pagosentities
    """)
    fun observeAllPagos() : Flow<List<PagosEntity>>

}