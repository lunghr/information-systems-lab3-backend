package com.lunghr.informationsystemslab1.import.model

import com.lunghr.informationsystemslab1.auth.model.ent.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.sql.Timestamp
import java.time.LocalDateTime

@Entity
@Table(name = "file_stats")
class FileStats(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User,

    @Column(name = "originalFilename")
    var originalFilename: String?,

    @Column(name = "storedFilename")
    var storedFilename: String?,

    @Column(name = "additions")
    @NotNull
    @Positive
    var additions: Long,

    @Column(name = "finished")
    @NotNull
    var finished: Boolean,

    @Column(name = "timestamp")
    @NotNull
    var timestamp: LocalDateTime = LocalDateTime.now()
)
