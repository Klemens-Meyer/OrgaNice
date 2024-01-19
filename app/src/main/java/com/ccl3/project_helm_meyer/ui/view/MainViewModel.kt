package com.ccl3.project_helm_meyer.ui.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccl3.project_helm_meyer.data.MainDao
import com.ccl3.project_helm_meyer.data.model.Assignment
import com.ccl3.project_helm_meyer.data.model.Exam
import com.ccl3.project_helm_meyer.data.model.Note
import com.ccl3.project_helm_meyer.data.model.Project
import com.ccl3.project_helm_meyer.ui.MainViewState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val dao: MainDao): ViewModel() {
    private val _assignmentState = MutableStateFlow(Assignment("","", "", 1))
    val assignmentState: StateFlow<Assignment> = _assignmentState.asStateFlow()

    private val _projectState = MutableStateFlow(Project(""))
    val projectState: StateFlow<Project> = _projectState.asStateFlow()

    private val _noteState = MutableStateFlow(Note("", "", ""))
    val noteState: StateFlow<Note> = _noteState.asStateFlow()

    private val _examState = MutableStateFlow(Exam("", "", ""))
    val examState: StateFlow<Exam> = _examState.asStateFlow()


    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()


    //functions, interactions for Assignment DB
    fun saveAssignment(assignment: Assignment){
        //Toast.makeText(this, "Test Test" , Toast.LENGTH_LONG).show()
        //val text = "test"
        //Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        viewModelScope.launch {
            dao.insertAssignment(assignment)
        }
    }

    fun getAssignments(){
        viewModelScope.launch {
            dao.getAssignments().collect(){ allAssignments ->
                //_mainViewState.update { it.copy(assignments = allAssignments) }
                _mainViewState.update { it.copy(assignments = allAssignments) }
            }
        }
    }

    fun editAssignment(assignment: Assignment){
        _assignmentState.value = assignment
        _mainViewState.update { it.copy(openDialog = true) }
    }

    fun updateAssignment(assignment: Assignment){
        viewModelScope.launch {
            dao.updateAssignment(assignment)
        }
        getAssignments()
        closeDialog()
    }

    fun closeDialog(){
        _mainViewState.update { it.copy(openDialog = false) }
    }

    fun deleteButton(assignment: Assignment){
        viewModelScope.launch {
            dao.deleteAssignment(assignment)
        }
        getAssignments()
    }

    fun selectScreen(screen: Screen){
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }

    //functions, interactions for Project DB
    fun saveProject(project: Project){
        //Toast.makeText(this, "Test Test" , Toast.LENGTH_LONG).show()
        //val text = "test"
        //Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        viewModelScope.launch {
            dao.insertProject(project)
        }
    }

    fun setSpecificProject(project: Project){
        _projectState.value = project
        Log.d("In Specy", _projectState.value.toString())

    }

    fun getSpecificProject(){
        viewModelScope.launch {
            Log.d("In Speci 2", _projectState.value.projectName)
            runCatching {
                //_projectState.value.projectName
                dao.getSpecificProject(_projectState.value.projectName).collect(){ allProjects ->
                    _mainViewState.update { it.copy(specificProject = allProjects) }
                    Log.d("allProjjo", allProjects.toString())

                }
            }.onFailure { exception ->
                Log.e("MyErrorrrr", "Error msg:", exception)
            }.onSuccess { result ->
                Log.d("NoError", "msg ${result} awdwd")
            }

        }
    }

    fun getProjects(){
        viewModelScope.launch {
            dao.getProjects().collect(){ allProjects ->
                //_mainViewState.update { it.copy(assignments = allAssignments) }
                _mainViewState.update { it.copy(projects = allProjects) }
            }
        }
    }
    fun getProjectsWithAssignmentsAndNotesAndExams(){
        viewModelScope.launch {
            dao.getProjectsWithAssignmentsAndNotesAndExams().collect(){ allProjectsWithAssignmentsAndNotesAndExams ->
                //_mainViewState.update { it.copy(assignments = allAssignments) }
                _mainViewState.update { it.copy(projectsWithAssignmentsAndNotesAndExams = allProjectsWithAssignmentsAndNotesAndExams) }
                Log.d("allProjjoTESTTTT", allProjectsWithAssignmentsAndNotesAndExams.toString())

            }
        }
    }




    //functions, interactions for Note DB
    fun saveNote(note: Note){
        //Toast.makeText(this, "Test Test" , Toast.LENGTH_LONG).show()
        //val text = "test"
        //Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        viewModelScope.launch {
            dao.insertNote(note)
        }
    }

    fun getNotes(){
        viewModelScope.launch {
            dao.getNotes().collect(){ allNotes ->
                //_mainViewState.update { it.copy(assignments = allAssignments) }
                _mainViewState.update { it.copy(notes = allNotes) }
            }
        }
    }



    //functions, interactions for Exam DB
    fun saveExam(exam: Exam){
        //Toast.makeText(this, "Test Test" , Toast.LENGTH_LONG).show()
        //val text = "test"
        //Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        viewModelScope.launch {
            dao.insertExam(exam)
        }
    }

    fun getExams(){
        viewModelScope.launch {
            dao.getExams().collect(){ allExams ->
                //_mainViewState.update { it.copy(assignments = allAssignments) }
                _mainViewState.update { it.copy(exams = allExams) }
            }
        }
    }

}