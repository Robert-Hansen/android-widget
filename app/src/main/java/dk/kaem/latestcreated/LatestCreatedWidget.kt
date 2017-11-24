package dk.kaem.latestcreated

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import dk.kaem.kaemwidget.R
import android.app.PendingIntent

/**
 * Implementation of App Widget functionality.
 */
class LatestCreatedWidget : AppWidgetProvider() {
    private val logTag = this.javaClass.simpleName
    val REFRESH_BUTTON = "dk.kaem.latestcreated.REFRESH"
    val SEE_ITEM = "dk.kaem.latestcreated.SEE"
    val EXTRA_ITEM = "dk.kaem.latestcreated.EXTRA_ITEM"

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        QueryUtils.fetchLatestCreated()
        LatestCreatedParserHandler.getLatestItems()

        for (appWidgetId in appWidgetIds) {
            // The empty view is displayed when the collection has no items. It should be a sibling
            // of the collection view.
            //rv.setEmptyView(R.id.stack_view, R.id.empty_view);
            val rv = RemoteViews(context.packageName, R.layout.dk_kaem_latestcreated_widget_layout)

            // Here we setup the intent which points to the LatestCreatedFactory which will
            // provide the views for this collection.
            val intent = Intent(context, LatestCreatedService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            // TODO: fix click for SE button
            // Here we setup the a pending intent template. Individuals items of a collection
            // cannot setup their own pending intents, instead, the collection as a whole can
            // setup a pending intent template, and the individual items can set a fillInIntent
            // to create unique before on an item to item basis.
            val seeIntent = Intent(context, LatestCreatedWidget::class.java)
            seeIntent.action = SEE_ITEM
            val pIntent = PendingIntent.getBroadcast(context, 0, seeIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            seeIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            seeIntent.data = Uri.parse(seeIntent.toUri(Intent.URI_INTENT_SCHEME))
            rv.setPendingIntentTemplate(R.id.dkLatestCreatedWidgetCollectionList, pIntent)


            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context, intent: Intent) {
        println(intent.action)

        when(intent.action) {
            SEE_ITEM -> {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                println("Touched view " + viewIndex)
            }
            REFRESH_BUTTON -> {
                println("Refresh Button Touched")
            }
        }

        super.onReceive(context, intent)
    }

    private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                 appWidgetId: Int) {
        // Construct the RemoteViews object which defines the view of out widget
        //RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.testlist);
        val views = initViews(context, appWidgetManager, appWidgetId)
        // Instruct the widget manager to update the widget
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widgetCollectionList)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun initViews(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.dk_kaem_latestcreated_widget_layout)

        // Here we setup the intent which points to the LatestCreatedFactory which will
        // provide the views for this collection.
        val intent = Intent(context, LatestCreatedService::class.java)
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)

        // When intents are compared, the extras are ignored, so we need to embed the extras
        // into the data so that the extras will not be ignored.
        intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))
        rv.setRemoteAdapter(R.id.dkLatestCreatedWidgetCollectionList, intent)

        return rv
    }
}
