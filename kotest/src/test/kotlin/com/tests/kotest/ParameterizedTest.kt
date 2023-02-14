package com.tests.kotest

import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.comparables.shouldBeLessThanOrEqualTo
import io.kotest.matchers.comparables.shouldNotBeEqualComparingTo
import io.kotest.matchers.string.shouldHaveLength
import io.kotest.property.checkAll
import io.kotest.property.forAll

class ParameterizedTests: StringSpec({

    "Random values generated. Boolean result" {
        forAll<String, String> { a, b ->
            println("a: $a, b: $b")
            (a + b).length == (a.length + b.length)
        }
    }

    "Random values generated. Simple assertion" {
        checkAll<String, String> { a, b ->
            println("a: $a, b: $b")
            (a + b) shouldHaveLength 25
        }
    }

    "Generate n times" {
        checkAll<Double, Double>(10_000) { a, b ->
            a shouldNotBeEqualComparingTo b
        }
    }

    "List of parameters" {
        listOf(
            'A', 'B', 'C'
        ).forAll {
            it.code shouldBeLessThanOrEqualTo 65
        }
    }

})
