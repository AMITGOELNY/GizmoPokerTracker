@file:Suppress("ktlint")
package com.ghn.database

import org.jooq.BindingGetResultSetContext
import org.jooq.BindingSetStatementContext
import org.jooq.Converter
import org.jooq.impl.AbstractBinding
import java.sql.Timestamp
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import kotlin.time.toKotlinInstant

@Suppress("UNUSED")
private class JooqInstantConverter : Converter<Timestamp, Instant> {
    override fun fromType(): Class<Timestamp> = Timestamp::class.java

    override fun toType(): Class<Instant> = Instant::class.java

    override fun from(databaseObject: Timestamp?): Instant? {
        return databaseObject?.toInstant()?.toKotlinInstant()
    }

    override fun to(userObject: Instant?): Timestamp? {
        return userObject?.toJavaInstant()?.run(Timestamp::from)
    }
}

@Suppress("UNUSED")
class JooqInstantBinding : AbstractBinding<Timestamp, Instant>() {
    private val convert = JooqInstantConverter()

    override fun converter(): Converter<Timestamp, Instant> {
        return convert
    }

    override fun get(ctx: BindingGetResultSetContext<Instant>) {
        ctx.convert(converter()).value(ctx.resultSet().getTimestamp(ctx.index()))
    }

    override fun set(ctx: BindingSetStatementContext<Instant>) {
        ctx.statement().setTimestamp(ctx.index(), ctx.convert(converter()).value())
    }
}
