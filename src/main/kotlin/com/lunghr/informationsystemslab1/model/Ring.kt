
package com.lunghr.informationsystemslab1.model

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
import jakarta.validation.constraints.Positive

@Entity
@Table(name = "rings")
class Ring(
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "name", nullable = false)
    @NotBlank
    var name: String,

    @Column(name = "weight", nullable = false)
    @Positive
    var weight: Int = 0,

    @Column(name = "ownerless", nullable = false)
    var ownerless: Boolean = true,

    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User
)
