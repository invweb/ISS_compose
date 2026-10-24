package com.example.iss.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.iss.R
import com.example.iss.data.CustomLandmark
import com.example.iss.data.CustomLandmarkStorage
import com.example.iss.viewmodel.IssViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IssScreen(
    onBack: () -> Unit,
    viewModel: IssViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    var showAddLandmarkDialog by remember { mutableStateOf(false) }
    var landmarkName by remember { mutableStateOf("") }
    var pendingLat by remember { mutableStateOf(0.0) }
    var pendingLng by remember { mutableStateOf(0.0) }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            viewModel.startLocationUpdates()
        } else {
            viewModel.onLocationPermissionDenied()
        }
    }

    val customLandmarks = remember {
        CustomLandmarkStorage.loadLandmarks(context).sortedBy { it.name.lowercase() }
    }

    LaunchedEffect(Unit) {
        viewModel.initPrefs(context)
        viewModel.initLocation(context)
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            viewModel.startLocationUpdates()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    if (showAddLandmarkDialog) {
        AlertDialog(
            onDismissRequest = { showAddLandmarkDialog = false },
            title = { Text(stringResource(R.string.add_landmark)) },
            text = {
                Column {
                    OutlinedTextField(
                        value = landmarkName,
                        onValueChange = { landmarkName = it },
                        label = { Text(stringResource(R.string.enter_landmark_name)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Saved (${customLandmarks.size})",
                        style = MaterialTheme.typography.bodySmall
                    )
                    LazyColumn {
                        itemsIndexed(customLandmarks) { index, landmark ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clickable {
                                        pendingLat = landmark.latitude
                                        pendingLng = landmark.longitude
                                        landmarkName = landmark.name
                                    },
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = landmark.name,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                IconButton(
                                    onClick = {
                                        CustomLandmarkStorage.deleteLandmark(context, index)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (landmarkName.isNotBlank()) {
                            val newLandmark = CustomLandmark(
                                name = landmarkName,
                                latitude = pendingLat,
                                longitude = pendingLng
                            )
                            val updated = customLandmarks + newLandmark
                            CustomLandmarkStorage.saveLandmarks(context, updated)
                            landmarkName = ""
                            showAddLandmarkDialog = false
                        }
                    },
                    enabled = landmarkName.isNotBlank()
                ) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    landmarkName = ""
                    showAddLandmarkDialog = false
                }) {
                    Text(stringResource(android.R.string.cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.toolbar_map)) },
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
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(top = 8.dp)) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.iss_title),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    } else if (uiState.error != null) {
                        Text(
                            text = stringResource(R.string.error, uiState.error!!),
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        ElevatedButton(
                            onClick = { viewModel.retryFetch() },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(stringResource(R.string.retry))
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.latitude, String.format("%.4f", uiState.latitude)),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                        Text(
                            text = stringResource(R.string.longitude, String.format("%.4f", uiState.longitude))
                        )
                        uiState.lastUpdated?.let { timestamp ->
                            Text(
                                text = "Updated: ${formatTime(timestamp, context)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth().clipToBounds()) {
                if (!uiState.isLoading) {
                    IssMap(
                        latitude = uiState.latitude,
                        longitude = uiState.longitude,
                        userLatitude = uiState.userLatitude,
                        userLongitude = uiState.userLongitude,
                        pathHistory = uiState.pathHistory,
                        customLandmarks = customLandmarks,
                        onAddLandmark = { lat, lng ->
                            pendingLat = lat
                            pendingLng = lng
                            showAddLandmarkDialog = true
                        }
                    )
                } else {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

private fun formatTime(timestamp: Long, context: android.content.Context): String {
    val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    return sdf.format(java.util.Date(timestamp))
}
