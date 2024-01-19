package com.ccl3.project_helm_meyer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.ccl3.project_helm_meyer.data.MainDatabase
import com.ccl3.project_helm_meyer.ui.theme.MyApplicationTheme
import com.ccl3.project_helm_meyer.ui.view.MainView
import com.ccl3.project_helm_meyer.ui.view.MainViewModel

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(this, MainDatabase::class.java, "MainDatabase.db").build()
    }

    private val mainViewModel by viewModels<MainViewModel> (
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(db.dao) as T
                }
            }
        }
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Greeting("Android")
                    MainView(mainViewModel)
                }
            }
        }
    }
}
