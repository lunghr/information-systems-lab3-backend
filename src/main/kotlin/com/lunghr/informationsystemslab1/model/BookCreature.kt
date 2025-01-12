package com.lunghr.informationsystemslab1.model

import com.lunghr.informationsystemslab1.auth.model.ent.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

@Entity
@Table(name = "book_creatures")
class BookCreature(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long,

    @Column(name = "name", nullable = false)
    @NotBlank
    private var name: String,

    @OneToOne
    @JoinColumn(name = "coordinates_id", nullable = false)
    private var coordinates: Coordinates,

    @Column(name = "creation_date", nullable = false)
    private val creationDate: java.time.ZonedDateTime,

    @Column(name = "age", nullable = true)
    @Positive
    private var age: Int,

    @Column(name = "creature_type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotBlank
    private var creatureType: BookCreatureType,

    @ManyToOne
    @JoinColumn(name = "creature_location", nullable = false)
    @NotNull
    private var creatureLocation: MagicCity,

    @ManyToOne
    @JoinColumn(name = "ring", nullable = false)
    @NotNull
    private var ring: Ring,

    @Column(name = "attack_level", nullable = true)
    @Positive
    private var attackLevel: Float,

    @ManyToOne
    @JoinColumn(name = "user_id")
    private var user: User
)
