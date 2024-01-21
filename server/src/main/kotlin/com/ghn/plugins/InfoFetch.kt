package com.ghn.plugins

import com.ghn.client.GizmoRSSClient
import com.ghn.gizmodb.common.models.NewsCategory
import com.ghn.gizmodb.tables.records.FeedRecord
import com.ghn.gizmodb.tables.references.FEED
import com.prof18.rssparser.RssParser
import io.ktor.server.application.Application
import io.ktor.server.application.log
import it.skrape.core.htmlDocument
import it.skrape.fetcher.BrowserFetcher
import it.skrape.fetcher.response
import it.skrape.fetcher.skrape
import it.skrape.selects.html5.a
import it.skrape.selects.html5.div
import it.skrape.selects.html5.h1
import it.skrape.selects.html5.h2
import it.skrape.selects.html5.img
import it.skrape.selects.html5.time
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
    val gizmoRSSClient = GizmoRSSClient(RssParser())
    launch {
        while (true) {
            log.info("Starting fetch for feed!")
            val feed1 = infoFetch(db)
            val feed2 = infoFetch2(db)
            val feed3 = infoFetch3(db)

            db.delete(FEED).execute()

            listOf(feed1, feed2, feed3).flatten().forEach {
                println("Found: $it")
                it.store()
            }

            val rssFeed = gizmoRSSClient.getFeed()
            val feedItems = rssFeed.items.mapNotNull {
                val title = it.title
                val subtitle = it.description
                val pubDate = it.pubDate

                if (title == null || subtitle == null || pubDate == null) {
                    return@mapNotNull null
                }

                val localDate = LocalDate.parse(
                    pubDate,
                    DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z")
                )

                db.newRecord(FEED).apply {
                    this.link = it.link.orEmpty()
                    this.image = it.image
                    this.title = it.title.orEmpty()
                    this.pubDate = localDate.toKotlinLocalDate()
                    this.site = "Poker News"
                    this.createdAt = Clock.System.now()
                    this.updatedAt = Clock.System.now()
                    this.category = NewsCategory.NEWS
                }
            }

            feedItems.forEach {
                println("Found: $it")
                it.store()
            }

            delay(Duration.ofHours(6L))
        }
    }
}

suspend fun infoFetch(db: DSLContext): List<FeedRecord> {
    val links: List<FeedRecord> = skrape(BrowserFetcher) {
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
                                this.category = NewsCategory.STRATEGY
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

suspend fun infoFetch2(db: DSLContext): List<FeedRecord> {
    val links: List<FeedRecord> = skrape(BrowserFetcher) {
        request {
            url = "https://www.cardschat.com/news/poker-news/"
        }
        response {
            htmlDocument {
                "li.wp-block-post" {
                    findAll {
                        map {
                            val (link, img) = it.a {
                                it.a { it.eachHref }.first() to it.img { it.eachSrc }.first()
                            }
                            val date = it.time {
                                findFirst { text }
                            }
                            val title = it.h2 {
                                findFirst { text }
                            }
                            link to title

                            val localDate = LocalDate.parse(
                                date.trim(),
                                DateTimeFormatter.ofPattern("MMMM d, yyyy")
                            )
                            db.newRecord(FEED).apply {
                                this.link = link
                                this.image = img
                                this.title = title
                                this.pubDate = localDate.toKotlinLocalDate()
                                this.site = "CardsChat"
                                this.createdAt = Clock.System.now()
                                this.updatedAt = Clock.System.now()
                                this.category = NewsCategory.NEWS
                            }
                        }
                    }
                }
            }
        }
    }

    links.forEach(::println)
    return links
}

suspend fun infoFetch3(db: DSLContext): List<FeedRecord> {
    val links: List<FeedRecord> = skrape(BrowserFetcher) {
        request {
            url = "https://www.philgalfond.com/articles"
        }

        response {
            htmlDocument {
                "article.blog-basic-grid--container" {
                    findAll {
                        map {
                            println("hola senor, ${it.text}")
                            val (link, img) = it.a {
                                it.a { it.eachHref }.first() to it.img { it.eachSrc }.firstOrNull()
                            }
                            val date = it.time {
                                findFirst { text }
                            }
                            val title = it.h1 {
                                findFirst { text }
                            }
                            link to title

                            val localDate = LocalDate.parse(
                                date.trim(),
                                DateTimeFormatter.ofPattern("M/d/yy")
                            )
                            db.newRecord(FEED).apply {
                                this.link = "https://www.philgalfond.com$link"
                                this.image = img
                                this.title = title
                                this.pubDate = localDate.toKotlinLocalDate()
                                this.site = "Phil Galfond"
                                this.createdAt = Clock.System.now()
                                this.updatedAt = Clock.System.now()
                                this.category = NewsCategory.STRATEGY
                            }
                        }
                    }
                }
            }
        }
    }

    links.forEach(::println)
    return links
}
