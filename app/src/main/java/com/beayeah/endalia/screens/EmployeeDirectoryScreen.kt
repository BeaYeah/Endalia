package com.beayeah.endalia.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.beayeah.endalia.R
import com.beayeah.endalia.entities.Employee
import com.beayeah.endalia.viewModels.EmployeeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeDirectoryScreen(
    employeeViewModel: EmployeeViewModel,
    navController: NavHostController
) {
    var showSearchBar by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }

    employeeViewModel.loadEmployees()

    val filteredEmployees by employeeViewModel.filteredEmployees.collectAsState()
    val sortedEmployees = filteredEmployees.sortedBy { it.lastName }
    val groupedEmployees = sortedEmployees.groupBy { it.lastName.first().uppercaseChar() }

    Surface(color = Color.White) {
        val darkBlue = colorResource(id = R.color.dark_blue)
        val disabledDarkBlue = colorResource(id = R.color.disabled_dark_blue)
        val white = colorResource(id = R.color.white)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_small_logo),
                                contentDescription = null,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = stringResource(id = R.string.directory),
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = white,
                        titleContentColor = darkBlue,
                    ),
                    actions = {
                        IconButton(
                            onClick = { showSearchBar = !showSearchBar },
                            colors = IconButtonDefaults.iconButtonColors(
                                contentColor = darkBlue,
                                disabledContentColor = disabledDarkBlue
                            )
                        ) {
                            Icon(
                                Icons.Outlined.Search,
                                contentDescription = stringResource(id = R.string.search)
                            )
                        }

                        Box {
                            IconButton(
                                onClick = {
                                    showMenu = !showMenu
                                    if (showSearchBar) showSearchBar = false
                                },
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = darkBlue,
                                    disabledContentColor = disabledDarkBlue
                                )
                            ) {
                                Icon(
                                    Icons.Outlined.MoreVert,
                                    contentDescription = stringResource(id = R.string.menu)
                                )
                            }
                            MaterialTheme(
                                colorScheme = MaterialTheme.colorScheme.copy(surface = white)
                            ) {
                                DropdownMenu(
                                    expanded = showMenu,
                                    onDismissRequest = { showMenu = false },
                                    modifier = Modifier.width(180.dp)
                                ) {
                                    DropdownMenuItem(
                                        text = { Text(stringResource(id = R.string.logout)) },
                                        onClick = {
                                            showMenu = false
                                            navController.navigate("loginScreen") {
                                                popUpTo("employeeDirectoryScreen") { inclusive = true }
                                            }
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.AutoMirrored.Outlined.ExitToApp,
                                                contentDescription = stringResource(id = R.string.logout),
                                                tint = darkBlue
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (showSearchBar) {
                    val searchTerm by employeeViewModel.searchTerm.collectAsState()
                    SearchBar(
                        searchTerm = searchTerm,
                        onSearchTermChanged = { employeeViewModel.updateSearchTerm(it) }
                    )
                }
                AlphabeticalEmployeeList(groupedEmployees = groupedEmployees, navController)
            }
        }
    }
}

@Composable
fun SearchBar(searchTerm: String, onSearchTermChanged: (String) -> Unit) {
    val search = stringResource(id = R.string.search)
    val clearSearch = stringResource(id = R.string.clear_search)
    val darkBlue = colorResource(id = R.color.dark_blue)
    val disabledDarkBlue = colorResource(id = R.color.disabled_dark_blue)
    OutlinedTextField(
        value = searchTerm,
        onValueChange = onSearchTermChanged,
        label = { Text(search) },
        trailingIcon = {
            IconButton(
                onClick = { onSearchTermChanged("") },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = darkBlue,
                    disabledContentColor = disabledDarkBlue
                )
            ) {
                Icon(Icons.Outlined.Search, contentDescription = clearSearch)
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Text
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    )
}

@Composable
fun AlphabeticalEmployeeList(
    groupedEmployees: Map<Char, List<Employee>>,
    navController: NavHostController
) {
    val darkGrey = colorResource(id = R.color.endalia_dark_grey)
    LazyColumn {
        groupedEmployees.forEach { (initial, employees) ->
            item {
                Text(
                    text = initial.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    color = darkGrey
                )
            }
            items(employees) { employee ->
                EmployeeListItem(employee = employee, navController)
            }
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EmployeeListItem(employee: Employee, navController: NavHostController) {
    val lightGrey = colorResource(id = R.color.endalia_grey)
    val darkBlue = colorResource(id = R.color.dark_blue)
    val white = colorResource(id = R.color.white)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("employeeDetail/${employee.id}")
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageModifier = Modifier
            .size(40.dp)
            .clip(CircleShape)

        if (employee.portrait != null) {
            Image(
                painter = rememberImagePainter(employee.portrait),
                contentDescription = stringResource(id = R.string.profile_photo),
                modifier = imageModifier
            )
        } else {
            Box(
                modifier = imageModifier
                    .background(darkBlue),
                contentAlignment = Alignment.Center
            ) {
                val initials = (employee.name.firstOrNull()?.toString() ?: "") +
                        (employee.lastName.firstOrNull()?.toString() ?: "")
                Text(
                    text = initials,
                    color = white,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = "${employee.lastName}, ${employee.name}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = employee.jobTitle,
                color = lightGrey,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
