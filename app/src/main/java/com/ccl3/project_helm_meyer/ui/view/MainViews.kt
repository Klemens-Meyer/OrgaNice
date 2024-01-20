package com.ccl3.project_helm_meyer.ui.view

import android.widget.Toast
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Card
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    object AddingPage: Screen("addingPage")
    object EditPage: Screen("editPage")
}

@Composable
fun MainView(mainViewModel: MainViewModel){
    val state = mainViewModel.mainViewState.collectAsState()
    val assState = mainViewModel.assignmentState.collectAsState()
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
                displayAssignments(mainViewModel, navController)
            }
            composable(Screen.SpecificProject.route){
                mainViewModel.selectScreen(Screen.SpecificProject)
                mainViewModel.getSpecificProject()
                displaySpecificProject(mainViewModel, navController)
            }
            composable(Screen.AddingPage.route){
                mainViewModel.selectScreen(Screen.AddingPage)
                //mainViewModel.getSpecificProject()
                mainViewModel.getProjects()
                displayAddingPage(mainViewModel, navController)
            }
            composable(Screen.EditPage.route){
                mainViewModel.selectScreen(Screen.EditPage)
                //mainViewModel.getSpecificProject()
                //if(entweder assignment, note, exam) bla bla maybe
                mainViewModel.getOneAssignment(assState.value.id)

                displayEditPage(mainViewModel, navController)
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
fun displayAssignments(mainViewModel: MainViewModel, navController: NavHostController){
    val state = mainViewModel.mainViewState.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    mainViewModel.setCurrentAdding("Assignment")
                    navController.navigate(Screen.AddingPage.route)
                },
                backgroundColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Default.Add,"Add Assignment")
            }
        },
        content = { paddingValues ->
            LazyColumn (
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                item {
                    Spacer(modifier = Modifier.height(60.dp))
                    Text(
                        text = "Current Assignments:",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(fontFamily = FontFamily.Cursive)
                    )
                }

                items(state.value.assignments) { assignment ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                mainViewModel.setCurrentEdit("Assignment")
                                mainViewModel.editAssignment(assignment)
                                navController.navigate(Screen.EditPage.route)
                            },
                        elevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = "Assignment",
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = assignment.assignmentName,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Text(
                                    text = assignment.projectName,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Text(
                                text = "${assignment.daysLeft} days left",
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.align(Alignment.CenterVertically)
                            )
                            IconButton(
                                onClick = { mainViewModel.deleteButton(assignment) }
                            ) {
                                Icon(Icons.Default.Delete, "Delete")
                            }
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun displayProjects(mainViewModel: MainViewModel, navController: NavHostController){
    val state = mainViewModel.mainViewState.collectAsState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text("Add Project") },
                onClick = {
                    // Define the action for adding a new project
                },
                icon = {
                    Icon(Icons.Default.Add, contentDescription = "Add Project")
                },
            )
        },
        content = { paddingValues ->
            LazyColumn (
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                item {
                    Spacer(modifier = Modifier.height(60.dp))
                    Text(
                        text = "Your Projects:",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        style = TextStyle(fontFamily = FontFamily.Cursive)
                    )
                    // Add your filters UI here
                }

                items(state.value.projectsWithAssignmentsAndNotesAndExams) { projectInfo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable {
                                mainViewModel.setSpecificProject(projectInfo.project)
                                navController.navigate(Screen.SpecificProject.route)
                            },
                        elevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Project",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = projectInfo.project.projectName,
                                    style = MaterialTheme.typography.headlineSmall
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                //Text(
                                //    text = projectInfo.project.daysLeft.toString(),
                                //    style = MaterialTheme.typography.bodySmall
                                //)
                                Row {
                                    Icon(Icons.Default.Call, contentDescription = "Assignments")
                                    Text(text = "${projectInfo.assignments.size}")
                                    Icon(Icons.Default.Build, contentDescription = "Notes")
                                    Text(text = "${projectInfo.notes.size}")
                                    Icon(Icons.Default.AccountBox, contentDescription = "Exams")
                                    Text(text = "${projectInfo.exams.size}")
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}



@Composable
fun displaySpecificProject(mainViewModel: MainViewModel, navController: NavHostController) {
    val state = mainViewModel.mainViewState.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Project: ${state.value.specificProject.getOrNull(0)?.project?.projectName}",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(fontFamily = FontFamily.Cursive)
            )
        }

        //Assignments
        state.value.specificProject.getOrNull(0)?.assignments?.let { assignments ->
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Assignments",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = {
                        mainViewModel.setCurrentAdding("Assignment")
                        navController.navigate(Screen.AddingPage.route)
                    }) {
                        Text("Add Assignment")
                    }
                }
            }
            items(assignments) { assignment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .border(1.dp, Color.Gray)
                        .clickable {
                            mainViewModel.setCurrentEdit("Assignment")
                            mainViewModel.editAssignment(assignment)
                            navController.navigate(Screen.EditPage.route)
                        },
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "${assignment.assignmentName}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        Text(text = "Due in: ${assignment.daysLeft} Days")
                    }
                }
            }
        }

        //Notes
        state.value.specificProject.getOrNull(0)?.notes?.let { notes ->
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Notes",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = { /* TODO: Add action */ }) {
                        Text("Add Note")
                    }
                }
            }
            items(notes) { note ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .border(1.dp, Color.Gray)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Note: ${note.noteName}")
                        }
                    }
            }
        }

        //Exams
        state.value.specificProject.getOrNull(0)?.exams?.let { exams ->
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Exams",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Button(onClick = { /* TODO: Add action */ }) {
                        Text("Add Exam")
                    }
                }
            }
            items(exams) { exam ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .border(1.dp, Color.Gray)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Exam: ${exam.examName}")
                        }
                    }
            }
        }
    }
}


