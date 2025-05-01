package com.trios2025rm.androidapp4.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.trios2025rm.androidapp4.ui.theme.CounterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: CounterViewModel,
    onNavigateBack: () -> Unit
) {
    val minValue by viewModel.minValue.collectAsState()
    val maxValue by viewModel.maxValue.collectAsState()
    
    var tempMinValue by remember { mutableStateOf(minValue.toString()) }
    var tempMaxValue by remember { mutableStateOf(maxValue.toString()) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Settings Card
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Counter Limits",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    OutlinedTextField(
                        value = tempMinValue,
                        onValueChange = { 
                            tempMinValue = it
                            showError = false
                        },
                        label = { Text("Minimum Value") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = showError,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = tempMaxValue,
                        onValueChange = { 
                            tempMaxValue = it
                            showError = false
                        },
                        label = { Text("Maximum Value") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = showError,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        singleLine = true
                    )

                    if (showError) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            Button(
                onClick = {
                    try {
                        val newMin = tempMinValue.toInt()
                        val newMax = tempMaxValue.toInt()
                        
                        if (newMin >= newMax) {
                            showError = true
                            errorMessage = "Minimum value must be less than maximum value"
                        } else {
                            viewModel.setMinValue(newMin)
                            viewModel.setMaxValue(newMax)
                            showError = false
                            onNavigateBack()
                        }
                    } catch (e: NumberFormatException) {
                        showError = true
                        errorMessage = "Please enter valid numbers"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    "Save Settings",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
} 