package com.ccl3.project_helm_meyer.ui.view

import android.widget.Toast
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ccl3.project_helm_meyer.R
import com.ccl3.project_helm_meyer.data.model.Assignment
import com.ccl3.project_helm_meyer.data.model.Exam
import com.ccl3.project_helm_meyer.data.model.Note
import com.ccl3.project_helm_meyer.data.model.Project

sealed class Screen(val route: String){
    object First: Screen("first")
    object Second: Screen("second")
    object Third: Screen("third")
    object SpecificProject: Screen("specificProject")
}

@Composable
fun MainView(mainViewModel: MainViewModel){
    val state = mainViewModel.mainViewState.collectAsState()
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {BottomNavigationBar(navController, state.value.selectedScreen)}
    ) {
        NavHost(
            navController = navController,
            modifier = Modifier.padding(it),
            startDestination = Screen.First.route
        ){
            composable(Screen.First.route){
                mainViewModel.selectScreen(Screen.First)
                mainViewModel.getProjects()
                mainScreen(mainViewModel)
            }
            composable(Screen.Second.route){
                mainViewModel.selectScreen(Screen.Second)
                mainViewModel.getProjectsWithAssignmentsAndNotesAndExams()
                displayProjects(mainViewModel, navController)
            }
            composable(Screen.Third.route){
                mainViewModel.selectScreen(Screen.Third)
                mainViewModel.getAssignments()
                displayAssignments(mainViewModel)
            }
            composable(Screen.SpecificProject.route){
                mainViewModel.selectScreen(Screen.SpecificProject)
                mainViewModel.getSpecificProject()
                displaySpecificProject(mainViewModel)
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController, selectedScreen: Screen){
    BottomNavigation (backgroundColor = MaterialTheme.colorScheme.primary) {
        NavigationBarItem(
            selected = (selectedScreen == Screen.First),
            onClick = { navController.navigate(Screen.First.route) },
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "") })

        NavigationBarItem(
            selected = (selectedScreen == Screen.Second),
            onClick = { navController.navigate(Screen.Second.route) },
            icon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = "") })


        NavigationBarItem(
            selected = (selectedScreen == Screen.Third),
            onClick = { navController.navigate(Screen.Third.route) },
            icon = { Icon(imageVector = Icons.Default.Call, contentDescription = "") })

    }
}

