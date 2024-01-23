package com.ccl3.project_helm_meyer.ui.view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ccl3.project_helm_meyer.data.MainDao
import com.ccl3.project_helm_meyer.data.model.Assignment
import com.ccl3.project_helm_meyer.data.model.Exam
import com.ccl3.project_helm_meyer.data.model.Note
import com.ccl3.project_helm_meyer.data.model.Project
import com.ccl3.project_helm_meyer.data.model.Username
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

    private val _usernameState = MutableStateFlow(Username("", 0))
    val usernameState: StateFlow<Username> = _usernameState.asStateFlow()


    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()



    fun saveUsername(username: Username){
        viewModelScope.launch {
            dao.insertUsername(username)
        }
        _usernameState.value = username
    }

    fun getUsernames(){
        viewModelScope.launch {
            dao.getUsernames().collect(){ usernames ->
                if (usernames.isNotEmpty()) {
                   _usernameState.value = usernames[0]
                } else {
                    //oder was anderes wenn halt leer ist
                 Log.d("Taggyyy", "The list is empty!")
                }
            }
        }
    }

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
    fun getOneAssignment(assID: Int){
        viewModelScope.launch {
            dao.getOneAssignment(assID).collect(){ allAssignments ->
                //_mainViewState.update { it.copy(assignments = allAssignments) }
                _mainViewState.update { it.copy(assignments = allAssignments) }
                _assignmentState.value = allAssignments[0]
            }
        }
    }

    fun editAssignment(assignment: Assignment){
        _assignmentState.value = assignment
        _mainViewState.update { it.copy(currentProject = assignment.projectName) }
        //_mainViewState.update { it.copy(openDialog = true) }
    }

    fun editNote(note: Note){
        _noteState.value = note
        _mainViewState.update { it.copy(currentProject = note.projectName) }
        //_mainViewState.update { it.copy(openDialog = true) }
    }

    fun editExam(exam: Exam){
        _examState.value = exam
        _mainViewState.update { it.copy(currentProject = exam.projectName) }
        //_mainViewState.update { it.copy(openDialog = true) }
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

    fun deleteButton(note: Note){
        viewModelScope.launch {
            dao.deleteNote(note)
        }
        getNotes()
    }

    fun deleteButton(exam: Exam){
        viewModelScope.launch {
            dao.deleteExam(exam)
        }
        getExams()
    }

    fun deleteButton(project: Project){
        viewModelScope.launch {
            dao.deleteProject(project)
        }
        getProjects()
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

    fun updateNote(note: Note){
        viewModelScope.launch {
            dao.updateNote(note)
        }
        getNotes()
        closeDialog()
    }

    fun updateExam(exam: Exam){
        viewModelScope.launch {
            dao.updateExam(exam)
        }
        getExams()
        closeDialog()
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

    fun setCurrentAdding(string: String){
        _mainViewState.update { it.copy(currentAdding = string) }
    }


    fun setCurrentEdit(string: String){
        _mainViewState.update { it.copy(currentEdit = string) }
    }
}