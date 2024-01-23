package com.ccl3.project_helm_meyer.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.ccl3.project_helm_meyer.data.model.Assignment
import com.ccl3.project_helm_meyer.data.model.Exam
import com.ccl3.project_helm_meyer.data.model.Note
import com.ccl3.project_helm_meyer.data.model.Project
import com.ccl3.project_helm_meyer.data.model.Username
import com.ccl3.project_helm_meyer.data.model.relations.ProjectWithAssignmentsAndNotesAndExams
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

@Dao
interface MainDao {

    @Insert
    suspend fun insertUsername(username: Username)
    @Insert
    suspend fun insertAssignment(assignment: Assignment)

    @Insert
    suspend fun insertProject(project: Project)

    @Insert
    suspend fun insertNote(note: Note)

    @Insert
    suspend fun insertExam(exam: Exam)

    @Update
    suspend fun updateAssignment(assignment: Assignment)

    @Update
    suspend fun updateProject(project: Project)

    @Update
    suspend fun updateNote(note: Note)

    @Update
    suspend fun updateExam(exam: Exam)

    @Delete
    suspend fun deleteAssignment(assignment: Assignment)

    @Delete
    suspend fun deleteProject(project: Project)

    @Delete
    suspend fun deleteNote(note: Note)

    @Delete
    suspend fun deleteExam(exam: Exam)



    @Query("SELECT * FROM assignments")
    fun getAssignments(): Flow<List<Assignment>>

    @Query("SELECT * FROM username")
    fun getUsernames(): Flow<List<Username>>

    @Query("SELECT * FROM projects")
    fun getProjects(): Flow<List<Project>>

    @Query("SELECT * FROM notes")
    fun getNotes(): Flow<List<Note>>

    @Query("SELECT * FROM exams")
    fun getExams(): Flow<List<Exam>>

    @Query("SELECT * FROM assignments WHERE id IS (:assID)")
    fun getOneAssignment(assID: Int): Flow<List<Assignment>>

    @Transaction
    @Query("SELECT * FROM projects WHERE projectName IS (:projectN)")
    fun getSpecificProject(projectN: String): Flow<List<ProjectWithAssignmentsAndNotesAndExams>>



    @Query("SELECT * FROM projects")
    fun getProjectsWithAssignmentsAndNotesAndExams(): Flow<List<ProjectWithAssignmentsAndNotesAndExams>>

}