@Composable
fun mainScreen(mainViewModel: MainViewModel){
    var projectName by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    var assignmentName by remember { mutableStateOf(TextFieldValue("")) }
    var assignmentDesc by remember { mutableStateOf(TextFieldValue("")) }
    var daysLeft by remember { mutableStateOf(TextFieldValue("")) }
    var noteName by remember { mutableStateOf(TextFieldValue("")) }
    var noteDesc by remember { mutableStateOf(TextFieldValue("")) }
    var examName by remember { mutableStateOf(TextFieldValue("")) }
    var examDate by remember { mutableStateOf(TextFieldValue("")) }

    val state = mainViewModel.mainViewState.collectAsState()
    val context = LocalContext.current
    Log.d("In IFFFF", state.value.projects.toString())
    var checkii: Boolean = false


    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hallo Finn", fontSize = 30.sp,  style = TextStyle(fontFamily = FontFamily.Cursive))
        Text(text = "Finn Lover 123", fontSize = 16.sp,  style = TextStyle(fontFamily = FontFamily.Cursive))

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Box"
        )
        Spacer(modifier = Modifier.height(40.dp))

        TextField(
            value = projectName,
            onValueChange = {
                    newText -> projectName = newText
            },
            label = { Text(text = "Projc Name" ) }
        )

        TextField(
            modifier = Modifier.padding(top = 12.dp),
            value = assignmentName,
            onValueChange = {
                    newText -> assignmentName = newText
            },
            label = { Text(text = "ass Name" ) }
        )


        TextField(
            modifier = Modifier.padding(top = 12.dp),
            value = assignmentDesc,
            onValueChange = {
                    newText -> assignmentDesc = newText
            },
            label = { Text(text = "Assignment Description") }
        )

        TextField(
            modifier = Modifier.padding(top = 12.dp),
            value = daysLeft,
            onValueChange = {
                    newText -> daysLeft = newText
            },
            label = { Text(text = "Days left to Deadline") }
        )

        TextField(
            modifier = Modifier.padding(top = 12.dp),
            value = noteName,
            onValueChange = {
                    newText -> noteName = newText
            },
            label = { Text(text = "NoteName") }
        )
        TextField(
            modifier = Modifier.padding(top = 12.dp),
            value = noteDesc,
            onValueChange = {
                    newText -> noteDesc = newText
            },
            label = { Text(text = "NoteDesc") }
        )
        TextField(
            modifier = Modifier.padding(top = 12.dp),
            value = examName,
            onValueChange = {
                    newText -> examName = newText
            },
            label = { Text(text = "ExamName") }
        )
        TextField(
            modifier = Modifier.padding(top = 12.dp),
            value = examDate,
            onValueChange = {
                    newText -> examDate = newText
            },
            label = { Text(text = "ExamDate") }
        )

        Button(
            onClick = {
                if(projectName.text.isNullOrEmpty()|| assignmentDesc.text.isNullOrEmpty() || daysLeft.text.isNullOrEmpty())
                {
                    Toast.makeText(context,"Fill out everything", Toast.LENGTH_LONG).show()

                }else{
                    if(daysLeft.text.toIntOrNull()!=null){




                        for(projectInTable in state.value.projects){
                            if(projectInTable.projectName == projectName.text){
                                checkii = true
                                break
                            }else{
                                checkii = false
                            }
                        }
                        if(!checkii){
                            mainViewModel.saveProject(Project(projectName.text))
                        }

                        //Log.d("In IFFFF", state.value.projects.toString())

                        mainViewModel.saveAssignment(Assignment(projectName.text, assignmentName.text, assignmentDesc.text, daysLeft.text.toInt()))

                        mainViewModel.saveNote(Note(projectName.text, noteName.text, noteDesc.text))
                        mainViewModel.saveExam(Exam(projectName.text, examName.text, examDate.text))

                        Toast.makeText(context,"Saved!", Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(context,"make sure Days-Left are numeric", Toast.LENGTH_LONG).show()

                    }

                }
            },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(text = "Save", fontSize = 20.sp)
        }
    }

}


@Composable
fun displayAssignments(mainViewModel: MainViewModel){
    val state = mainViewModel.mainViewState.collectAsState()

    LazyColumn (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item{
            Spacer(modifier = Modifier.height(60.dp))

            Text(text = "Current Assignments:",fontSize = 40.sp, fontWeight = FontWeight.Bold, style = TextStyle(fontFamily = FontFamily.Cursive))
        }

        items(state.value.assignments){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .clickable { mainViewModel.editAssignment(it) }
            ){
                Column (modifier = Modifier.weight(1f)) {
                    Text(text = "${it.projectName}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Name: ${it.assignmentName}")
                    Text(text = "Description: ${it.assignmentDesc}")
                    Text(text = "Days Left: ${it.daysLeft}")
                }
                IconButton(
                    onClick = { mainViewModel.deleteButton(it)}
                    //onClick = { confirmDelete() }
                ) {
                    Icon(Icons.Default.Delete,"Delete")
                }
            }
        }
    }
    //Column {
        //editAssignmentModal(mainViewModel)
    //}
}

