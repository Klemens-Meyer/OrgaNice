package com.ccl3.project_helm_meyer.data.model
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exams")
data class Exam(
    val projectName: String,
    val examName: String,
    val examDate: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
