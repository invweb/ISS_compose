package com.example.iss.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AstroPeopleParsingTest {

    @Test
    fun `AstroPeopleResponse should parse valid data`() {
        val response = AstroPeopleResponse(
            people = listOf(
                AstroPerson("Mark Vande Hei", "ISS"),
                AstroPerson("Walter Villadei", "ISS")
            ),
            number = 2,
            message = "success"
        )
        assertNotNull(response)
        assertEquals(2, response.people.size)
        assertEquals(2, response.number)
        assertEquals("success", response.message)
    }

    @Test
    fun `AstroPerson should have name and craft`() {
        val person = AstroPerson("Sergey Korsakov", "ISS")
        assertEquals("Sergey Korsakov", person.name)
        assertEquals("ISS", person.craft)
    }

    @Test
    fun `people should be groupable by craft`() {
        val people = listOf(
            AstroPerson("Alice", "ISS"),
            AstroPerson("Bob", "ISS"),
            AstroPerson("Charlie", "Tiangong")
        )
        val grouped = people.groupBy { it.craft }
        assertEquals(2, grouped.size)
        assertEquals(2, grouped["ISS"]!!.size)
        assertEquals(1, grouped["Tiangong"]!!.size)
    }

    @Test
    fun `empty people list should be handled`() {
        val response = AstroPeopleResponse(
            people = emptyList(),
            number = 0,
            message = "success"
        )
        assertTrue(response.people.isEmpty())
        assertEquals(0, response.number)
    }
}
