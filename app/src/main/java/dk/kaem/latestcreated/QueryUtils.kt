package dk.kaem.latestcreated

import android.util.Log
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

object QueryUtils {
    private val logTag = this.javaClass.simpleName
    private val latestUrl: String = "https://kaem.dk/api/v1/app/latest"

    fun fetchLatestCreated() {
        latestUrl.httpGet().responseJson { request, response, result ->
            when(result) {
                is Result.Success -> {
                    LatestCreatedParserHandler.getLatestItems().clear()
                    val array = result.get().array()
                    (0 until array.length())
                        .map { array.getJSONObject(it) }
                        .forEach {
                            LatestCreatedParserHandler.setLatestItems(
                                it.getString("type"),
                                it.getString("title"),
                                it.getLong("date"),
                                it.getString("link")
                            )
                        }
                }
                is Result.Failure -> {
                    Log.e(logTag,"Failed: ${result.error}")
                }
            }
        }
    }

}