package com.tests.kluent

import com.tests.code.STRING_VALUE_TO_TEST
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
            STRING_VALUE_TO_TEST shouldBeEqualTo STRING_VALUE_TO_TEST

            STRING_VALUE_TO_TEST
                .shouldBeEqualTo("aaa")
                .shouldContainNone("a", "b", "c")
                .shouldBeNull()
        }
    }

}
