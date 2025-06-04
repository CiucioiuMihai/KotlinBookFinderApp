package com.example.bookfinderapp.ui.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bookfinderapp.viewmodel.AuthViewModel
import com.example.bookfinderapp.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onLogoutSuccess: () -> Unit = {}
) {
    val isDarkMode by viewModel.isDarkMode.collectAsState()
    val userName = authViewModel.userName
    val userId = authViewModel.userId

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        if (userName != null) {
            Text(text = "Name: $userName", modifier = Modifier.padding(bottom = 4.dp))
        }
        if (userId != null) {
            Text(text = "User ID: $userId", modifier = Modifier.padding(bottom = 16.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Dark Mode")
            Spacer(modifier = Modifier.width(16.dp))
            Switch(
                checked = isDarkMode,
                onCheckedChange = { viewModel.setDarkMode(it) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Logout button
        Button(
            onClick = {
                authViewModel.logout()
                onLogoutSuccess()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Logout"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Logout")
        }
    }
}
