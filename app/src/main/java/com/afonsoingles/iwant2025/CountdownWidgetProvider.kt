package com.afonsoingles.iwant2025

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.afonsoingles.iwant2025.utils.TimeUtils
import java.time.ZoneId

class CountdownWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appWidgetId ->
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_countdown)
            
            // Get saved timezone or use default
            val savedZoneId = context.getSharedPreferences("WidgetPrefs", Context.MODE_PRIVATE)
                .getString("widget_$appWidgetId", null)
            val zoneId = savedZoneId?.let { ZoneId.of(it) } ?: ZoneId.systemDefault()
            
            val components = TimeUtils.getTimeComponents(zoneId)
            views.setTextViewText(
                R.id.widget_timezone_text,
                "Timezone: ${zoneId.id}"
            )
            views.setTextViewText(
                R.id.widget_countdown_text,
                "${components[0]} days, ${components[1]} hours"
            )
            
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
} 