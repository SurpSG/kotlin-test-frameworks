package com.tests.kotest

import com.tests.code.EMPLOYEES
import com.tests.code.EMPLOYEE_ALICE
import com.tests.code.EMPLOYEE_JOHN
import com.tests.code.Employee
import com.tests.code.LIST_1_2_3
import com.tests.code.MAP_EMPLOYEES
import com.tests.code.STRING_VALUE_TO_TEST
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldBeSorted
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldEndWith
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.collections.shouldHaveElementAt
import io.kotest.matchers.collections.shouldHaveSingleElement
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.collections.shouldStartWith
import io.kotest.matchers.comparables.beEqualComparingTo
import io.kotest.matchers.comparables.shouldBeEqualComparingTo
import io.kotest.matchers.comparables.shouldNotBeEqualComparingTo
import io.kotest.matchers.ints.shouldBeExactly
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.ints.shouldBeGreaterThanOrEqual
import io.kotest.matchers.ints.shouldBeLessThan
import io.kotest.matchers.ints.shouldBePositive
import io.kotest.matchers.maps.containAnyValues
import io.kotest.matchers.maps.haveValue
import io.kotest.matchers.maps.haveValues
import io.kotest.matchers.maps.shouldBeEmpty
import io.kotest.matchers.maps.shouldContain
import io.kotest.matchers.maps.shouldContainKey
import io.kotest.matchers.maps.shouldContainKeys
import io.kotest.matchers.maps.shouldHaveSize
import io.kotest.matchers.maps.shouldNotBeEmpty
import io.kotest.matchers.maps.shouldNotContainKey
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.string.beEmpty
import io.kotest.matchers.string.shouldBeBlank
import io.kotest.matchers.string.shouldBeEmpty
import io.kotest.matchers.string.shouldBeEqualIgnoringCase
import io.kotest.matchers.string.shouldBeUpperCase
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.matchers.string.shouldMatch
import io.kotest.matchers.string.shouldNotBeEmpty
import io.kotest.matchers.string.shouldStartWith
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.matchers.throwable.shouldNotHaveCause
import io.kotest.matchers.throwable.shouldNotHaveMessage
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.matchers.types.shouldBeTypeOf
import io.kotest.matchers.types.shouldNotBeSameInstanceAs

// Kotest IDEA plugin should be installed
// https://kotest.io/docs/framework/testing-styles.html#expect-spec
// https://kotest.io/docs/assertions/core-matchers.html
class KotestTest : AnnotationSpec() {

    // @Nested - doesn't work https://github.com/kotest/kotest/issues/3103

    // GeneralConceptsTest
    @Test
    fun `incomplete assertion`() {
        assertSoftly(STRING_VALUE_TO_TEST) {
            // it passes
        }
    }

    @Test
    fun `infix assertions`() {
        1.shouldBeExactly(1)
        1 shouldBeExactly 1

        1 should beEqualComparingTo(1)
    }

    @Test
    fun `assertions with chain`() {
        1
            .shouldBeLessThan(2)
            .shouldBeGreaterThan(0)
            .shouldBePositive()
    }

    @Test
    fun `compare with incompatible type`() {
        1 shouldBe "1"

        // 1 shouldBeEqualComparingTo "1" // not compile

        1.shouldBeTypeOf<String>().shouldBe("1")
    }

    @Test
    fun `soft assert`() {
        assertSoftly(1) {
            shouldNotBeEqualComparingTo(0)
            shouldBe(2) // prints clickable stacktrace frame to failed assertion
            shouldBeGreaterThanOrEqual(8)
        }

        assertSoftly {
            1 shouldNotBeEqualComparingTo 0
            2 shouldBe 2
            3 shouldBeGreaterThanOrEqual 8
        }
    }

    // StringTest
    @Test
    fun `general string assertions`() {
        assertSoftly(STRING_VALUE_TO_TEST) {
            shouldBeEqualComparingTo("aaaaa")
            shouldNotBeEqualComparingTo(STRING_VALUE_TO_TEST)
            shouldMatch("[\\w\\s]+")
            shouldBeEqualIgnoringCase("ffffff")
            shouldStartWith("BBB")
        }
    }

    @Test
    fun `string meta`() {
        assertSoftly(STRING_VALUE_TO_TEST) {
            shouldHaveLength(35)
            shouldBeBlank()
            shouldBeEmpty()
            shouldNotBeEmpty()
            shouldBeNull()
            shouldBeUpperCase<String>()
        }
    }

    // CollectionTests
    @Test
    fun `collection meta test`() {
        assertSoftly(LIST_1_2_3) {
            shouldBeEmpty()
            shouldNotBeEmpty()
            shouldHaveSingleElement(0)

            shouldHaveSize(3)
            count().shouldBeExactly(3)
            count { it < 0 }.shouldBeExactly(0)

            shouldBeSorted()
        }
    }

    @Test
    fun `collection elements test`() {
        assertSoftly(LIST_1_2_3) {
            shouldHaveElementAt(0, 1)
            filter { it == 1 }.shouldHaveSingleElement(1)

            shouldStartWith(1)
            shouldEndWith(3)
        }
    }

    @Test
    fun `elements by filter`() {
        assertSoftly(LIST_1_2_3) {
            filter { it > 0 } shouldHaveSize 3 // all match
            filter { it > 0 } shouldHaveAtLeastSize 1 // at least one match
            filter { it < 0 } shouldHaveSize 0 // none match

            filterIsInstance<Int>().shouldHaveSize(3)
        }
    }

    @Test
    fun `assertions with transforming`() {
        assertSoftly(EMPLOYEES) {
            flatMap { employee -> employee.department.location.phones }.shouldHaveSize(4)
            map { employee -> employee.name }.shouldContainExactlyInAnyOrder("Alice", "John")
        }
    }

    // MapTests
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
            shouldContainKey(1)
            shouldContainKeys(1, 2)
            shouldNotContainKey(1)
        }
    }

    @Test
    fun `assert values`() {
        assertSoftly(MAP_EMPLOYEES) {
            containAnyValues(EMPLOYEE_JOHN, EMPLOYEE_ALICE)
            haveValue(EMPLOYEE_JOHN)
            haveValues(EMPLOYEE_JOHN)
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

    // ObjectsTests
    @Test
    fun `object`() {
        assertSoftly(EMPLOYEE_ALICE) {
            shouldBe(EMPLOYEE_ALICE)
            shouldNotBeNull()
            shouldBeTypeOf<Employee>()
            shouldNotBeSameInstanceAs(EMPLOYEES)
        }
    }

    @Test
    fun `object properties`() {
        assertSoftly(EMPLOYEE_ALICE) {
            name shouldBe "Alice"
            department.name shouldNot beEmpty()

            assertSoftly(department) {
                name shouldBe "Sales"
                location.phones.shouldNotBeEmpty()
            }
        }
    }

    // ExceptionsTests
    @Test
    fun `expect throws`() {
        val throwableCode: () -> Unit = { throw IllegalArgumentException("a") }

        shouldThrowExactly<IllegalArgumentException>(throwableCode).should { ex ->
            ex.shouldHaveMessage("a")
            ex.shouldNotHaveMessage("b")
            ex.shouldHaveMessage(Regex("a+"))

            ex.message.shouldNotBeEmpty()

            ex.shouldNotHaveCause()
        }
    }

    @Test
    fun `expect no exceptions`() {
        shouldNotThrow<Throwable> { "I throw nothing" }
    }

    @Test
    fun `expect catching exception`() {
        shouldThrow<Exception> { throw IllegalStateException() }.should {
            it.shouldBeInstanceOf<IllegalStateException>()
        }
    }
}
