package com.beayeah.endalia.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.MailOutline
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.beayeah.endalia.R
import com.beayeah.endalia.entities.Employee
import com.beayeah.endalia.viewModels.EmployeeViewModel

@Composable
fun EmployeeDetailsScreen(
    employeeId: Int, employeeViewModel: EmployeeViewModel, navController: NavHostController
) {
    val employees by employeeViewModel.employees.collectAsState()
    val employee = employees.find { it.id == employeeId }

    employee?.let {
        EmployeeDetailContent(employee = it, navigateBack = { navController.popBackStack() })
    } ?: run {
        Text(stringResource(id = R.string.employee_not_found))
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun EmployeeDetailContent(employee: Employee, navigateBack: () -> Unit) {
    val context = LocalContext.current
    val darkBlue = colorResource(id = R.color.dark_blue)
    val disabledDarkBlue = colorResource(id = R.color.disabled_dark_blue)
    val white = colorResource(id = R.color.white)
    val darkGrey = colorResource(id = R.color.endalia_dark_grey)
    val lightGrey = colorResource(id = R.color.endalia_grey)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = darkBlue
                )
            }
        }

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                HorizontalDivider(color = lightGrey, thickness = 1.dp)

                val imageModifier = Modifier
                    .size(150.dp)
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
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Text(
                    text = "${employee.name} ${employee.lastName}",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(text = employee.jobTitle, fontSize = 15.sp, color = darkGrey)

                HorizontalDivider(color = lightGrey, thickness = 1.dp)

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            val intent =
                                Intent(Intent.ACTION_DIAL, Uri.parse("tel:${employee.phoneNumber}"))
                            context.startActivity(intent)
                        },
                        colors = IconButtonColors(
                            containerColor = white,
                            contentColor = darkBlue,
                            disabledContentColor = disabledDarkBlue,
                            disabledContainerColor = white
                        ),
                        modifier = Modifier
                            .size(60.dp)
                    ) {
                        Icon(Icons.Outlined.Phone, contentDescription = "call")
                    }

                    IconButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = Uri.parse("mailto:${employee.email}")
                            }
                            context.startActivity(intent)
                        },
                        colors = IconButtonColors(
                            containerColor = white,
                            contentColor = darkBlue,
                            disabledContentColor = disabledDarkBlue,
                            disabledContainerColor = white
                        ),
                        modifier = Modifier
                            .size(60.dp)
                    ) {
                        Icon(Icons.Outlined.MailOutline, contentDescription = "mail")
                    }
                }

                HorizontalDivider(thickness = 1.dp, color = lightGrey)

                Text(text = employee.phoneNumber)
                Text(text = employee.email)
            }
        }
    }
}
