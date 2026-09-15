package com.isx3i.nitrokill.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.isx3i.nitrokill.R
import com.isx3i.nitrokill.data.PrefsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsScreen(
    prefs: PrefsManager,
    onBack: () -> Unit,
    onLanguageChanged: (String) -> Unit,
    onOpenNotificationSettings: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.options_title)) },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text(stringResource(R.string.action_back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            SectionTitle(stringResource(R.string.options_language))
            Row {
                OutlinedButton(onClick = { onLanguageChanged("ar") }) {
                    Text(stringResource(R.string.lang_arabic))
                }
                OutlinedButton(
                    onClick = { onLanguageChanged("en") },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(stringResource(R.string.lang_english))
                }
            }

            Spacer(Modifier.height(24.dp))
            SectionTitle(stringResource(R.string.options_permissions))

            PermissionRow(
                label = stringResource(R.string.perm_notification),
                onClick = onOpenNotificationSettings
            )

            Spacer(Modifier.height(24.dp))
            SectionTitle(stringResource(R.string.options_about))
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text(stringResource(R.string.about_version), style = MaterialTheme.typography.bodyMedium)
                    Text(stringResource(R.string.about_email), style = MaterialTheme.typography.bodyMedium)
                    Text(stringResource(R.string.about_developer), style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(text, style = MaterialTheme.typography.titleMedium)
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun PermissionRow(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(top = 8.dp))
        Button(onClick = onClick) {
            Text(stringResource(R.string.perm_grant))
        }
    }
}
