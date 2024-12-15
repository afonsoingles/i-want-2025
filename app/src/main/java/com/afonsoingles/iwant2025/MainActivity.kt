package com.afonsoingles.iwant2025

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.afonsoingles.iwant2025.ui.theme.IWant2025Theme
import kotlinx.coroutines.delay
import java.time.*
import java.time.temporal.ChronoUnit
import java.util.*
import com.afonsoingles.iwant2025.utils.TimeUtils
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import com.afonsoingles.iwant2025.ui.theme.ButtonShape
import com.afonsoingles.iwant2025.ui.theme.CardShape

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IWant2025Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CountdownScreen()
                }
            }
        }
    }
}

@Composable
fun CountdownScreen() {
    val context = LocalContext.current
    var selectedZone by remember { mutableStateOf(ZoneId.systemDefault()) }
    var timeComponents by remember { mutableStateOf(TimeUtils.getTimeComponents(selectedZone)) }
    var showTimezoneDialog by remember { mutableStateOf(false) }
    
    // Create a key that changes when timezone changes to force recomposition
    val effectKey = selectedZone.toString()
    
    LaunchedEffect(effectKey) {
        while(true) {
            timeComponents = TimeUtils.getTimeComponents(selectedZone)
            delay(1000)
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // Banner Image
            Image(
                painter = painterResource(id = R.drawable.app_banner),
                contentDescription = "App Banner",
                modifier = Modifier
                    .size(180.dp),
                contentScale = ContentScale.Fit
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Countdown Display
            val (days, hours, minutes, seconds) = timeComponents
            Text(
                text = String.format("%02d : %02d : %02d : %02d", days, hours, minutes, seconds),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Days, Hours, Minutes, Seconds",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Timezone info and selector
            Text(
                text = "Current timezone: ${selectedZone.id}",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )
            
            TextButton(
                onClick = { showTimezoneDialog = true }
            ) {
                Text(
                    "Change Timezone",
                    fontSize = 12.sp
                )
            }
        }
        
        // Widget hint at bottom
        Text(
            text = "Did you know that you can put a widget in your lock screen?",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
    
    if (showTimezoneDialog) {
        TimezoneSelectionDialog(
            currentZone = selectedZone,
            onZoneSelected = { newZone -> 
                selectedZone = newZone  // This will trigger the LaunchedEffect
                showTimezoneDialog = false
            },
            onDismiss = { showTimezoneDialog = false }
        )
    }
}

@Composable
fun TimezoneSelectionDialog(
    currentZone: ZoneId,
    onZoneSelected: (ZoneId) -> Unit,
    onDismiss: () -> Unit
) {
    val timezones = remember { TimeZone.getAvailableIDs().sorted() }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select Timezone") },
        text = {
            LazyColumn {
                items(timezones) { zoneId ->
                    TextButton(
                        onClick = { onZoneSelected(ZoneId.of(zoneId)) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(zoneId)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}