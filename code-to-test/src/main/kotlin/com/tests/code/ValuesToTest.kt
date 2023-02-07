package com.tests.code

const val STRING_VALUE_TO_TEST = "string value to test"

val LIST_1_2_3 = listOf(1, 2, 3)

val EMPLOYEE_JOHN = Employee(
    name = "John",
    department = Department(
        name = "R&D",
        location = Location("Kyiv", listOf("3", "4"))
    )
)

val EMPLOYEE_ALICE = Employee(
    name = "Alice",
    department = Department(
        name = "Sales",
        location = Location("Irpin", listOf("1", "2"))
    )
)

val EMPLOYEES = listOf(EMPLOYEE_JOHN, EMPLOYEE_ALICE)

val MAP_EMPLOYEES = mapOf(1 to EMPLOYEE_JOHN, 2 to EMPLOYEE_ALICE)

data class Location(
    val value: String,
    val phones: List<String>
)

data class Department(
    val name: String,
    val location: Location,
)

data class Employee(
    val name: String,
    val department: Department
)

