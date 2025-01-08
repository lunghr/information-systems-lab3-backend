package com.lunghr.informationsystemslab1.model

import com.lunghr.informationsystemslab1.auth.model.ent.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "magic_cities")
class MagicCity(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @Column(name = "name", nullable = false)
    @NotBlank
    var name: String,

    @Column(name = "population")
    @Min(1)
    var population: Int,

    @Column(name = "area")
    @Min(1)
    var area: Double,

    @Column(name = "established_data")
    val establishedData: java.time.LocalDateTime,

    @Column(name = "governor", nullable = false)
    var governor: BookCreatureType,

    @Column(name = "capital")
    var capital: Boolean,

    @Column(name = "population_density")
    @Min(1)
    var populationDensity: Double,

    @OneToMany(mappedBy = "creatureLocation")
    var creatures: List<BookCreature>,

    @ManyToOne
    @JoinColumn(name = "user_id")
    private var user: User
)
