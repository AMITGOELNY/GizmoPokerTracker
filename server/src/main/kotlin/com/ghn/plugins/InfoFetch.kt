package com.ghn.plugins

import com.ghn.client.GizmoRSSClient
import com.ghn.gizmodb.common.models.NewsCategory
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
import it.skrape.selects.html5.h1
import it.skrape.selects.html5.h2
import it.skrape.selects.html5.img
import it.skrape.selects.html5.time
import it.skrape.selects.html5.title
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import kotlinx.datetime.toKotlinLocalDate
import org.jooq.DSLContext
import org.koin.ktor.ext.inject
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.time.Clock

fun Application.configureInfoFetch() {
    val db by inject<DSLContext>()
    val gizmoRSSClient by inject<GizmoRSSClient>()

    // Read configuration values
    val initialDelayHours = environment.config.propertyOrNull("ktor.infoFetch.initialDelayHours")
        ?.getString()?.toLongOrNull() ?: 6L
    val intervalHours = environment.config.propertyOrNull("ktor.infoFetch.intervalHours")
        ?.getString()?.toLongOrNull() ?: 6L

    log.info("InfoFetch scheduler configured - Initial delay: ${initialDelayHours}h, Interval: ${intervalHours}h")

    launch {
        // Wait before first run
        log.info("InfoFetch scheduler waiting ${initialDelayHours}h before first run")
        delay(Duration.ofHours(initialDelayHours))

        while (true) {
            val startTime = System.currentTimeMillis()
            log.info("Starting scheduled feed fetch")
            var totalRecords = 0

            // Fetch from all sources
            val feed1 = try {
                infoFetch(db)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                log.error("Failed to fetch from Upswing Poker", e)
                emptyList()
            }

            val feed2 = try {
                infoFetch2(db)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                log.error("Failed to fetch from CardsChat", e)
                emptyList()
            }

            val feed3 = try {
                infoFetch3(db)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                log.error("Failed to fetch from Phil Galfond", e)
                emptyList()
            }

            // Clear existing feed data
            db.delete(FEED).execute()
            log.info("Cleared existing feed records")

            // Store scraped feed items
            listOf(feed1, feed2, feed3).flatten().forEach {
                it.store()
                totalRecords++
            }

            // Fetch and store RSS feed
            val rssFeedItems = try {
                val rssFeed = gizmoRSSClient.getFeed()
                rssFeed.items.mapNotNull {
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
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                log.error("Failed to fetch RSS feed from Poker News", e)
                emptyList()
            }

            rssFeedItems.forEach {
                it.store()
                totalRecords++
            }

            val duration = System.currentTimeMillis() - startTime
            log.info("Feed fetch completed successfully - Stored $totalRecords records in ${duration}ms")

            // Wait for next run
            log.info("Next feed fetch scheduled in ${intervalHours}h")
            delay(Duration.ofHours(intervalHours))
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

    links.forEach { record -> println("${record.title} --> ${record.link}") }
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
