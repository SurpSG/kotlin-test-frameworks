package com.tests.kotest

import com.tests.code.stringToTest
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldBeBlank
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith

class KotestTest : FunSpec() {

    init {
        test("string should start with x") {
            val actualValue = stringToTest()

            actualValue shouldStartWith "x"
        }

        test("string chain") {
            val actualValue = stringToTest()

            actualValue.shouldStartWith("x")
                .shouldBeBlank()
                .shouldContain("Ajax")
        }
    }


}
