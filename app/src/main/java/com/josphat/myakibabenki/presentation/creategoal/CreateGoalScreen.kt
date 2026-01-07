package com.josphat.myakibabenki.presentation.creategoal

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.josphat.myakibabenki.presentation.components.AppTopBar
import com.josphat.myakibabenki.ui.theme.MyAkibaBenkiTheme
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGoalScreen(
    onNavigateBack: () -> Unit,
    onGoalCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateGoalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    // Handle goal creation success with toast
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.onSuccessMessageDismissed()
        }
    }

    // Handle goal creation success
    LaunchedEffect(uiState.goalCreated) {
        if (uiState.goalCreated) {
            onGoalCreated()
            viewModel.resetGoalCreatedState()
        }
    }

    // Show error as snackbar
    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.onErrorDismissed()
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Create a Goal",
                navigationIcon = Icons.AutoMirrored.Filled.ArrowBack,
                onNavigationClick = onNavigateBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Text(
                    text = "Please let's have the following:",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color.Gray,
                        fontSize = 16.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                GoalNameField(
                    value = uiState.goalName,
                    onValueChange = viewModel::updateGoalName,
                    isError = !uiState.isGoalNameValid,
                    enabled = !uiState.isLoading
                )
            }

            item {
                GoalCategoryField(
                    selectedCategory = uiState.goalCategory,
                    onCategorySelected = viewModel::updateGoalCategory,
                    enabled = !uiState.isLoading
                )
            }

            item {
                TargetAmountField(
                    value = uiState.targetAmount,
                    onValueChange = viewModel::updateTargetAmount,
                    isError = !uiState.isTargetAmountValid,
                    enabled = !uiState.isLoading
                )
            }

            item {
                TargetDateField(
                    value = uiState.targetDate,
                    onDateSelected = viewModel::updateTargetDate,
                    isError = !uiState.isTargetDateValid,
                    enabled = !uiState.isLoading
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                CreateGoalButton(
                    onClick = viewModel::createGoal,
                    enabled = uiState.isFormValid && !uiState.isLoading,
                    isLoading = uiState.isLoading
                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun GoalNameField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Goal Name",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text("Dubai Trip") },
            isError = isError,
            enabled = enabled,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = if (isError) Color.Red else Color.Gray.copy(alpha = 0.5f),
                focusedBorderColor = if (isError) Color.Red else Color(0xFF4CAF50)
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun GoalCategoryField(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = "Goal Category",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = { },
                readOnly = true,
                enabled = enabled,
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                        tint = Color.Gray,
                        modifier = Modifier.clickable(enabled = enabled) {
                            expanded = !expanded
                        }
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                    focusedBorderColor = Color(0xFF4CAF50)
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = enabled) { expanded = !expanded }
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                GoalCategory.entries.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = category.displayName,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        },
                        onClick = {
                            onCategorySelected(category.displayName)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun TargetAmountField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Target Amount",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = {
                Text(
                    text = "KES",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    ),
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                )
            },
            placeholder = { Text("10000.00") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = isError,
            enabled = enabled,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = if (isError) Color.Red else Color.Gray.copy(alpha = 0.5f),
                focusedBorderColor = if (isError) Color.Red else Color(0xFF4CAF50)
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TargetDateField(
    value: String,
    onDateSelected: (Long) -> Unit,
    isError: Boolean,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    // Set up date picker with tomorrow as minimum date
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 1) // Tomorrow
    val tomorrowMillis = calendar.timeInMillis

    // Reset calendar to get current day end time for proper validation
    val todayCalendar = Calendar.getInstance()
    todayCalendar.set(Calendar.HOUR_OF_DAY, 23)
    todayCalendar.set(Calendar.MINUTE, 59)
    todayCalendar.set(Calendar.SECOND, 59)
    todayCalendar.set(Calendar.MILLISECOND, 999)
    val todayEndMillis = todayCalendar.timeInMillis

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = tomorrowMillis
    )

    Column(modifier = modifier) {
        Text(
            text = "Savings Target Date",
            style = MaterialTheme.typography.bodyMedium.copy(
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = { },
            readOnly = true,
            placeholder = { Text("Select date") },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Open Calendar",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(enabled = enabled) {
                            showDatePicker = true
                        }
                )
            },
            enabled = enabled,
            isError = isError,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = if (isError) Color.Red else Color.Gray.copy(alpha = 0.5f),
                focusedBorderColor = if (isError) Color.Red else Color(0xFF4CAF50)
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = enabled) { showDatePicker = true }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selectedMillis ->
                            // Validate that selected date is after today
                            if (selectedMillis > todayEndMillis) {
                                onDateSelected(selectedMillis)
                                showDatePicker = false
                            } else {
                                // Keep dialog open and show error via ViewModel
                                // The validation will be handled by the ViewModel
                                onDateSelected(selectedMillis) // This will trigger validation error
                            }
                        }
                    },
                    enabled = datePickerState.selectedDateMillis != null
                ) {
                    Text(
                        text = "OK",
                        color = Color(0xFF4CAF50)
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(
                        text = "Cancel",
                        color = Color.Gray
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                showModeToggle = false,
                title = {
                    Text(
                        text = "Select Target Date",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(16.dp)
                    )
                },
                headline = {
                    Text(
                        text = "Choose a future date for your savings goal",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            )
        }
    }
}

@Composable
private fun CreateGoalButton(
    onClick: () -> Unit,
    enabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50),
            disabledContainerColor = Color.Gray.copy(alpha = 0.3f)
        ),
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(20.dp)
            )
        } else {
            Text(
                text = "Create a Goal",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CreateGoalScreenPreview() {
    MyAkibaBenkiTheme {
        CreateGoalScreen(
            onNavigateBack = { },
            onGoalCreated = { }
        )
    }
}