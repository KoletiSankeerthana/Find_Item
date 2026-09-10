package com.wheredidikeepit.app.utils

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray

class ProfileManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_profiles_pref", Context.MODE_PRIVATE)

    private val _profiles = MutableStateFlow<List<String>>(emptyList())
    val profiles: StateFlow<List<String>> = _profiles.asStateFlow()

    private val _activeProfile = MutableStateFlow("")
    val activeProfile: StateFlow<String> = _activeProfile.asStateFlow()

    init {
        loadProfiles()
    }

    private fun loadProfiles() {
        val jsonString = prefs.getString("profiles_list", null)
        val list = mutableListOf<String>()
        if (jsonString != null) {
            try {
                val array = JSONArray(jsonString)
                for (i in 0 until array.length()) {
                    val name = array.getString(i).trim()
                    // Filter out legacy placeholder names if empty
                    if (name.isNotEmpty() && name != "Sankeerthana" && name != "Default" && name != "default") {
                        if (!list.contains(name)) {
                            list.add(name)
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        _profiles.value = list
        if (list.isNotEmpty()) {
            val savedActive = prefs.getString("active_profile", list.first())
            val finalActive = if (savedActive != null && list.contains(savedActive)) savedActive else list.first()
            _activeProfile.value = finalActive
        } else {
            _activeProfile.value = ""
        }
    }

    fun hasProfile(name: String): Boolean {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return false
        return _profiles.value.any { it.equals(trimmed, ignoreCase = true) }
    }

    fun setActiveProfile(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        prefs.edit().putString("active_profile", trimmed).commit()
        _activeProfile.value = trimmed
    }

    fun addProfile(name: String): Boolean {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return false
        if (hasProfile(trimmed)) {
            return false // Reject duplicate profile names
        }
        val current = _profiles.value.toMutableList()
        current.add(trimmed)
        saveProfiles(current)
        setActiveProfile(trimmed)
        setOnboardingCompleted(true)
        return true
    }

    private fun saveProfiles(list: List<String>) {
        val array = JSONArray()
        list.forEach { array.put(it) }
        prefs.edit().putString("profiles_list", array.toString()).commit()
        _profiles.value = list
    }

    fun isSetupComplete(): Boolean {
        val hasProfiles = _profiles.value.isNotEmpty()
        val isFlagTrue = prefs.getBoolean("onboarding_completed", false)
        return hasProfiles || (isFlagTrue && hasProfiles)
    }

    fun isOnboardingCompleted(): Boolean {
        return isSetupComplete()
    }

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean("onboarding_completed", completed).commit()
    }
}
