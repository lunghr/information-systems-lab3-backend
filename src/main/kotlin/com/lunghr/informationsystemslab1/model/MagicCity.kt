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
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

@Entity
@Table(name = "magic_cities")
class MagicCity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank
    var name: String,

    @Column(name = "area", nullable = false)
    @NotNull
    @Positive
    var area: Double,

    @Column(name = "population")
    @Positive
    var population: Int,

    @Column(name = "established_data")
    val establishedData: java.time.LocalDateTime,

    @Column(name = "governor", nullable = true)
    @Enumerated(EnumType.STRING)
    var governor: BookCreatureType,

    @Column(name = "capital")
    var capital: Boolean,

    @Column(name = "population_density")
    @Positive
    var populationDensity: Double,

    @OneToMany(mappedBy = "creatureLocation")
    var creatures: List<BookCreature> = emptyList(),

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User
)
