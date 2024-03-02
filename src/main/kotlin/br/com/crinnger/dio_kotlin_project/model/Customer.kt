package br.com.crinnger.dio_kotlin_project.model

import jakarta.persistence.*
import lombok.EqualsAndHashCode
import lombok.ToString
import java.math.BigDecimal

@Entity
@EqualsAndHashCode
@ToString
data class Customer(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long?=null,

        @Column(nullable = false)
        var firstName:String="",

        @Column(nullable = false)
        var lastName:String="",

        @Column(nullable = false, unique = true)
        var cpf:String="",

        @Column(nullable = false, unique = true)
        var email:String="",

        var income: BigDecimal=BigDecimal.ZERO,

        @Column(nullable = false)
        var password:String="",

        @Embedded
        @Column(nullable = false)
        var address: Address=Address(),

        @OneToMany(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.REMOVE), mappedBy = "customer")
        @ToString.Exclude
        @EqualsAndHashCode.Exclude
        var credits: List<Credit> = mutableListOf()
)
