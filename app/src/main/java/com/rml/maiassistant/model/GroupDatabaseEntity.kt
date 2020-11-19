package com.rml.maiassistant.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroupDatabaseEntity(
    @PrimaryKey
    val id: Long,
    val name: String
)