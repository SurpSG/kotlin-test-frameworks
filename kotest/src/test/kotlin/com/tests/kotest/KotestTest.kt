package com.tests.kotest

import com.tests.code.STRING_VALUE_TO_TEST
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldBeBlank
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith

class KotestTest : FunSpec() {

    init {
        test("string should start with x") {
            STRING_VALUE_TO_TEST shouldStartWith "x"
        }

        test("string chain") {
            STRING_VALUE_TO_TEST.shouldStartWith("x")
                .shouldBeBlank()
                .shouldContain("Ajax")
        }
    }


}
