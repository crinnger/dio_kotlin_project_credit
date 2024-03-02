package br.com.crinnger.dio_kotlin_project.repository

import br.com.crinnger.dio_kotlin_project.model.Customer
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository: JpaRepository<Customer,Long>