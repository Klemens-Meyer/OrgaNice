package com.ccl3.project_helm_meyer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = false)
    val projectName: String,
)
