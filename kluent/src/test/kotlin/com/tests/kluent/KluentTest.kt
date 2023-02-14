package com.tests.kluent

import com.tests.code.EMPLOYEES
import com.tests.code.EMPLOYEE_ALICE
import com.tests.code.EMPLOYEE_JOHN
import com.tests.code.Employee
import com.tests.code.LIST_1_2_3
import com.tests.code.MAP_EMPLOYEES
import com.tests.code.STRING_VALUE_TO_TEST
import org.amshove.kluent.assertSoftly
import org.amshove.kluent.`should be equal to`
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeBlank
import org.amshove.kluent.shouldBeEmpty
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeEqualToIgnoringCase
import org.amshove.kluent.shouldBeEquivalentTo
import org.amshove.kluent.shouldBeGreaterOrEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldBeNullOrBlank
import org.amshove.kluent.shouldBeNullOrEmpty
import org.amshove.kluent.shouldBeSortedAccordingTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldContainAny
import org.amshove.kluent.shouldContainNone
import org.amshove.kluent.shouldHaveKey
import org.amshove.kluent.shouldHaveSingleItem
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldHaveTheSameClassAs
import org.amshove.kluent.shouldHaveValue
import org.amshove.kluent.shouldMatch
import org.amshove.kluent.shouldNotBeEmpty
import org.amshove.kluent.shouldNotBeEqualTo
import org.amshove.kluent.shouldNotBeEquivalentTo
import org.amshove.kluent.shouldNotBeNull
import org.amshove.kluent.shouldNotBeNullOrBlank
import org.amshove.kluent.shouldNotHaveKey
import org.amshove.kluent.shouldNotThrow
import org.amshove.kluent.shouldStartWith
import org.amshove.kluent.shouldThrow
import org.amshove.kluent.with
import org.amshove.kluent.withMessage
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class KluentTest {

    @Nested
    inner class GeneralConceptsTest {

        @Test
        fun `infix assertions`() {
            1 shouldBeEqualTo 1

            "hello" `should be equal to` "hello"
        }

        @Test
        fun `assertions with chain`() {
            1.shouldNotBeEqualTo(2).shouldBeGreaterOrEqualTo(8)
        }

        @Test
        fun `compare with incompatible type`() {
            1 shouldBeEqualTo "1"

            1.shouldBeInstanceOf<String>().shouldBeEqualTo("1")
        }

        @Test
        fun `soft assertion`() {
            assertSoftly(1) {
                shouldBeEqualTo(2) // prints clickable stacktrace frame to failed assertion
                shouldBeEqualTo(3) // as well
            }

            assertSoftly {
                1 shouldBeEqualTo 2
                1 shouldBeEqualTo 3
            }
        }

        @Test
        fun `incomplete assertion`() {
            assertSoftly {
                // it passes
            }
        }
    }

    @Nested
    inner class StringTest {
        @Test
        fun `assertions with context(AKA soft assertion)`() {
            assertSoftly(STRING_VALUE_TO_TEST) {
                shouldBe("aaaaa")
                shouldNotBeEqualTo(STRING_VALUE_TO_TEST)
                shouldMatch("[\\w\\s]+")
                shouldBeEqualToIgnoringCase("ffffff")
                shouldStartWith("BBB")
            }
        }

        @Test
        fun `string meta`() {
            assertSoftly(STRING_VALUE_TO_TEST) {
                length shouldBeEqualTo 35
                shouldBeBlank()
                shouldBeEmpty()
                shouldNotBeEmpty()
                shouldBeNull()
                shouldBeNullOrBlank()
                shouldBeNullOrEmpty()
                // shouldBeUpperCase<String>() no such assertion
            }
        }
    }

    @Nested
    inner class CollectionTests {
        @Test
        fun `collection meta test`() {
            assertSoftly(LIST_1_2_3) {
                shouldBeEmpty()
                shouldNotBeEmpty()

                shouldHaveSingleItem().shouldBeEqualTo(0)

                shouldHaveSize(3)
                count().shouldBeEqualTo(0)
                count { it < 0 }.shouldBeEqualTo(0)

                shouldBeSortedAccordingTo(naturalOrder())
            }
        }

        @Test
        fun `collection elements test`() {
            assertSoftly(LIST_1_2_3) {
                get(0) shouldBeEqualTo 1
                filter { it == 1 }.shouldHaveSingleItem().shouldBeEqualTo(0)

                first().shouldBeEqualTo(1)
                last().shouldBeEqualTo(3)
            }
        }

        @Test
        fun `elements by filter`() {
            assertSoftly(LIST_1_2_3) {
                filter { it > 0 } shouldHaveSize 3 // all match
                shouldContainAny { it > 0 } // at least one match
                shouldContainNone { it < 0 } // none match

                filterIsInstance<Int>().shouldHaveSize(2)
            }
        }

        @Test
        fun `assertions with transforming`() {
            assertSoftly(EMPLOYEES) {
                flatMap { employee -> employee.department.location.phones } shouldHaveSize 4
                map { employee -> employee.name } shouldContainAll listOf("Alice", "John") // infix is the only way for such assertion
            }
        }
    }

    @Nested
    inner class MapTests {
        @Test
        fun `meta test`() {
            assertSoftly(MAP_EMPLOYEES) {
                shouldBeEmpty()
                shouldNotBeEmpty()
                shouldHaveSize(2)
            }
        }

        @Test
        fun `keys test`() {
            assertSoftly(MAP_EMPLOYEES) {
                shouldHaveKey(1)
                keys shouldContainAll listOf(1, 2)
                shouldNotHaveKey(1)
            }
        }

        @Test
        fun `assert values`() {
            assertSoftly(MAP_EMPLOYEES) {
                shouldHaveValue(EMPLOYEE_JOHN)

                values shouldContainAll listOf(EMPLOYEE_JOHN, EMPLOYEE_ALICE)
            }
        }

        @Test
        fun `value by key`() {
            assertSoftly(MAP_EMPLOYEES) {
                get(0).shouldBeNull()
                getValue(1) shouldBe EMPLOYEE_JOHN
            }
        }

        @Test
        fun `entry test`() {
            MAP_EMPLOYEES.shouldContain(1 to EMPLOYEE_ALICE)
        }
    }

    @Nested
    inner class ObjectsTests {
        @Test
        fun `object`() {
            assertSoftly(EMPLOYEE_ALICE) {
                shouldBe(EMPLOYEE_ALICE)
                shouldNotBeNull()
                shouldBeInstanceOf<Employee>()
                shouldHaveTheSameClassAs(EMPLOYEES)
            }
        }

        @Test
        fun `object properties`() {
            assertSoftly(EMPLOYEE_ALICE) {
                name shouldBe "Alice"
                department.name.shouldNotBeEmpty()

                assertSoftly(department) {
                    name shouldBe "Sales"
                    location.phones.shouldNotBeEmpty()
                }
            }
        }

        @ExperimentalStdlibApi
        @Test
        fun `compare by data`() {
            class NoEqualsMethodClass(val a: Int, val b: String, val c: Double)

            val instance1 = NoEqualsMethodClass(1, "2", 3.1)
            val instance2 = NoEqualsMethodClass(1, "2", 3.2)

            assertSoftly {
                instance1.shouldBeEqualTo(instance2)

                instance1.shouldNotBeEquivalentTo(instance2)

                instance1.shouldBeEquivalentTo(instance2)
                instance1.shouldBeEquivalentTo(instance2) {
                    it.excluding(NoEqualsMethodClass::c)
                }
            }
        }
    }

    @Nested
    inner class ExceptionsTests {
        @Test
        fun `expect throws`() {
            val throwableCode: () -> Unit = { throw IllegalArgumentException("a") }

            throwableCode.shouldThrow(IllegalArgumentException::class)
                .withMessage("a")

            throwableCode.shouldThrow(IllegalArgumentException::class)
                .with {
                    message.shouldNotBeNullOrBlank()
                    cause.shouldBeNull()
                }
        }

        @Test
        fun `expect no exceptions`() {
            { "I throw nothing" }.shouldNotThrow(IllegalArgumentException::class)
        }
    }
}
