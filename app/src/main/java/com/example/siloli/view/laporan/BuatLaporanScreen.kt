package com.example.siloli.view.laporan

import com.example.siloli.viewmodel.LaporanViewModel
import com.example.siloli.viewmodel.UiState

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.siloli.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuatLaporanScreen(
    onNavigateBack: () -> Unit,
    viewModel: LaporanViewModel = hiltViewModel()
) {
    var wilayah by remember { mutableStateOf("") }
    var aduan by remember { mutableStateOf("") }
    val createLaporanState by viewModel.createLaporanState.collectAsState()

    LaunchedEffect(createLaporanState) {
        if (createLaporanState is UiState.Success) {
            viewModel.resetCreateLaporanState()
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.buat_laporan_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = wilayah,
                onValueChange = { wilayah = it },
                label = { Text(stringResource(R.string.wilayah_hint)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = aduan,
                onValueChange = { aduan = it },
                label = { Text(stringResource(R.string.aduan_hint)) },
                modifier = Modifier.fillMaxWidth().weight(1f),
                minLines = 5
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (createLaporanState is UiState.Error) {
                Text(
                    text = (createLaporanState as UiState.Error).message,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = { viewModel.buatLaporan(wilayah, aduan) },
                modifier = Modifier.fillMaxWidth(),
                enabled = wilayah.length >= 3 && aduan.length >= 10 && createLaporanState !is UiState.Loading
            ) {
                if (createLaporanState is UiState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text(stringResource(R.string.simpan))
                }
            }
        }
    }
}
