package com.lastregrets.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "resonate_prefs")

/**
 * 共鸣状态持久化存储
 * 使用 DataStore 记录用户已共鸣的遗憾 ID，防止重复共鸣
 */
class ResonateStore(private val context: Context) {

    private val RESONATED_IDS_KEY = stringSetPreferencesKey("resonated_ids")

    val resonatedIds: Flow<Set<String>> = context.dataStore.data.map { prefs ->
        prefs[RESONATED_IDS_KEY] ?: emptySet()
    }

    suspend fun addResonatedId(id: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[RESONATED_IDS_KEY] ?: emptySet()
            prefs[RESONATED_IDS_KEY] = current + id
        }
    }

    suspend fun isResonated(id: String): Boolean {
        return context.dataStore.data.first()[RESONATED_IDS_KEY]?.contains(id) ?: false
    }
}
