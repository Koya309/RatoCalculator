package com.example.ratocalculator.settings.section

import android.content.pm.ActivityInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ratocalculator.R
import com.example.ratocalculator.core.utils.LockScreenOrientation
import com.example.ratocalculator.settings.model.AppLanguage
import com.example.ratocalculator.settings.model.AppTheme

@Composable
fun SystemSection(
    currentTheme: AppTheme,
    currentLanguage: AppLanguage,
    onNavigateToTheme: () -> Unit,
    onNavigateToLanguage: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.system),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToTheme() }
                    .padding(horizontal = 25.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.theme),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(currentTheme.labelResId),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToLanguage() }
                    .padding(horizontal = 25.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.language),
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = stringResource(currentLanguage.labelResId),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ThemeScreen(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        AppTheme.entries.forEach { theme ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .selectable(
                        selected = (theme == currentTheme),
                        onClick = { onThemeSelected(theme) }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (theme == currentTheme),
                    onClick = { onThemeSelected(theme)}
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(theme.labelResId),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
fun LanguageScreen(
    currentLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ){
        AppLanguage.entries.forEach { language ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .selectable(
                        selected = (language == currentLanguage),
                        onClick = { onLanguageSelected(language) }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (language == currentLanguage),
                    onClick = { onLanguageSelected(language) }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(language.labelResId),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}