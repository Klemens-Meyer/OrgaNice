package com.ccl3.project_helm_meyer.data.model.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.ccl3.project_helm_meyer.data.model.Assignment
import com.ccl3.project_helm_meyer.data.model.Exam
import com.ccl3.project_helm_meyer.data.model.Note
import com.ccl3.project_helm_meyer.data.model.Project

data class ProjectWithExams(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "projectName",
        entityColumn = "projectName"
    )
    val exams: List<Exam>
)
