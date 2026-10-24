package com.example.iss.ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iss.R
import com.example.iss.data.CustomLandmark
import com.example.iss.viewmodel.CustomLandmarkViewModel

private const val PREFS_NAME = "iss_settings"
private const val KEY_ISS_COLOR = "iss_marker_color"
private const val KEY_PATH_COLOR = "iss_path_color"
const val DEFAULT_ISS_COLOR = 0xFF333333.toInt()
const val DEFAULT_PATH_COLOR = 0xFFE53935.toInt()

private val colorOptions = listOf(
    0xFFE53935.toInt() to "Red",
    0xFF1E88E5.toInt() to "Blue",
    0xFF43A047.toInt() to "Green",
    0xFFFF9800.toInt() to "Orange",
    0xFFFFEB3B.toInt() to "Yellow",
    0xFF8E24AA.toInt() to "Purple",
    0xFF00BCD4.toInt() to "Cyan",
    0xFF333333.toInt() to "Dark",
    0xFFFFFFFF.toInt() to "White",
    0xFFE91E63.toInt() to "Pink"
)

fun getIssMarkerColor(context: Context): Int {
    return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .getInt(KEY_ISS_COLOR, DEFAULT_ISS_COLOR)
}

fun getPathColor(context: Context): Int {
    return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .getInt(KEY_PATH_COLOR, DEFAULT_PATH_COLOR)
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: CustomLandmarkViewModel = viewModel()
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE) }
    val pathPrefs = remember { context.getSharedPreferences("iss_path", Context.MODE_PRIVATE) }
    var showClearDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf<CustomLandmark?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    val currentIssColor = remember { getIssMarkerColor(context) }
    val currentPathColor = remember { getPathColor(context) }
    val landmarks by viewModel.landmarks.collectAsState()
    
    val filteredLandmarks = landmarks.filter {
        searchQuery.isBlank() || it.name.lowercase().contains(searchQuery.lowercase())
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // ISS Marker Color section
            Text(
                text = stringResource(R.string.settings_iss_color),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                colorOptions.forEach { (colorInt, _) ->
                    val isSelected = colorInt == currentIssColor
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(colorInt))
                            .then(
                                if (isSelected) Modifier.border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                else Modifier.border(1.dp, Color.Gray, CircleShape)
                            )
                            .clickable {
                                prefs.edit().putInt(KEY_ISS_COLOR, colorInt).apply()
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Path Color section
            Text(
                text = stringResource(R.string.settings_path_color),
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                colorOptions.forEach { (colorInt, _) ->
                    val isSelected = colorInt == currentPathColor
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(Color(colorInt))
                            .then(
                                if (isSelected) Modifier.border(3.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                else Modifier.border(1.dp, Color.Gray, CircleShape)
                            )
                            .clickable {
                                prefs.edit().putInt(KEY_PATH_COLOR, colorInt).apply()
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Custom Landmarks section
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.settings_landmarks_section),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))

                    // Search field
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(stringResource(R.string.settings_search_landmarks))
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))

                    if (landmarks.isEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Map,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.settings_no_landmarks),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    } else if (filteredLandmarks.isEmpty()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.settings_no_results, searchQuery),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    } else {
                        Text(
                            text = if (searchQuery.isBlank()) {
                                stringResource(R.string.settings_landmarks_count, landmarks.size)
                            } else {
                                stringResource(R.string.settings_found_landmarks, filteredLandmarks.size, landmarks.size)
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(filteredLandmarks, key = { it.id }) { landmark ->
                                LandmarkSettingsItem(
                                    landmark = landmark,
                                    onDelete = { showDeleteDialog = landmark }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Clear path button
            Button(
                onClick = { showClearDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.settings_clear_path))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App info
            Text(
                text = stringResource(R.string.settings_version, "1.0.0"),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }

    // Clear path dialog
    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            confirmButton = {
                Button(onClick = {
                    pathPrefs.edit().remove("path_history").apply()
                    showClearDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showClearDialog = false }) {
                    Text(stringResource(R.string.back))
                }
            },
            title = { Text(stringResource(R.string.settings_clear_path)) }
        )
    }

    // Delete landmark dialog
    showDeleteDialog?.let { landmark ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            title = { Text(stringResource(R.string.settings_delete_landmark_title)) },
            text = { Text(stringResource(R.string.settings_delete_landmark_message, landmark.name)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteLandmark(landmark)
                        searchQuery = ""
                        showDeleteDialog = null
                    }
                ) {
                    Text(stringResource(R.string.settings_delete_action), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = null }
                ) {
                    Text(stringResource(android.R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun LandmarkSettingsItem(
    landmark: CustomLandmark,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = landmark.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = String.format("%.4f°, %.4f°", landmark.latitude, landmark.longitude),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        IconButton(
            onClick = onDelete,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = stringResource(R.string.settings_delete_landmark_desc),
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}
