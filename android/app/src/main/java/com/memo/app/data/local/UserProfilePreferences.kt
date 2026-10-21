package com.memo.app.data.local

import android.content.Context
import android.content.SharedPreferences
import com.memo.app.data.model.User

object UserProfilePreferences {
    private const val PREFS_NAME = "memo_user_profile_prefs"
    private const val KEY_ID = "pref_user_id"
    private const val KEY_NAME = "pref_user_name"
    private const val KEY_NICKNAME = "pref_user_nickname"
    private const val KEY_EMAIL = "pref_user_email"
    private const val KEY_MEMBER_SINCE = "pref_user_member_since"
    private const val KEY_BLOOD_GROUP = "pref_user_blood_group"
    private const val KEY_AGE = "pref_user_age"
    private const val KEY_GENDER = "pref_user_gender"
    private const val KEY_EMERGENCY_CONTACT = "pref_user_emergency_contact"
    private const val KEY_ALLERGIES = "pref_user_allergies"
    private const val KEY_PROFILE_COMPLETED = "pref_user_is_profile_completed"

    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        if (sharedPreferences == null) {
            sharedPreferences = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        }
    }

    fun isProfileCompleted(): Boolean {
        return sharedPreferences?.getBoolean(KEY_PROFILE_COMPLETED, false) ?: false
    }

    fun setProfileCompleted(completed: Boolean) {
        sharedPreferences?.edit()?.putBoolean(KEY_PROFILE_COMPLETED, completed)?.apply()
    }

    fun saveUser(user: User) {
        sharedPreferences?.edit()?.apply {
            putString(KEY_ID, user.id)
            putString(KEY_NAME, user.name)
            putString(KEY_NICKNAME, user.nickname)
            putString(KEY_EMAIL, user.email)
            putString(KEY_MEMBER_SINCE, user.memberSince)
            putString(KEY_BLOOD_GROUP, user.bloodGroup)
            putInt(KEY_AGE, user.age)
            putString(KEY_GENDER, user.gender)
            putString(KEY_EMERGENCY_CONTACT, user.emergencyContact)
            putString(KEY_ALLERGIES, user.allergies)
            putBoolean(KEY_PROFILE_COMPLETED, user.isProfileCompleted)
            apply()
        }
    }

    fun loadUser(defaultUser: User): User {
        val prefs = sharedPreferences ?: return defaultUser
        if (!prefs.contains(KEY_ID) && !prefs.contains(KEY_NAME) && !prefs.contains(KEY_BLOOD_GROUP)) {
            return defaultUser
        }
        return User(
            id = prefs.getString(KEY_ID, defaultUser.id) ?: defaultUser.id,
            name = prefs.getString(KEY_NAME, defaultUser.name) ?: defaultUser.name,
            nickname = prefs.getString(KEY_NICKNAME, defaultUser.nickname) ?: defaultUser.nickname,
            email = prefs.getString(KEY_EMAIL, defaultUser.email) ?: defaultUser.email,
            memberSince = prefs.getString(KEY_MEMBER_SINCE, defaultUser.memberSince) ?: defaultUser.memberSince,
            bloodGroup = prefs.getString(KEY_BLOOD_GROUP, defaultUser.bloodGroup) ?: defaultUser.bloodGroup,
            age = prefs.getInt(KEY_AGE, defaultUser.age),
            gender = prefs.getString(KEY_GENDER, defaultUser.gender) ?: defaultUser.gender,
            emergencyContact = prefs.getString(KEY_EMERGENCY_CONTACT, defaultUser.emergencyContact) ?: defaultUser.emergencyContact,
            allergies = prefs.getString(KEY_ALLERGIES, defaultUser.allergies) ?: defaultUser.allergies,
            isProfileCompleted = prefs.getBoolean(KEY_PROFILE_COMPLETED, defaultUser.isProfileCompleted)
        )
    }

    fun clear() {
        sharedPreferences?.edit()?.clear()?.apply()
    }
}
