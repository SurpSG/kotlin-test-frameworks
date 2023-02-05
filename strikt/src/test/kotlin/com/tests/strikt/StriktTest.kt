package com.tests.strikt

import com.tests.code.ACTUAL_STRING
import com.tests.code.stringToTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.hasLength
import strikt.assertions.isBlank
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isNotEqualTo
import strikt.assertions.matches
import strikt.assertions.startsWith

class StriktTest {

    @Nested
    inner class StringTest {
        @Test
        fun `string value test`() {
            val actualValue: String = stringToTest()

            expectThat(actualValue)
                .isEqualTo(ACTUAL_STRING)
                .hasLength(ACTUAL_STRING.length)

            expectThat(actualValue) {
                isEqualTo("aaaaa")
                isNotEqualTo(ACTUAL_STRING)
                hasLength(35)
                isBlank()
                isEmpty()
                matches(Regex("[\\w]+"))
                startsWith("T")
            }
        }
    }

}
