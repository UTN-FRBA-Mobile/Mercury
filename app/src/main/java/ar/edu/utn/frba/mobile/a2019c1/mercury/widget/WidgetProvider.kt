package ar.edu.utn.frba.mobile.a2019c1.mercury.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import ar.edu.utn.frba.mobile.a2019c1.mercury.R

class WidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        appWidgetIds.forEach {
            val remoteViews = updateWidgetListView(context, it)
            appWidgetManager.updateAppWidget(it, remoteViews)
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private fun updateWidgetListView(context: Context, appWidgetId: Int): RemoteViews {

        //which layout to show on widget
        val remoteViews = RemoteViews(context.packageName, R.layout.widget)

        //RemoteViews Service needed to provide adapter for ListView
        val svcIntent = Intent(context, WidgetService::class.java)
        //passing app widget id to that RemoteViews Service
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        //setting a unique Uri to the intent
        //don't know its purpose to me right now
        svcIntent.data = Uri.parse(
            svcIntent.toUri(Intent.URI_INTENT_SCHEME)
        )
        //setting adapter to listview of the widget
        remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget, svcIntent)
        //setting an empty view in case of no data
        remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view)
        return remoteViews
    }
}
