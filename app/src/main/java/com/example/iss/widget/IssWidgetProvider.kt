package com.example.iss.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.example.iss.R
import com.example.iss.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IssWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { widgetId ->
            val views = RemoteViews(context.packageName, R.layout.iss_widget)
            
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val database = AppDatabase.getDatabase(context)
                    val lastPosition = database.issDao().getLastPositionRaw()
                    
                    withContext(Dispatchers.Main) {
                        if (lastPosition != null) {
                            views.setTextViewText(R.id.widgetLat, "Lat: ${String.format("%.4f", lastPosition.latitude)}")
                            views.setTextViewText(R.id.widgetLng, "Lng: ${String.format("%.4f", lastPosition.longitude)}")
                        } else {
                            views.setTextViewText(R.id.widgetLat, "Lat: --")
                            views.setTextViewText(R.id.widgetLng, "Lng: --")
                        }
                        
                        appWidgetManager.updateAppWidget(widgetId, views)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
