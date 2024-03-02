package br.com.crinnger.dio_kotlin_project.model

import br.com.crinnger.dio_kotlin_project.enums.Status
import jakarta.persistence.*
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

@Entity
@EqualsAndHashCode
@ToString
data class Credit(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?=null,

        @Column(nullable = false, unique = true)
        val creditCode: UUID=UUID.randomUUID(),

        @Column(nullable = false)
        val creditValue: BigDecimal=BigDecimal.ZERO,

        @Column(nullable = false)
        val dayFirstInstallment: LocalDate,

        @Column(nullable = false)
        val numberInstallments: Int=0,

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        val status: Status = Status.IN_PROGRESS,

        @ManyToOne
        var customer: Customer?
        )
