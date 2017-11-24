package dk.kaem.latestcreated

import java.util.ArrayList

object LatestCreatedParserHandler {
    private val latestItems = ArrayList<LatestCreated>()

    fun getLatestItems(): ArrayList<LatestCreated> {
        Thread.sleep(1000)
        return this.latestItems
    }

    fun setLatestItems(type: String, username: String, date: Long, link: String) {
        this.latestItems.add(LatestCreated(type, username, date, link))
    }
}