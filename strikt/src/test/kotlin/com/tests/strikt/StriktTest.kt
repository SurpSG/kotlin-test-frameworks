package com.tests.strikt

import com.tests.code.STRING_VALUE_TO_TEST
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
            val actualValue: String = STRING_VALUE_TO_TEST

            expectThat(actualValue)
                .isEqualTo(STRING_VALUE_TO_TEST)
                .hasLength(STRING_VALUE_TO_TEST.length)

            expectThat(actualValue) {
                isEqualTo("aaaaa")
                isNotEqualTo(STRING_VALUE_TO_TEST)
                hasLength(35)
                isBlank()
                isEmpty()
                matches(Regex("[\\w]+"))
                startsWith("T")
            }
        }
    }

}
