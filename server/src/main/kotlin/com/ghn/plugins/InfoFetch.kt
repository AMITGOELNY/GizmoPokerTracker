package com.ghn.plugins

import com.ghn.gizmodb.tables.records.FeedRecord
import com.ghn.gizmodb.tables.references.FEED
import io.ktor.server.application.Application
import io.ktor.server.application.log
import it.skrape.core.htmlDocument
import it.skrape.fetcher.BrowserFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.a
import it.skrape.selects.html5.div
import it.skrape.selects.html5.h2
import it.skrape.selects.html5.img
import it.skrape.selects.html5.title
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import kotlinx.datetime.Clock
import kotlinx.datetime.toKotlinLocalDate
import org.jooq.DSLContext
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun Application.configureInfoFetch(db: DSLContext) {
    launch {
        while (true) {
            log.info("Starting fetch for feed!")
            val feed = infoFetch(db)

            db.delete(FEED).execute()
            feed.forEach {
                println("Found: $it")
                it.store()
            }

            delay(Duration.ofHours(6L))
        }
    }
}

suspend fun infoFetch(db: DSLContext): List<FeedRecord> {
    val links: List<FeedRecord> = skrape(BrowserFetcher) {
//        request {
//            url = "https://newsletter.philgalfond.com/"
//        }
//        response {
//            htmlDocument {
//                "div.group.h-full.overflow-hidden.transition-all" {
//
//                    findAll {
//                        map {
////                            it.a{ findFirst { text } } to
//                            Dopey(
//                                link = it.a { findFirst("href") }.text,
//                                image = it.a { findFirst("href") }.text,
//                                title = it.a { findFirst("href") }.text,
//                            )
//                        }
////                        this@htmlDocument.eachImage
//                    }
//                }
//            }
//        }

        request {
            url = "https://upswingpoker.com/blog/"
        }
        response {
            htmlDocument {
                "div.fl-post-grid-post" {
                    findAll {
                        map {
                            val (link, img) = it.div("fl-post-grid-image") {
                                it.a { it.eachHref }.first() to it.img { it.eachSrc }.first()
                            }
                            val (title, date) = it.h2("fl-post-grid-title") {
                                it.a {
                                    val items = it.title { it.text }.split("By")
                                    val titleTrimmed = items.first().trim()
                                    val date = items[1].split("|")
                                    titleTrimmed to date[1]
                                }
                            }

                            val localDate = LocalDate.parse(
                                date.trim(),
                                DateTimeFormatter.ofPattern("MMMM d, yyyy")
                            )
                            db.newRecord(FEED).apply {
                                this.link = link
                                this.image = img
                                this.title = title
                                this.pubDate = localDate.toKotlinLocalDate()
                                this.site = "Upswing Poker"
                                this.createdAt = Clock.System.now()
                                this.updatedAt = Clock.System.now()
                            }
                        }
                    }
                }
            }
        }
    }

    links.forEach { (text, link) -> println("$text --> $link") }
    return links
}
