
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
@Table(name = "rings")
class Ring(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @Column(name = "name", nullable = false)
    @NotBlank
    var name: String,

    @Column(name = "weight", nullable = false)
    @Min(1)
    var weight: Int,

    @OneToMany(mappedBy = "ring")
    var creatures: List<BookCreature>,

    @ManyToOne
    @JoinColumn(name = "user_id")
    private var user: User
)
