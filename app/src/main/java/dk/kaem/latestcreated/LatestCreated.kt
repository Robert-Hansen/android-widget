package dk.kaem.latestcreated

/**
 * Latest Creations on Kaem.dk
 *
 * @property mType type of item
 * @property mTitle title of item
 * @property mDate creation date of item
 * @property mLink link to item
 */
data class LatestCreated(
        val mType: String,
        val mTitle: String,
        val mDate: Long,
        val mLink: String
)