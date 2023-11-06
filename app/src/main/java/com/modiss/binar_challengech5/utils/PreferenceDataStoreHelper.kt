package com.modiss.binar_challengech5.utils

import kotlinx.coroutines.flow.Flow
import java.io.IOException

interface PreferenceDataStoreHelper {
    fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T>
    suspend fun <T> getFirstPreference(key: Preferences.Key<T>, defaultValue: T): T
    suspend fun <T> putPreference(key: Preferences.Key<T>, value: T)
    suspend fun <T> removePreference(key: Preferences.Key<T>)
    suspend fun <T> clearAllPreference()
}

class PreferenceDataStoreHelperImpl(private val dataStore: DataStore<Preferences>) :
    PreferenceDataStoreHelper {

    /* This returns us a flow of data from DataStore.
    Basically as soon we update the value in Datastore,
    the values returned by it also changes. */
    override fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T):
            Flow<T> = dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val result = preferences[key] ?: defaultValue
        result
    }

    /* This returns the last saved value of the key. If we change the value,
        it wont effect the values produced by this function */
    override suspend fun <T> getFirstPreference(key: Preferences.Key<T>, defaultValue: T):
            T = dataStore.data.first()[key] ?: defaultValue

    // This Sets the value based on the value passed in value parameter.
    override suspend fun <T> putPreference(key: Preferences.Key<T>, value: T) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    // This Function removes the Key Value pair from the datastore, hereby removing it completely.
    override suspend fun <T> removePreference(key: Preferences.Key<T>) {
        dataStore.edit { preferences ->
            preferences.remove(key)
        }
    }

    // This function clears the entire Preference Datastore.
    override suspend fun <T> clearAllPreference() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}