package com.trios2025rm.androidapp4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.trios2025rm.androidapp4.ui.history.HistoryScreen
import com.trios2025rm.androidapp4.ui.settings.SettingsScreen
import com.trios2025rm.androidapp4.ui.theme.AndroidApp4Theme
import com.trios2025rm.androidapp4.ui.theme.CounterViewModel
import com.trios2025rm.androidapp4.ui.theme.CounterViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: CounterViewModel by viewModels {
        CounterViewModelFactory(applicationContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidApp4Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent(viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AppContent(viewModel: CounterViewModel) {
    var currentScreen by remember { mutableStateOf("main") }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        when (currentScreen) {
                            "main" -> "Counter App"
                            "settings" -> "Settings"
                            "history" -> "History"
                            else -> "Counter App"
                        },
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    if (currentScreen != "main") {
                        IconButton(onClick = { currentScreen = "main" }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                        }
                    }
                },
                actions = {
                    if (currentScreen == "main") {
                        IconButton(onClick = { currentScreen = "history" }) {
                            Icon(
                                Icons.Filled.History,
                                contentDescription = "History",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        IconButton(onClick = { currentScreen = "settings" }) {
                            Icon(
                                Icons.Filled.Settings,
                                contentDescription = "Settings",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (currentScreen == "main") {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    // Decrement FAB
                    FloatingActionButton(
                        onClick = {
                            if (!viewModel.isAtMin()) {
                                viewModel.decrement()
                            } else {
                                showSnackbar = true
                                snackbarMessage = "Minimum value reached!"
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                        elevation = FloatingActionButtonDefaults.elevation(8.dp)
                    ) {
                        Icon(
                            Icons.Filled.Remove,
                            "Decrement",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    
                    // Increment FAB
                    FloatingActionButton(
                        onClick = {
                            if (!viewModel.isAtMax()) {
                                viewModel.increment()
                            } else {
                                showSnackbar = true
                                snackbarMessage = "Maximum value reached!"
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        elevation = FloatingActionButtonDefaults.elevation(8.dp)
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            "Increment",
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (currentScreen) {
                "main" -> CounterScreen(viewModel, padding)
                "settings" -> SettingsScreen(viewModel) { currentScreen = "main" }
                "history" -> HistoryScreen(viewModel) { currentScreen = "main" }
            }

            // Snackbar
            if (showSnackbar) {
                Snackbar(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomCenter),
                    action = {
                        TextButton(onClick = { showSnackbar = false }) {
                            Text(
                                "Dismiss",
                                color = MaterialTheme.colorScheme.inversePrimary
                            )
                        }
                    },
                    dismissAction = { showSnackbar = false },
                    containerColor = MaterialTheme.colorScheme.inverseSurface,
                    contentColor = MaterialTheme.colorScheme.inverseOnSurface
                ) {
                    Text(snackbarMessage)
                }
            }
        }
    }
}

@Composable
fun CounterScreen(viewModel: CounterViewModel, padding: PaddingValues) {
    val count by viewModel.count.collectAsStateWithLifecycle()
    val minValue by viewModel.minValue.collectAsStateWithLifecycle()
    val maxValue by viewModel.maxValue.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Welcome Card
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface,
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 6.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to Jetpack Compose!",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "A modern UI toolkit for Android",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        // Counter Card
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            elevation = CardDefaults.elevatedCardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Count",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                AnimatedContent(
                    targetState = count,
                    transitionSpec = {
                        slideInVertically { height -> height } + fadeIn() togetherWith
                        slideOutVertically { height -> -height } + fadeOut()
                    }
                ) { targetCount ->
                    Text(
                        text = targetCount.toString(),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Range: $minValue to $maxValue",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                )
            }
        }
        
        // Reset Button
        OutlinedButton(
            onClick = { viewModel.reset() },
            modifier = Modifier
                .padding(top = 16.dp)
                .height(48.dp),
            border = BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Icon(
                Icons.Filled.Refresh,
                contentDescription = "Reset",
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                "Reset Counter",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}