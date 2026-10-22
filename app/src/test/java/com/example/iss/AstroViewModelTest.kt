package com.example.iss.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AstroViewModelTest {

    @Test
    fun `ui state data class should have correct defaults`() {
        val state = AstroUiState()
        assertTrue("People list should be empty by default", state.people.isEmpty())
        assertEquals("Total should be 0 by default", 0, state.totalNumber)
        assertTrue("Loading should be true by default", state.isLoading)
        assertTrue("Error should be null by default", state.error == null)
    }

    @Test
    fun `ui state should allow creating with custom values`() {
        val state = AstroUiState(
            people = listOf(
                com.example.iss.data.AstroPerson("Alice", "ISS"),
                com.example.iss.data.AstroPerson("Bob", "ISS")
            ),
            totalNumber = 2,
            isLoading = false
        )
        assertEquals(2, state.people.size)
        assertEquals(2, state.totalNumber)
        assertTrue("Loading should be false", !state.isLoading)
        assertTrue("Error should be null", state.error == null)
    }

    @Test
    fun `ui state should support copy for error state`() {
        val state = AstroUiState(
            people = listOf(com.example.iss.data.AstroPerson("Alice", "ISS")),
            totalNumber = 1,
            isLoading = false
        )
        val errorState = state.copy(error = "Connection failed")
        assertEquals("People should be preserved", 1, errorState.people.size)
        assertEquals("Total should be preserved", 1, errorState.totalNumber)
        assertTrue("Error should be set", errorState.error == "Connection failed")
    }
}