@Composable
fun displayAddingPage(mainViewModel: MainViewModel, navController: NavHostController){
    val state = mainViewModel.mainViewState.collectAsState()
    var projectName by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    var assignmentName by remember { mutableStateOf(TextFieldValue("")) }
    var assignmentDesc by remember { mutableStateOf(TextFieldValue("")) }
    var daysLeft by remember { mutableStateOf(TextFieldValue("")) }
    var noteName by remember { mutableStateOf(TextFieldValue("")) }
    var noteDesc by remember { mutableStateOf(TextFieldValue("")) }
    var examName by remember { mutableStateOf(TextFieldValue("")) }
    var examDate by remember { mutableStateOf(TextFieldValue("")) }



    val context = LocalContext.current
    //Log.d("In IFFFF", state.value.projects.toString())
    var checkii: Boolean = false


    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        IconButton(onClick = { navController.navigateUp() }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
        }

        Text(text = "Create your ${state.value.currentAdding}", fontSize = 36.sp,  style = TextStyle(fontFamily = FontFamily.Cursive))

        Spacer(modifier = Modifier.height(40.dp))

        TextField(
            value = projectName,
            onValueChange = { newText ->
                projectName = newText
            },
            label = { Text(text = "Projc Name") }
        )

        if(state.value.currentAdding == "Assignment") {

            TextField(
                modifier = Modifier.padding(top = 12.dp),
                value = assignmentName,
                onValueChange = { newText ->
                    assignmentName = newText
                },
                label = { Text(text = "ass Name") }
            )


            TextField(
                modifier = Modifier.padding(top = 12.dp),
                value = assignmentDesc,
                onValueChange = { newText ->
                    assignmentDesc = newText
                },
                label = { Text(text = "Assignment Description") }
            )

            TextField(
                modifier = Modifier.padding(top = 12.dp),
                value = daysLeft,
                onValueChange = { newText ->
                    daysLeft = newText
                },
                label = { Text(text = "Days left to Deadline") }
            )
        }else if(state.value.currentAdding == "Note"){
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
        }else if(state.value.currentAdding == "Exam") {

            TextField(
                modifier = Modifier.padding(top = 12.dp),
                value = examName,
                onValueChange = { newText ->
                    examName = newText
                },
                label = { Text(text = "ExamName") }
            )
            TextField(
                modifier = Modifier.padding(top = 12.dp),
                value = examDate,
                onValueChange = { newText ->
                    examDate = newText
                },
                label = { Text(text = "ExamDate") }
            )
        }

        Button(
            onClick = {
                if(state.value.currentAdding == "Assignment"){
                    if(assignmentName.text.isNullOrEmpty() || assignmentDesc.text.isNullOrEmpty() || daysLeft.text.isNullOrEmpty()){
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

                            mainViewModel.saveAssignment(Assignment(projectName.text, assignmentName.text, assignmentDesc.text, daysLeft.text.toInt()))

                            Toast.makeText(context,"Saved!", Toast.LENGTH_LONG).show()

                        }else{
                            Toast.makeText(context,"make sure Days-Left are numeric", Toast.LENGTH_LONG).show()
                        }
                    }
                }else if(state.value.currentAdding == "Note"){
                    if(noteName.text.isNullOrEmpty() || noteDesc.text.isNullOrEmpty()){
                        Toast.makeText(context,"Fill out everything", Toast.LENGTH_LONG).show()

                    }else{
                        mainViewModel.saveNote(Note(projectName.text, noteName.text, noteDesc.text))
                        Toast.makeText(context,"Saved!", Toast.LENGTH_LONG).show()
                    }
                }else if(state.value.currentAdding == "Exam"){
                    if(examName.text.isNullOrEmpty() || examDate.text.isNullOrEmpty()){
                        Toast.makeText(context,"Fill out everything", Toast.LENGTH_LONG).show()

                    }else{
                        mainViewModel.saveExam(Exam(projectName.text, examName.text, examDate.text))
                        Toast.makeText(context,"Saved!", Toast.LENGTH_LONG).show()
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
fun displayEditPage(mainViewModel: MainViewModel, navController: NavHostController){
    var projectName by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }
    var assignmentName by remember { mutableStateOf(TextFieldValue("")) }
    var assignmentDesc by remember { mutableStateOf(TextFieldValue("")) }
    var daysLeft by remember { mutableStateOf(TextFieldValue("")) }
    var noteName by remember { mutableStateOf(TextFieldValue("")) }
    var noteDesc by remember { mutableStateOf(TextFieldValue("")) }
    var examName by remember { mutableStateOf(TextFieldValue("")) }
    var examDate by remember { mutableStateOf(TextFieldValue("")) }

    val state = mainViewModel.mainViewState.collectAsState()
    val assState = mainViewModel.assignmentState.collectAsState()
    val context = LocalContext.current
    Log.d("In IFFFF", state.value.projects.toString())
    var checkii: Boolean = false

    var placeholderTest = ""

    /* if(state.value.assignments.getOrNull(0)?.assignmentName.isNullOrEmpty()){
        //bla
    }else{
        //assignmentName = TextFieldValue(state.value.assignments.get(0).assignmentName)
        LaunchedEffect(Unit) {
            //assignmentName = TextFieldValue(state.value.assignments.get(0).assignmentName)
            assignmentName = TextFieldValue(assState.value.assignmentName)

        }
    } */

    LaunchedEffect(Unit) {
        //assignmentName = TextFieldValue(state.value.assignments.get(0).assignmentName)
        assignmentName = TextFieldValue(assState.value.assignmentName)

    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        IconButton(onClick = { navController.navigateUp() }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
        }

        Text(text = "Edit your ${state.value.currentEdit}", fontSize = 36.sp,  style = TextStyle(fontFamily = FontFamily.Cursive))

        Spacer(modifier = Modifier.height(40.dp))

        TextField(
            value = projectName,
            onValueChange = { newText ->
                projectName = newText
            },
            label = { Text(text = "Projc Name") }
        )

        if(state.value.currentEdit == "Assignment") {

            TextField(
                modifier = Modifier.padding(top = 12.dp),
                value = assignmentName,
                onValueChange = { newText ->
                    assignmentName = newText
                },
                label = { Text(text = "ass Name") },

            )


            TextField(
                modifier = Modifier.padding(top = 12.dp),
                value = assignmentDesc,
                onValueChange = { newText ->
                    assignmentDesc = newText
                },
                label = { Text(text = "Assignment Description") }
            )

            TextField(
                modifier = Modifier.padding(top = 12.dp),
                value = daysLeft,
                onValueChange = { newText ->
                    daysLeft = newText
                },
                label = { Text(text = "Days left to Deadline") }
            )
        }else if(state.value.currentEdit == "Note"){
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
        }else if(state.value.currentEdit == "Exam") {

            TextField(
                modifier = Modifier.padding(top = 12.dp),
                value = examName,
                onValueChange = { newText ->
                    examName = newText
                },
                label = { Text(text = "ExamName") }
            )
            TextField(
                modifier = Modifier.padding(top = 12.dp),
                value = examDate,
                onValueChange = { newText ->
                    examDate = newText
                },
                label = { Text(text = "ExamDate") }
            )
        }

        Button(
            onClick = {
                if(state.value.currentEdit == "Assignment"){
                    if(assignmentName.text.isNullOrEmpty() || assignmentDesc.text.isNullOrEmpty() || daysLeft.text.isNullOrEmpty()){
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

                            mainViewModel.saveAssignment(Assignment(projectName.text, assignmentName.text, assignmentDesc.text, daysLeft.text.toInt()))

                            Toast.makeText(context,"Saved!", Toast.LENGTH_LONG).show()

                        }else{
                            Toast.makeText(context,"Make sure Days-Left are numeric", Toast.LENGTH_LONG).show()
                        }
                    }
                }else if(state.value.currentEdit == "Note"){
                    if(noteName.text.isNullOrEmpty() || noteDesc.text.isNullOrEmpty()){
                        Toast.makeText(context,"Fill out everything", Toast.LENGTH_LONG).show()

                    }else{
                        mainViewModel.saveNote(Note(projectName.text, noteName.text, noteDesc.text))
                        Toast.makeText(context,"Saved!", Toast.LENGTH_LONG).show()
                    }
                }else if(state.value.currentEdit == "Exam"){
                    if(examName.text.isNullOrEmpty() || examDate.text.isNullOrEmpty()){
                        Toast.makeText(context,"Fill out everything", Toast.LENGTH_LONG).show()

                    }else{
                        mainViewModel.saveExam(Exam(projectName.text, examName.text, examDate.text))
                        Toast.makeText(context,"Saved!", Toast.LENGTH_LONG).show()
                    }
                }

            },
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text(text = "Save", fontSize = 20.sp)
        }
    }
}
