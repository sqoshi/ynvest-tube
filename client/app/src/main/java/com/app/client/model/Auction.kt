package com.app.client.model

import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

data class Auction(val id: Int,
                   val video: Video,
                   val auction_expiration_date: String)
