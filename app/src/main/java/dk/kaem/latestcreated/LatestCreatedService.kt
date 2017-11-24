package dk.kaem.latestcreated

import android.content.Intent
import android.widget.RemoteViewsService

class LatestCreatedService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsService.RemoteViewsFactory =
            LatestCreatedFactory(applicationContext, intent)

}
