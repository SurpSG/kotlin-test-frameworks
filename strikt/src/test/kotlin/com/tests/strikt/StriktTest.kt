package com.tests.strikt

import com.tests.code.stringToTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.hasLength
import strikt.assertions.isEqualTo
import strikt.assertions.matches
import strikt.assertions.startsWith

class StriktTest {

    @Nested
    inner class StringTest {
        @Test
        fun `string value test`() {
            val actualValue: String = stringToTest()

            expectThat(actualValue) {
                hasLength(35)
                matches(Regex("[\\w]+"))
                startsWith("T")
            }
        }

        @Test
        fun `string value test chain`() {
            val actualValue: String = stringToTest()

            expectThat(actualValue)
                .isEqualTo("string value to test")
                .hasLength("string value to test".length)
                .matches(Regex(".*"))
                .startsWith("s")
        }

    }

}
