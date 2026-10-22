package com.example.iss.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class IssPositionParsingTest {

    @Test
    fun `parseValidLatitudeShouldReturnCorrectValue`() {
        val position = IssPosition(latitude = "51.8857", longitude = "-17.1251")
        assertEquals(51.8857, position.latitudeDouble(), 0.0001)
    }

    @Test
    fun `parseNegativeLatitudeShouldReturnCorrectValue`() {
        val position = IssPosition(latitude = "-34.5678", longitude = "19.1234")
        assertEquals(-34.5678, position.latitudeDouble(), 0.0001)
    }

    @Test
    fun `parseValidLongitudeShouldReturnCorrectValue`() {
        val position = IssPosition(latitude = "0.0", longitude = "179.9999")
        assertEquals(179.9999, position.longitudeDouble(), 0.0001)
    }

    @Test
    fun `parseInvalidLatitudeShouldReturnZero`() {
        val position = IssPosition(latitude = "invalid", longitude = "0.0")
        assertEquals(0.0, position.latitudeDouble(), 0.0001)
    }

    @Test
    fun `parseInvalidLongitudeShouldReturnZero`() {
        val position = IssPosition(latitude = "0.0", longitude = "not-a-number")
        assertEquals(0.0, position.longitudeDouble(), 0.0001)
    }

    @Test
    fun `responseShouldHaveNonNullFields`() {
        val response = IssPositionResponse(
            issPosition = IssPosition(latitude = "50.0", longitude = "30.0"),
            timestamp = 1234567890L,
            message = "success"
        )
        assertNotNull(response.issPosition)
        assertEquals(1234567890L, response.timestamp)
        assertEquals("success", response.message)
    }
}
