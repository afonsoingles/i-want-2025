package com.afonsoingles.iwant2025

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.ZoneId
import java.util.*
import com.afonsoingles.iwant2025.ui.theme.IWant2025Theme

class WidgetConfigActivity : ComponentActivity() {
    private var widgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Set the result to CANCELED in case the user backs out
        setResult(Activity.RESULT_CANCELED)
        
        // Find the widget id from the intent
        widgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        setContent {
            IWant2025Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WidgetConfigScreen(
                        onConfigComplete = { selectedZone ->
                            // Save the timezone preference
                            getSharedPreferences("WidgetPrefs", MODE_PRIVATE).edit()
                                .putString("widget_$widgetId", selectedZone.id)
                                .apply()

                            // Update widget
                            val appWidgetManager = AppWidgetManager.getInstance(this)
                            CountdownWidgetProvider.updateWidget(this, appWidgetManager, widgetId)

                            // Set the result ok
                            val resultValue = Intent()
                            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                            setResult(Activity.RESULT_OK, resultValue)
                            finish()
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun WidgetConfigScreen(onConfigComplete: (ZoneId) -> Unit) {
    val timezones = remember { TimeZone.getAvailableIDs().sorted() }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Widget Timezone",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyColumn {
            items(timezones) { zoneId ->
                TextButton(
                    onClick = { onConfigComplete(ZoneId.of(zoneId)) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(zoneId)
                }
            }
        }
    }
} 