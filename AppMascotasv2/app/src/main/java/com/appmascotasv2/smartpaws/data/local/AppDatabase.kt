package com.appmascotasv2.smartpaws.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.appmascotasv2.smartpaws.data.local.dao.EventoMascotaDao
import com.appmascotasv2.smartpaws.data.local.dao.MascotaDao
import com.appmascotasv2.smartpaws.data.local.dao.UsuarioDao
import com.appmascotasv2.smartpaws.data.local.entity.EventoMascotaEntity
import com.appmascotasv2.smartpaws.data.local.entity.MascotaEntity
import com.appmascotasv2.smartpaws.data.local.entity.UsuarioEntity

@Database(
    entities  = [UsuarioEntity::class, MascotaEntity::class, EventoMascotaEntity::class],
    version   = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun eventoMascotaDao(): EventoMascotaDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smartpaws.db"
                )
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
            }
    }
}