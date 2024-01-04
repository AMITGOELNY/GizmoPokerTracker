@file:Suppress("ktlint")
package com.ghn.database

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinLocalDate
import org.jooq.BindingGetResultSetContext
import org.jooq.BindingSetStatementContext
import org.jooq.Converter
import org.jooq.impl.AbstractBinding
import java.sql.Date

@Suppress("UNUSED")
private class JooqLocalDateConverter : Converter<Date, LocalDate> {
    override fun fromType(): Class<java.sql.Date> = java.sql.Date::class.java

    override fun toType(): Class<LocalDate> = LocalDate::class.java

    override fun from(databaseObject: java.sql.Date?): LocalDate? {
        return databaseObject?.toLocalDate()?.toKotlinLocalDate()
    }

    override fun to(userObject: LocalDate?): java.sql.Date? {
        return userObject?.toJavaLocalDate()?.run(java.sql.Date::valueOf)
    }
}

@Suppress("UNUSED")
class JooqLocalDateBinding : AbstractBinding<Date, LocalDate>() {
    private val convert = JooqLocalDateConverter()

    override fun converter(): Converter<Date, LocalDate> {
        return convert
    }

    override fun get(ctx: BindingGetResultSetContext<LocalDate>) {
        ctx.convert(converter()).value(ctx.resultSet().getDate(ctx.index()))
    }

    override fun set(ctx: BindingSetStatementContext<LocalDate>) {
        ctx.statement().setDate(ctx.index(), ctx.convert(converter()).value())
    }
}
