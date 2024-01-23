package com.ccl3.project_helm_meyer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "username")
data class Username(
    val userName: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
