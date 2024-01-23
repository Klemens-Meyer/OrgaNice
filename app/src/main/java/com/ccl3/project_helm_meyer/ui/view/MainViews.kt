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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Card
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ccl3.project_helm_meyer.R
import com.ccl3.project_helm_meyer.data.model.Assignment
import com.ccl3.project_helm_meyer.data.model.Exam
import com.ccl3.project_helm_meyer.data.model.Note
import com.ccl3.project_helm_meyer.data.model.Project
import com.ccl3.project_helm_meyer.data.model.Username

sealed class Screen(val route: String){
    object First: Screen("first")
    object Second: Screen("second")
    object Third: Screen("third")
    object SpecificProject: Screen("specificProject")
    object AddingPage: Screen("addingPage")
    object EditPage: Screen("editPage")
    object Main: Screen("mainPage")
}

@Composable
fun MainView(mainViewModel: MainViewModel, firstStartScreen: Boolean){
    mainViewModel.getUsernames()
    val state = mainViewModel.mainViewState.collectAsState()
    val assState = mainViewModel.assignmentState.collectAsState()
    val userState = mainViewModel.usernameState.collectAsState()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    var firstScreen = ""

    if(firstStartScreen){
        firstScreen = Screen.First.route
    }else if(!firstStartScreen && userState.value.userName.isEmpty()) {
        firstScreen = Screen.First.route
    }else{
        firstScreen = Screen.Main.route
    }
    Scaffold(
        bottomBar = {
            if(navBackStackEntry?.destination?.route != "first"){
                BottomNavigationBar(navController, state.value.selectedScreen)
            }
        }

    ) {
        NavHost(
            navController = navController,
            modifier = Modifier.padding(it),
            startDestination = firstScreen
        ){
            composable(Screen.First.route){
                mainViewModel.selectScreen(Screen.First)
                //mainViewModel.getProjects()
                firstScreen(mainViewModel, navController)
            }
            composable(Screen.Main.route){
                mainViewModel.selectScreen(Screen.Main)
                mainViewModel.getUsernames()
                mainScreen(mainViewModel, navController)
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
                //mainViewModel.getOneAssignment(assState.value.id)

                displayEditPage(mainViewModel, navController)
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController, selectedScreen: Screen){
    BottomNavigation (backgroundColor = MaterialTheme.colorScheme.secondary) {
        NavigationBarItem(
            selected = (selectedScreen == Screen.Main),
            onClick = { navController.navigate(Screen.Main.route) },
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary) })

        NavigationBarItem(
            selected = (selectedScreen == Screen.Second),
            onClick = { navController.navigate(Screen.Second.route) },
            icon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary) })


        NavigationBarItem(
            selected = (selectedScreen == Screen.Third),
            onClick = { navController.navigate(Screen.Third.route) },
            icon = { Icon(imageVector = Icons.Default.Call, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary) })

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun firstScreen(mainViewModel: MainViewModel, navController: NavHostController){
    //var projectName by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }


    var userName by remember { mutableStateOf(TextFieldValue("")) }

    val state = mainViewModel.mainViewState.collectAsState()
    val context = LocalContext.current
    //Log.d("In IFFFF", state.value.projects.toString())
    //var checkii: Boolean = false


    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Text(
                text = "Orga",
                fontSize = 32.sp,
                style = TextStyle(fontFamily = FontFamily.Default, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Nice",
                fontSize = 32.sp,
                style = TextStyle(fontFamily = FontFamily.Default, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
        Card(
            modifier = Modifier
                .size(width = 360.dp, height = 360.dp),
            elevation = 4.dp,
            backgroundColor = MaterialTheme.colorScheme.secondary,
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Before we get started",
                    fontSize = 14.sp,
                    style = TextStyle(fontFamily = FontFamily.Default),
                    color = MaterialTheme.colorScheme.onSecondary
                )
                Row {
                    Text(
                        text = "Input your ",
                        fontSize = 24.sp,
                        style = TextStyle(fontFamily = FontFamily.Default),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "Name",
                        fontSize = 24.sp,
                        style = TextStyle(fontFamily = FontFamily.Default),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = userName,
                    onValueChange = { newText ->
                        userName = newText
                    },
                    label = { Text(text = "User Name") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .wrapContentSize()
                )



                Button(
                    onClick = {
                        if (userName.text.isNotEmpty()) {
                            mainViewModel.saveUsername(Username(userName.text))
                            navController.navigate(Screen.Main.route)
                        }

                    },
                    shape = RoundedCornerShape(16.dp),

                    modifier = Modifier
                        .padding(top = 32.dp)
                        .wrapContentSize()
                ) {
                    Text(
                        text = "Get Started",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }

}


@Composable
fun mainScreen(mainViewModel: MainViewModel, navController: NavHostController){
    //var projectName by rememberSaveable(stateSaver = TextFieldValue.Saver) { mutableStateOf(TextFieldValue("")) }


    //var userName by remember { mutableStateOf(TextFieldValue("")) }

    val state = mainViewModel.mainViewState.collectAsState()
    val userState = mainViewModel.usernameState.collectAsState()
    val context = LocalContext.current
    //Log.d("In IFFFF", state.value.projects.toString())
    //var checkii: Boolean = false


    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hello, " + userState.value.userName + "!", fontSize = 30.sp,  style = TextStyle(fontFamily = FontFamily.Cursive))
        Text(text = "Have a nice day!", fontSize = 16.sp,  style = TextStyle(fontFamily = FontFamily.Cursive))

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Box"
        )
    }

}


@Composable
fun displayAssignments(mainViewModel: MainViewModel, navController: NavHostController){
    val state = mainViewModel.mainViewState.collectAsState()
    var sortedListOfAssignment = state.value.assignments.sortedBy {
        it.daysLeft
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    mainViewModel.setCurrentAdding("AssignmentWithProject")
                    navController.navigate(Screen.AddingPage.route)
                },
                backgroundColor = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape( 16.dp),
            ) {
                Icon(Icons.Default.Add,"Add Assignment", tint = MaterialTheme.colorScheme.onPrimary)
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

                items(sortedListOfAssignment) { assignment ->
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
                FloatingActionButton(
                    onClick = {
                        mainViewModel.setCurrentAdding("Project")
                        navController.navigate(Screen.AddingPage.route)
                    },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape( 16.dp),

                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Project", tint = MaterialTheme.colorScheme.onPrimary)
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
                            IconButton(
                                onClick = {
                                            mainViewModel.deleteButton(projectInfo.project)
                                            for(assi in projectInfo.assignments){
                                                mainViewModel.deleteButton(assi)
                                            }
                                            for(noti in projectInfo.notes){
                                                mainViewModel.deleteButton(noti)
                                            }
                                            for(exi in projectInfo.exams){
                                                mainViewModel.deleteButton(exi)
                                            }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete, "Delete",
                                    tint = MaterialTheme.colorScheme.onError
                                )
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
    //var sortedListOfExam= state.value.assignments.sortedBy {
    //    it.daysLeft
    //}
    LazyColumn(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Spacer(modifier = Modifier.height(60.dp))
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
            }
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
                    IconButton(
                        onClick = { mainViewModel.deleteButton(assignment) }
                    ) {
                        Icon(Icons.Default.Delete, "Delete")
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
                    Button(onClick = {
                        mainViewModel.setCurrentAdding("Note")
                        navController.navigate(Screen.AddingPage.route) }) {
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
                            .clickable {
                                mainViewModel.setCurrentEdit("Note")
                                mainViewModel.editNote(note)
                                navController.navigate(Screen.EditPage.route)
                            },
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Note: ${note.noteName}")
                        }
                        IconButton(
                            onClick = { mainViewModel.deleteButton(note) }
                        ) {
                            Icon(Icons.Default.Delete, "Delete")
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
                    Button(onClick = {
                        mainViewModel.setCurrentAdding("Exam")
                        navController.navigate(Screen.AddingPage.route) }) {
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
                            .clickable {
                                mainViewModel.setCurrentEdit("Exam")
                                mainViewModel.editExam(exam)
                                navController.navigate(Screen.EditPage.route)
                            },
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = "Exam: ${exam.examName}")
                        }
                        IconButton(
                            onClick = { mainViewModel.deleteButton(exam) }
                        ) {
                            Icon(Icons.Default.Delete, "Delete")
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
    val projectState = mainViewModel.projectState.collectAsState()

    val currentProject = projectState.value.projectName

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
        if(state.value.currentAdding == "Project" || state.value.currentAdding == "AssignmentWithProject"){
            TextField(
                value = projectName,
                onValueChange = { newText ->
                    projectName = newText
                },
                label = { Text(text = "Project Name") }
            )
        }


        if(state.value.currentAdding == "Assignment" || state.value.currentAdding == "AssignmentWithProject") {

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
                if(state.value.currentAdding == "Assignment" || state.value.currentAdding == "AssignmentWithProject"){ //!!!!!!!!!!!!LEER Project
                    if(assignmentName.text.isNullOrEmpty() || assignmentDesc.text.isNullOrEmpty() || daysLeft.text.isNullOrEmpty()){
                        Toast.makeText(context,"Fill out everything", Toast.LENGTH_LONG).show()
                    }else{
                        if(daysLeft.text.toIntOrNull()!=null){
                            if(state.value.currentAdding == "AssignmentWithProject"){
                                if(projectName.text.isNullOrEmpty()){
                                    Toast.makeText(context,"Fill out everything", Toast.LENGTH_LONG).show()

                                }else {
                                    for (projectInTable in state.value.projects) {
                                        if (projectInTable.projectName == projectName.text) {
                                            checkii = true
                                            break
                                        } else {
                                            checkii = false
                                        }
                                    }
                                    if (!checkii) {
                                        mainViewModel.saveProject(Project(projectName.text))
                                    }
                                }
                            }



                            if(state.value.currentAdding == "Assignment"){
                                mainViewModel.saveAssignment(Assignment(currentProject, assignmentName.text, assignmentDesc.text, daysLeft.text.toInt()))
                            }else if(state.value.currentAdding == "AssignmentWithProject") {
                                mainViewModel.saveAssignment(Assignment(projectName.text, assignmentName.text, assignmentDesc.text, daysLeft.text.toInt()))

                            }

                            Toast.makeText(context,"Saved!", Toast.LENGTH_LONG).show()

                        }else{
                            Toast.makeText(context,"make sure Days-Left are numeric", Toast.LENGTH_LONG).show()
                        }
                    }
                }else if(state.value.currentAdding == "Note"){
                    if(noteName.text.isNullOrEmpty() || noteDesc.text.isNullOrEmpty()){
                        Toast.makeText(context,"Fill out everything", Toast.LENGTH_LONG).show()

                    }else{
                        mainViewModel.saveNote(Note(currentProject, noteName.text, noteDesc.text))
                        Toast.makeText(context,"Saved!", Toast.LENGTH_LONG).show()
                    }
                }else if(state.value.currentAdding == "Exam"){
                    if(examName.text.isNullOrEmpty() || examDate.text.isNullOrEmpty()){
                        Toast.makeText(context,"Fill out everything", Toast.LENGTH_LONG).show()

                    }else{
                        mainViewModel.saveExam(Exam(currentProject, examName.text, examDate.text))
                        Toast.makeText(context,"Saved!", Toast.LENGTH_LONG).show()
                    }
                }else if(state.value.currentAdding == "Project"){
                    if(projectName.text.isNullOrEmpty()){
                        Toast.makeText(context,"Fill out everything", Toast.LENGTH_LONG).show()

                    }else{
                        mainViewModel.saveProject(Project(projectName.text))
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
    val noteState = mainViewModel.noteState.collectAsState()
    val examState = mainViewModel.examState.collectAsState()
    val currentProject = state.value.currentProject

    val context = LocalContext.current
    var checkii: Boolean = false

    //var placeholderTest = ""

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
        if(state.value.currentEdit == "Assignment"){
            assignmentName = TextFieldValue(assState.value.assignmentName)
            assignmentDesc = TextFieldValue(assState.value.assignmentDesc)
            daysLeft = TextFieldValue(assState.value.daysLeft.toString())
            //currentProject = assState.value.projectName
        }else if(state.value.currentEdit == "Note"){
            noteName = TextFieldValue(noteState.value.noteName)
            noteDesc = TextFieldValue(noteState.value.noteDesc)
            //currentProject = noteState.value.projectName
        }else if(state.value.currentEdit == "Exam"){
            examName = TextFieldValue(examState.value.examName)
            examDate = TextFieldValue(examState.value.examDate)
            //currentProject = examState.value.projectName
        }
        Log.d("CURRENT PROJECCCTTT", currentProject)

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

        /*
        TextField(

            value = projectName,
            onValueChange = { newText ->
                projectName = newText
            },
            label = { Text(text = "Projc Name") }
        )
        */
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
                            /*for(projectInTable in state.value.projects){
                                if(projectInTable.projectName == projectName.text){
                                    checkii = true
                                    break
                                }else{
                                    checkii = false
                                }
                            }
                            if(!checkii){
                                //mainViewModel.saveProject(Project(projectName.text))
                            }
                            */
                            mainViewModel.updateAssignment(Assignment(currentProject, assignmentName.text, assignmentDesc.text, daysLeft.text.toInt(), assState.value.id))

                            Toast.makeText(context,"Saved!", Toast.LENGTH_LONG).show()

                        }else{
                            Toast.makeText(context,"Make sure Days-Left are numeric", Toast.LENGTH_LONG).show()
                        }
                    }
                }else if(state.value.currentEdit == "Note"){
                    if(noteName.text.isNullOrEmpty() || noteDesc.text.isNullOrEmpty()){
                        Toast.makeText(context,"Fill out everything", Toast.LENGTH_LONG).show()

                    }else{
                        mainViewModel.updateNote(Note(currentProject, noteName.text, noteDesc.text, noteState.value.id))
                        Toast.makeText(context,"Saved!", Toast.LENGTH_LONG).show()
                    }
                }else if(state.value.currentEdit == "Exam"){
                    if(examName.text.isNullOrEmpty() || examDate.text.isNullOrEmpty()){
                        Toast.makeText(context,"Fill out everything", Toast.LENGTH_LONG).show()

                    }else{
                        mainViewModel.updateExam(Exam(currentProject, examName.text, examDate.text, examState.value.id))
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
