package com.app.ynvest_tube.model.internal

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

class RelativeDate(dateStr: String) {

    private val date: ZonedDateTime

    init {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val serverDateTime = LocalDateTime.parse(dateStr, dateTimeFormatter)
        val zonedDateTime = serverDateTime.atZone(ZoneId.of("UTC"))
        date = zonedDateTime.withZoneSameInstant(TimeZone.getDefault().toZoneId())
    }

    val reprRelativeToNow: String
        get() {
            val currentDate = ZonedDateTime.now()

            val dateStart = date.truncatedTo(ChronoUnit.DAYS)
            val currentDateStart = currentDate.truncatedTo(ChronoUnit.DAYS)
            val daysDelta = ChronoUnit.DAYS.between(currentDateStart, dateStart)

            val reprStart = when (daysDelta) {
                0L -> {
                    ""
                }
                1L -> {
                    "tomorrow "
                }
                -1L -> {
                    "yesterday "
                }
                else -> {
                    val day = date.dayOfMonth.toString().padStart(2, '0')
                    val month = date.month.toString().padStart(2, '0')
                    val year = date.year.toString().padStart(4, '0')
                    "${day}-${month}-${year} "
                }
            }
            val hour = date.hour.toString().padStart(2, '0')
            val minute = date.minute.toString().padStart(2, '0')
            val reprEnd = "${hour}:${minute}"

            return reprStart + reprEnd
        }

    val timeLeft: String
        get() {
            val currentDate = ZonedDateTime.now()
            val secondsLeft = ChronoUnit.SECONDS.between(currentDate, date)

            return when {
                secondsLeft >= 60*60*24 -> {
                    "${secondsLeft / (60*60*24)}d"
                }
                secondsLeft >= 60*60 -> {
                    "${secondsLeft / (60*60)}h"
                }
                secondsLeft >= 60 -> {
                    "${secondsLeft / 60}m"
                }
                secondsLeft > 0 -> {
                    "${secondsLeft}s"
                }
                else -> {
                    "0s"
                }
            }
        }
}