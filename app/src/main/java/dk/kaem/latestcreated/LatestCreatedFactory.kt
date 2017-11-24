package dk.kaem.latestcreated

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import dk.kaem.kaemwidget.R
import java.text.SimpleDateFormat
import java.util.*

class LatestCreatedFactory(private val mContext: Context, intent: Intent) : RemoteViewsService.RemoteViewsFactory {
    private val logTag = this.javaClass.simpleName
    private val item = LatestCreatedParserHandler.getLatestItems()
    val REFRESH_BUTTON = "dk.kaem.latestcreated.REFRESH"
    val SEE_ITEM = "dk.kaem.latestcreated.SEE"

    override fun getCount(): Int = item.size

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewAt(position: Int): RemoteViews? {
        Log.i(logTag, "getViewAt")
        val row = RemoteViews(mContext.packageName, R.layout.dk_kaem_latestcreated_list_item)
        val date = Date(item[position].mDate * 1000L) // *1000 is to convert seconds to milliseconds
        val sdf = SimpleDateFormat("EEEE d MMMM, k:m", Locale("da", "DK")) // the format of your date
        val formattedDate = sdf.format(date)

        // TODO: Fix click for rows (SE button)
        val extras = Bundle()
        extras.putInt(SEE_ITEM, position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)
        // Make it possible to distinguish the individual on-click
        // action of a given item
        row.setOnClickFillInIntent(R.id.dkLatestCreatedSeeBtn, fillInIntent)

        // We construct a remote views item based on our widget item xml file, and set the
        // text based on the position.
        row.setTextViewText(R.id.dkLatestCreatedType, item[position].mType)
        row.setTextViewText(R.id.dkLatestCreatedUsername, item[position].mTitle)
        row.setTextViewText(R.id.dkLatestCreatedCreated_at, formattedDate)

        return row
    }

    override fun getViewTypeCount(): Int = 1

    override fun hasStableIds(): Boolean = true

    override fun onCreate() {
        QueryUtils.fetchLatestCreated()
    }

    override fun onDataSetChanged() {
        // This is triggered when you call AppWidgetManager notifyAppWidgetViewDataChanged
        // on the collection view corresponding to this factory. You can do heaving lifting in
        // here, synchronously. For example, if you need to process an image, fetch something
        // from the network, etc., it is ok to do it here, synchronously. The widget will remain
        // in its current state while work is being done here, so you don't need to worry about
        // locking up the widget.
        Log.d(logTag, "onDataSetChanged")
        QueryUtils.fetchLatestCreated()
    }

    override fun onDestroy() {
        Log.d(logTag, "onDestroy")
        item.clear()
    }
}