@Composable
fun displayProjects(mainViewModel: MainViewModel, navController: NavHostController){
    val state = mainViewModel.mainViewState.collectAsState()
    //val state1 = mainViewModel.mainViewState.collectAsState()
    Log.d("Hier stehts", state.value.projectsWithAssignmentsAndNotesAndExams.toString())


    LazyColumn (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item{
            Spacer(modifier = Modifier.height(60.dp))

            Text(text = "Your Projects:",fontSize = 40.sp, fontWeight = FontWeight.Bold, style = TextStyle(fontFamily = FontFamily.Cursive))
        }

        items(state.value.projectsWithAssignmentsAndNotesAndExams){
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .clickable {
                    Log.d("In Clicky", it.project.toString())
                    mainViewModel.setSpecificProject(it.project)
                    navController.navigate(Screen.SpecificProject.route)

                }
            ){
                Column (modifier = Modifier.weight(1f)) {
                    Text(text = "${it.project.projectName}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    //Text(text = "Name: ${it.assignments[0].assignmentName}")
                    //Text(text = "Name: ${it.assignments[1].assignmentName}")
                    Text(text = "AssLength: ${it.assignments.size}")
                    Text(text = "AssLength: ${it.notes.size}")
                    Text(text = "AssLength: ${it.exams.size}")

                }
                /*IconButton(
                    onClick = { mainViewModel.deleteButton(it)}
                    //onClick = { confirmDelete() }
                ) {
                    Icon(Icons.Default.Delete,"Delete")
                }*/
            }
        }
    }

}


@Composable
fun displaySpecificProject(mainViewModel: MainViewModel){
    val state = mainViewModel.mainViewState.collectAsState()

    Log.d("Hierrrrrrrrrrrrrrrrr", state.value.specificProject.toString())
    Log.d("Hierrrrrrr", state.value.specificProject.size.toString())
    Log.d("Hierrr", state.value.specificProject.getOrNull(0).toString())




    LazyColumn (
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item{
            Spacer(modifier = Modifier.height(60.dp))

            Text(text = "Project: ${state.value.specificProject.getOrNull(0)?.project?.projectName}",fontSize = 40.sp, fontWeight = FontWeight.Bold, style = TextStyle(fontFamily = FontFamily.Cursive))
        }

        state.value.specificProject.getOrNull(0)?.let {
            items(it.assignments){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    //.clickable { mainViewModel.editAssignment(it) }
                ){
                    Column (modifier = Modifier.weight(1f)) {
                        Text(text = "${it.projectName}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        //Text(text = "Name: ${it.assignments[0].assignmentName}")
                        //Text(text = "Name: ${it.assignments[1].assignmentName}")
                        Text(text = "Ass: ${it.assignmentName}")
                        //Text(text = "Note: ${it.notes[0].noteName}")
                        //Text(text = "Ex: ${it.exams[0].examName}")

                    }
                    /*IconButton(
                            onClick = { mainViewModel.deleteButton(it)}
                            //onClick = { confirmDelete() }
                        ) {
                            Icon(Icons.Default.Delete,"Delete")
                        }*/
                }
            }
        }

        state.value.specificProject.getOrNull(0)?.let {
            items(it.notes){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    //.clickable { mainViewModel.editAssignment(it) }
                ){
                    Column (modifier = Modifier.weight(1f)) {
                        Text(text = "${it.projectName}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        //Text(text = "Name: ${it.assignments[0].assignmentName}")
                        //Text(text = "Name: ${it.assignments[1].assignmentName}")
                        Text(text = "Ass: ${it.noteName}")
                        //Text(text = "Note: ${it.notes[0].noteName}")
                        //Text(text = "Ex: ${it.exams[0].examName}")

                    }
                    /*IconButton(
                            onClick = { mainViewModel.deleteButton(it)}
                            //onClick = { confirmDelete() }
                        ) {
                            Icon(Icons.Default.Delete,"Delete")
                        }*/
                }
            }
        }
        state.value.specificProject.getOrNull(0)?.let {
            items(it.exams){
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    //.clickable { mainViewModel.editAssignment(it) }
                ){
                    Column (modifier = Modifier.weight(1f)) {
                        Text(text = "${it.projectName}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        //Text(text = "Name: ${it.assignments[0].assignmentName}")
                        //Text(text = "Name: ${it.assignments[1].assignmentName}")
                        Text(text = "Ass: ${it.examName}")
                        //Text(text = "Note: ${it.notes[0].noteName}")
                        //Text(text = "Ex: ${it.exams[0].examName}")

                    }
                    /*IconButton(
                            onClick = { mainViewModel.deleteButton(it)}
                            //onClick = { confirmDelete() }
                        ) {
                            Icon(Icons.Default.Delete,"Delete")
                        }*/
                }
            }
        }
    }

}
