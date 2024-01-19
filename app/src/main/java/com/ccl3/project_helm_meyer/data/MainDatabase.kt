package com.ccl3.project_helm_meyer.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ccl3.project_helm_meyer.data.model.Assignment
import com.ccl3.project_helm_meyer.data.model.Exam
import com.ccl3.project_helm_meyer.data.model.Note
import com.ccl3.project_helm_meyer.data.model.Project

@Database(entities = [Assignment::class, Project::class, Note::class, Exam::class], version = 1)
abstract class MainDatabase: RoomDatabase() {
    abstract val dao: MainDao
}