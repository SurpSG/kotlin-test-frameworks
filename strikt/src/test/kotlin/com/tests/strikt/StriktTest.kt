package com.tests.strikt

import com.tests.code.EMPLOYEES
import com.tests.code.EMPLOYEE_ALICE
import com.tests.code.EMPLOYEE_JOHN
import com.tests.code.Employee
import com.tests.code.LIST_1_2_3
import com.tests.code.MAP_EMPLOYEES
import com.tests.code.STRING_VALUE_TO_TEST
import jdk.dynalink.linker.support.Guards.isNotNull
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.all
import strikt.assertions.any
import strikt.assertions.atLeast
import strikt.assertions.atMost
import strikt.assertions.contains
import strikt.assertions.containsExactlyInAnyOrder
import strikt.assertions.containsKey
import strikt.assertions.containsKeys
import strikt.assertions.count
import strikt.assertions.doesNotContainKey
import strikt.assertions.exactly
import strikt.assertions.filter
import strikt.assertions.filterIsInstance
import strikt.assertions.first
import strikt.assertions.flatMap
import strikt.assertions.get
import strikt.assertions.getValue
import strikt.assertions.hasEntry
import strikt.assertions.hasLength
import strikt.assertions.hasSize
import strikt.assertions.isA
import strikt.assertions.isBlank
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isEqualToIgnoringCase
import strikt.assertions.isFailure
import strikt.assertions.isGreaterThan
import strikt.assertions.isGreaterThanOrEqualTo
import strikt.assertions.isLessThan
import strikt.assertions.isLessThanOrEqualTo
import strikt.assertions.isNotEmpty
import strikt.assertions.isNotEqualTo
import strikt.assertions.isNotNull
import strikt.assertions.isNull
import strikt.assertions.isNullOrBlank
import strikt.assertions.isNullOrEmpty
import strikt.assertions.isSameInstanceAs
import strikt.assertions.isSorted
import strikt.assertions.isSuccess
import strikt.assertions.isUpperCase
import strikt.assertions.last
import strikt.assertions.length
import strikt.assertions.map
import strikt.assertions.matches
import strikt.assertions.message
import strikt.assertions.none
import strikt.assertions.single
import strikt.assertions.startsWith
import strikt.assertions.withElementAt
import strikt.assertions.withFirst
import strikt.assertions.withValue

class StriktTest {

    @Nested
    inner class GeneralConceptsTest {

        @Test
        fun `infix assertions`() {
            expectThat(1) isNotEqualTo 1
        }

        @Test
        fun `assertions with chain`() {
            expectThat(1)
                .isNotEqualTo(2)
                .isGreaterThanOrEqualTo(8)
        }

        @Test
        fun `reverse assertions`() {
            expectThat(1)
                .not()
                .isEqualTo(2)
                .isGreaterThan(8)
        }

        @Test
        fun `assertions with context(AKA soft assertion)`() {
            expectThat(1) {
                isNotEqualTo(0)
                isEqualTo(2)
                isGreaterThanOrEqualTo(8)
            }
        }

        @Test
        fun `incomplete assertion`() {
            expectThat(1) { }
        }
    }

    @Nested
    inner class StringTest {
        @Test
        fun `assertions with context(AKA soft assertion)`() {
            expectThat(STRING_VALUE_TO_TEST) {
                isEqualTo("aaaaa")
                isNotEqualTo(STRING_VALUE_TO_TEST)
                matches(Regex("[\\w\\s]+"))
                isEqualToIgnoringCase("ffffff")
                startsWith("BBB")
            }
        }

        @Test
        fun `string meta`() {
            expectThat(STRING_VALUE_TO_TEST) {
                hasLength(35)
                isBlank()
                isEmpty()
                isNotEmpty()
                isNullOrBlank()
                isNullOrEmpty()
                isUpperCase()
            }
        }
    }

    @Nested
    inner class CollectionTests {
        @Test
        fun `collection meta test`() {
            expectThat(LIST_1_2_3) {
                isNotEmpty()
                single().isEqualTo(0)
                count().isEqualTo(3)
                count("less then 0") { it < 0 }.isEqualTo(0)
                isSorted()
            }
        }

        @Test
        fun `collection elements test`() {
            expectThat(LIST_1_2_3) {
                first().isEqualTo(1)
                first { it == 1 }.isEqualTo(1)
                withFirst {
                    isLessThan(2)
                }

                this[0].isEqualTo(1)
                withElementAt(0) {
                    isLessThanOrEqualTo(2)
                }

                last().isEqualTo(3)
            }
        }

        @Test
        fun `elements by filter`() {
            expectThat(LIST_1_2_3) {
                all { isGreaterThan(0) }
                any { isGreaterThan(0) }
                none { isLessThan(1) }
                atLeast(1) { isGreaterThan(0) }
                atMost(3) { isGreaterThan(0) }
                exactly(3) { isGreaterThan(0) }

                filterIsInstance<Int>().count().isEqualTo(3)
                filter { int -> int > 0 }.count().isEqualTo(3)
            }
        }

        @Test
        fun `assertions with transforming`() {
            expectThat(EMPLOYEES) {
                flatMap { employee -> employee.department.location.phones }.count().isEqualTo(4)
                map { employee -> employee.name }.containsExactlyInAnyOrder("Alice", "John")
            }
        }
    }

    @Nested
    inner class MapTests {
        @Test
        fun `meta test`() {
            expectThat(MAP_EMPLOYEES) {
                isEmpty()
                isNotEmpty()
                hasSize(2)
            }
        }

        @Test
        fun `keys test`() {
            expectThat(MAP_EMPLOYEES) {
                containsKey(1)
                containsKeys(1, 2)
                doesNotContainKey(3)
            }
        }

        @Test
        fun `value by key`() {
            expectThat(MAP_EMPLOYEES) {
                get(0).isNull()
                getValue(1).isEqualTo(EMPLOYEE_JOHN)
                withValue(2) {
                    isEqualTo(EMPLOYEE_ALICE)
                }
            }
        }

        @Test
        fun `entry test`() {
            expectThat(MAP_EMPLOYEES).hasEntry(1, EMPLOYEE_ALICE)
        }
    }

    @Nested
    inner class ObjectsTests {
        @Test
        fun `object`() {
            expectThat(EMPLOYEE_ALICE) {
                isEqualTo(EMPLOYEE_ALICE)
                isNotNull()
                isA<Employee>()
                isSameInstanceAs(EMPLOYEE_ALICE)
            }
        }

        @Test
        fun `fields`() {
            expectThat(EMPLOYEE_ALICE) {
                get { name }.isEqualTo("Alice")
                get { department.name }.isNotEmpty()

                with({ department }) {
                    get { name }.isEqualTo("Sales")
                    get { location.phones }.isNotEmpty()
                }
            }
        }
    }

    @Nested
    inner class ExceptionsTests {
        @Test
        fun `expect throws`() {
            val throwableCode: () -> Unit = { throw IllegalArgumentException("a") }

            expectThrows<IllegalArgumentException> { throwableCode() }
                .message.isNotNull().contains("a")

            expectThrows<IllegalArgumentException> { throwableCode() }
                .with({ message }) {
                    isNotNull().length.isGreaterThan(0)
                }
                .with({ cause }) {
                    isNull()
                }
        }

        @Test
        fun `expect no exceptions`() {
            expectCatching { "I throw nothing" }
                .isSuccess()
                .contains("nothing")
        }

        @Test
        fun `expect catching exception`() {
            expectCatching { throw IllegalStateException() }
                .isFailure()
                .isA<IllegalStateException>()
                .message.isNull()
        }
    }
}
