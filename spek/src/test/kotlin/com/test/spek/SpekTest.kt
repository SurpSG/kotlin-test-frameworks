package com.test.spek

import org.spekframework.spek2.Spek

object SpekTest: Spek({
    group("String tests\uD83D\uDCA9") {
        test("basic test") {
        }

        group("Emptiness checks") {
            test("sub") {
                throw RuntimeException()
            }
        }
    }
})
