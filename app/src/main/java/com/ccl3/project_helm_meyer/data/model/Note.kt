package com.ccl3.project_helm_meyer.data.model


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    val projectName: String,
    val noteName: String,
    val noteDesc: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
