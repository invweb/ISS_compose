package com.example.iss.viewmodel

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IssViewModelTest {

    @Test
    fun `ui state data class should have correct defaults`() {
        val state = IssUiState()
        assertEquals("Latitude should be 0.0 by default", 0.0, state.latitude, 0.0001)
        assertEquals("Longitude should be 0.0 by default", 0.0, state.longitude, 0.0001)
        assertTrue("User latitude should be null by default", state.userLatitude == null)
        assertTrue("User longitude should be null by default", state.userLongitude == null)
        assertTrue("Loading should be true by default", state.isLoading)
        assertTrue("Error should be null by default", state.error == null)
        assertTrue("Should not have permission by default", !state.hasLocationPermission)
        assertTrue("Path history should be empty by default", state.pathHistory.isEmpty())
    }

    @Test
    fun `ui state should allow creating with custom values`() {
        val state = IssUiState(
            latitude = 51.8857,
            longitude = -17.1251,
            userLatitude = 48.8566,
            userLongitude = 2.3522,
            isLoading = false,
            hasLocationPermission = true,
            pathHistory = listOf(Pair(51.0, -17.0), Pair(51.8857, -17.1251))
        )
        assertEquals(51.8857, state.latitude, 0.0001)
        assertEquals(-17.1251, state.longitude, 0.0001)
        assertEquals(48.8566, state.userLatitude!!, 0.0001)
        assertEquals(2.3522, state.userLongitude!!, 0.0001)
        assertTrue("Loading should be false", !state.isLoading)
        assertTrue("Should have permission", state.hasLocationPermission)
        assertEquals("Path should have 2 points", 2, state.pathHistory.size)
    }

    @Test
    fun `ui state should support copy for error state`() {
        val state = IssUiState(
            latitude = 51.8857,
            longitude = -17.1251,
            isLoading = false
        )
        val errorState = state.copy(error = "Connection failed", isLoading = false)
        assertEquals("Latitude should be preserved", 51.8857, errorState.latitude, 0.0001)
        assertEquals("Longitude should be preserved", -17.1251, errorState.longitude, 0.0001)
        assertTrue("Error should be set", errorState.error == "Connection failed")
    }
}
