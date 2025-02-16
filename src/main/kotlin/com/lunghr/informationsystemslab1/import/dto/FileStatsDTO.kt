package com.lunghr.informationsystemslab1.import.dto

import java.time.LocalDateTime

data class FileStatsDTO (
    var id: Long = 0,
    var initiator: String? = null,
    var filename: String? = null,
    var additions: Long = 0,
    var finished: Boolean = false,
    var timestamp: LocalDateTime? = null
)