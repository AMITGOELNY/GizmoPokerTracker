package com.ghn.database


import kotlinx.datetime.Instant
import org.jooq.Converter

@Suppress("UNUSED")
class JooqInstantConverter : Converter<Long, Instant> {
    override fun fromType(): Class<Long> = Long::class.java

    override fun toType(): Class<Instant> = Instant::class.java

    override fun from(databaseObject: Long?): Instant? {
        return databaseObject?.run(Instant::fromEpochMilliseconds)
    }

    override fun to(userObject: Instant?): Long? {
        return userObject?.toEpochMilliseconds()
    }
}