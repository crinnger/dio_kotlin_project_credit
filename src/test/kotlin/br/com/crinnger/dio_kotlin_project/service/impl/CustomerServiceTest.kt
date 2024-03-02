package br.com.crinnger.dio_kotlin_project.service.impl

import br.com.crinnger.dio_kotlin_project.exception.BusinessException
import br.com.crinnger.dio_kotlin_project.model.Address
import br.com.crinnger.dio_kotlin_project.model.Customer
import br.com.crinnger.dio_kotlin_project.repository.CustomerRepository
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import java.util.Optional

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK lateinit var customerRepository: CustomerRepository
    @InjectMockKs lateinit var customerService: CustomerService

    @Test
    fun `should create customer`() {
        val fake=createCustomer()

        every { customerRepository.save(any()) }returns fake

        val actual=customerService.save(fake)
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isEqualTo(fake)
        verify (exactly = 1){ customerRepository.save(any()) }
    }

    @Test
    fun `should update customer`() {
        val fake=createCustomer()

        every { customerRepository.save(any()) }returns fake

        val actual=customerService.update(fake)
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isEqualTo(fake)
        verify (exactly = 1){ customerRepository.save(any()) }
    }

    @Test
    fun `should find customer`() {
        val fake=Optional.of(createCustomer())

        every { customerRepository.findById(any()) } returns fake

        val actual=customerService.findById(1)
        Assertions.assertThat(actual).isNotNull
        Assertions.assertThat(actual).isEqualTo(fake.get())
        verify (exactly = 1){ customerRepository.findById(any()) }
    }

    @Test
    fun `should throw error when not find customer`() {
        every { customerRepository.findById(any()) } returns Optional.empty()
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
                .isThrownBy { customerService.findById(1) }
                .withMessage("Id 1 not Found")
    }

    @Test
    fun `should delete when find customer`() {
        val fake=createCustomer()

        every { customerRepository.findById(any()) } returns Optional.of(fake)
        every { customerRepository.delete(any()) } just runs

        customerService.delete(1)
        verify (exactly = 1){ customerRepository.findById(any()) }
        verify (exactly = 1){ customerRepository.delete(any()) }
    }

    @Test
    fun `should throw error try delete and not find customer`() {
        every { customerRepository.findById(any()) } returns Optional.empty()
        every { customerRepository.delete(any()) } just runs
        Assertions.assertThatExceptionOfType(BusinessException::class.java)
                .isThrownBy { customerService.delete(1) }
                .withMessage("Id 1 not Found")
        verify (exactly = 1){ customerRepository.findById(any()) }
        verify (exactly = 0){ customerRepository.delete(any()) }
    }

    fun createCustomer() : Customer=
            Customer(firstName = "teste",
                    lastName = "tst",
                    cpf="111111111",
                    email = "teste@gmail.com",
                    password = "eerr",
                    income = BigDecimal.TEN,
                    id = 1L,
                    address = Address(zip = "54545", street = "teste"))
}