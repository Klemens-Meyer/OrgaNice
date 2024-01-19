package com.ccl3.project_helm_meyer.ui

import com.ccl3.project_helm_meyer.data.model.Assignment
import com.ccl3.project_helm_meyer.data.model.Exam
import com.ccl3.project_helm_meyer.data.model.Note
import com.ccl3.project_helm_meyer.data.model.Project
import com.ccl3.project_helm_meyer.data.model.relations.ProjectWithAssignmentsAndNotesAndExams
import com.ccl3.project_helm_meyer.ui.view.Screen

data class MainViewState (
    val assignments: List<Assignment> = emptyList(),
    val projects: List<Project> = emptyList(),
    val notes: List<Note> = emptyList(),
    val exams: List<Exam> = emptyList(),
    val specificProject: List<ProjectWithAssignmentsAndNotesAndExams> = emptyList(),
    val projectsWithAssignmentsAndNotesAndExams: List<ProjectWithAssignmentsAndNotesAndExams> = emptyList(),
    val selectedScreen: Screen = Screen.First,
    val openDialog: Boolean = false
)

