package com.ghn.client

import com.prof18.rssparser.RssParser
import com.prof18.rssparser.model.RssChannel

internal class GizmoRSSClient(
    private val rssParser: RssParser,
) {
    suspend fun getFeed(): RssChannel {
        return rssParser.getRssChannel("https://www.pokernews.com/rss.php")
    }
}
