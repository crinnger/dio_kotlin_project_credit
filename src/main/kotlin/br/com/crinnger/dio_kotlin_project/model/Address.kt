package br.com.crinnger.dio_kotlin_project.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable


@Embeddable
data class Address(
        @Column(nullable = false)
      var zip: String="",
      @Column(nullable = false)
      var street: String=""
)
