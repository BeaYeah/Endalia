package com.beayeah.endalia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.beayeah.endalia.screens.EmployeeDetailsScreen
import com.beayeah.endalia.screens.EmployeeDirectoryScreen
import com.beayeah.endalia.screens.LoginScreen
import com.beayeah.endalia.ui.theme.EndaliaTheme
import com.beayeah.endalia.viewModels.EmployeeViewModel
import com.beayeah.endalia.viewModels.UserViewModel

class MainActivity : ComponentActivity() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var employeeViewModel: EmployeeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[UserViewModel::class.java]
        employeeViewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        )[EmployeeViewModel::class.java]

        setContent {
            val navController = rememberNavController()
            EndaliaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController, startDestination = "loginScreen") {
                        composable("loginScreen") {
                            LoginScreen(userViewModel, navController)
                        }
                        composable("employeeDirectoryScreen") {
                            EmployeeDirectoryScreen(employeeViewModel, navController)
                        }
                        composable("employeeDetail/{employeeId}") { backStackEntry ->
                            val employeeId =
                                backStackEntry.arguments?.getString("employeeId")?.toIntOrNull()
                            if (employeeId != null) {
                                EmployeeDetailsScreen(
                                    employeeId = employeeId,
                                    employeeViewModel = employeeViewModel,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
