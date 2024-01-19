package com.ccl3.project_helm_meyer.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assignments")
data class Assignment(
    val projectName: String,
    val assignmentName: String,
    val assignmentDesc: String,
    val daysLeft: Int,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
