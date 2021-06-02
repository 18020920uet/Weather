package com.example.weather

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

class TimeConvertTests {
    @Test
    fun checkCalendarInstanceTimeZone_ReturnTrue() {
        val calendar = Calendar.getInstance()
        val timezone = calendar.timeZone
        val hcmTimeZone = TimeZone.getTimeZone("Asia/Ho_Chi_Minh")
        assertEquals(timezone, hcmTimeZone)
        assertEquals(timezone.rawOffset, hcmTimeZone.rawOffset)
    }
}