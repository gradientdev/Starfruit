package com.gradient.starfruit

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.widget.RemoteViews

/**
 * Implementation of App Widget functionality.
 */
class QuoteWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    var quote: String? = preferences.getString("dailyQuote", "")
    var views = RemoteViews(context.packageName, R.layout.quote_widget)
    views.setTextViewText(R.id.appwidget_text, quote)
    appWidgetManager.updateAppWidget(appWidgetId, views)
}