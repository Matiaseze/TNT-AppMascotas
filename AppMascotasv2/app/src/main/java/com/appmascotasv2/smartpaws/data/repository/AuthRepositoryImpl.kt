package com.appmascotasv2.smartpaws.data.repository

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.appmascotasv2.smartpaws.data.local.dao.UsuarioDao
import com.appmascotasv2.smartpaws.data.local.entity.UsuarioEntity
import com.appmascotasv2.smartpaws.data.mapper.toDomain
import com.appmascotasv2.smartpaws.domain.model.usuario.Usuario
import com.appmascotasv2.smartpaws.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.security.MessageDigest

private val Context.dataStore by preferencesDataStore(name = "session")

class AuthRepositoryImpl(
    private val userDao: UsuarioDao,
    private val context: Context
) : AuthRepository {

    private val KEY_USER_ID = intPreferencesKey("logged_user_id")

    override val loggedUserId: Flow<Int> = context.dataStore.data
        .map { prefs -> prefs[KEY_USER_ID] ?: 0 }

    override suspend fun login(username: String, password: String): Result<Usuario> {
        val user = userDao.findByUsername(username)
            ?: return Result.failure(Exception("Usuario no encontrado"))

        if (user.passwordHash != sha256(password))
            return Result.failure(Exception("Contraseña incorrecta"))

        saveSession(user.id)
        return Result.success(user.toDomain())
    }

    override suspend fun register(username: String, password: String): Result<Usuario> {
        if (userDao.findByUsername(username) != null)
            return Result.failure(Exception("El usuario ya existe"))

        val entity = UsuarioEntity(
            username     = username,
            passwordHash = sha256(password)
        )
        val id    = userDao.insert(entity)
        val saved = userDao.findById(id.toInt())!!
        saveSession(saved.id)
        return Result.success(saved.toDomain())
    }

    override suspend fun logout() {
        context.dataStore.edit { it.remove(KEY_USER_ID) }
    }

    override suspend fun getUsuarioById(id: Int): Usuario? =
        userDao.findById(id)?.toDomain()

    private suspend fun saveSession(userId: Int) {
        context.dataStore.edit { it[KEY_USER_ID] = userId }
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // ─── Punto de extensión para Firebase ────────────────────────────────────
    // suspend fun loginWithGoogle(idToken: String): Result<UsuarioEntity> {
    //     val credential = GoogleAuthProvider.getCredential(idToken, null)
    //     val result = FirebaseAuth.getInstance().signInWithCredential(credential).await()
    //     val firebaseUser = result.user ?: return Result.failure(Exception("Error Firebase"))
    //     // Buscá o creá el usuario en Room con provider = "google"
    // }

}