package com.tests.kluent

import com.tests.code.ACTUAL_STRING
import com.tests.code.stringToTest
import org.amshove.kluent.`should be instance of`
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldContainNone
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class KluentTest {

    @Nested
    inner class StringTest {
        @Test
        fun `string value test`() {
            val actualValue: String = stringToTest()

            actualValue shouldBeEqualTo ACTUAL_STRING

            actualValue.shouldBeEqualTo("aaa")
                .shouldContainNone("a", "b", "c")
                .shouldBeNull()
        }
    }

}
