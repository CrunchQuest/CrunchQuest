package com.example.crunchquest.data.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class UserSkills(
    var uid: String? = "",
    var skill: String? = "",
    var skillExpertise: String? = "",
    var userUid: String? = ""
) {
    override fun toString(): String {
        return "Skill: $skill - $skillExpertise"
    }
}