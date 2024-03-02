package br.com.crinnger.dio_kotlin_project.repository

import br.com.crinnger.dio_kotlin_project.model.Address
import br.com.crinnger.dio_kotlin_project.model.Credit
import br.com.crinnger.dio_kotlin_project.model.Customer
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CreditRepositoryTest {
    @Autowired lateinit var creditRepository: CreditRepository

    @Autowired lateinit var testEntityManager: TestEntityManager

    private lateinit var customer: Customer
    private lateinit var credit1:Credit
    private lateinit var credit2: Credit

    @BeforeEach
    fun setUp(){
        customer=testEntityManager.persist(createCustomer("teste@teste.com"))
        credit1=testEntityManager.persist(createCredit(customer, UUID.randomUUID()))
        credit2=testEntityManager.persist(createCredit(customer, UUID.randomUUID()))
    }

    @Test
    fun `should find credit by credit code`(){
        val creditResult1=creditRepository.findByCreditCode(credit1.creditCode)
        val creditResult2=creditRepository.findByCreditCode(credit2.creditCode)

        Assertions.assertThat(creditResult1).isNotNull
        Assertions.assertThat(creditResult2).isNotNull
        Assertions.assertThat(creditResult1).isEqualTo(credit1)
        Assertions.assertThat(creditResult2).isEqualTo(credit2)

    }

    @Test
    fun `should not find credit by credit code`(){
        val creditResult1=creditRepository.findByCreditCode(credit1.creditCode)
        val creditResult2=creditRepository.findByCreditCode(UUID.randomUUID())

        Assertions.assertThat(creditResult1).isNotNull
        Assertions.assertThat(creditResult2).isNull()
        Assertions.assertThat(creditResult1).isEqualTo(credit1)

    }

    @Test
    fun `should find credit by customer id`(){
        val creditResult1=creditRepository.findAllByCustomerId(customer.id!!)

        Assertions.assertThat(creditResult1).isNotNull
        Assertions.assertThat(creditResult1).isNotEmpty
        Assertions.assertThat(creditResult1.size).isEqualTo(2)
        Assertions.assertThat(creditResult1).contains(credit1,credit2)


    }

    @Test
    fun `should not find credit by customer id`(){
        val creditResult1=creditRepository.findAllByCustomerId(22)

        Assertions.assertThat(creditResult1).isNotNull
        Assertions.assertThat(creditResult1).isEmpty()
        Assertions.assertThat(creditResult1.size).isEqualTo(0)


    }

    fun createCredit(customer:Customer,creditCode: UUID): Credit=
        Credit(
                creditValue = BigDecimal.TEN,
                dayFirstInstallment = LocalDate.now(),
                numberInstallments = 1,
                creditCode = creditCode,
                customer = customer
        )

    fun createCustomer(email:String): Customer=
            Customer(
                    address = Address(zip = "teste", street = "tese"),
                    income = BigDecimal.TEN,
                    firstName = "teste",
                    lastName = "credit",
                    email = email,
                    password = "teste",
                    cpf = "11111111111"
            )

}