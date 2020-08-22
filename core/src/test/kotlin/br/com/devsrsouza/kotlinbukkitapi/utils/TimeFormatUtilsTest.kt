package br.com.devsrsouza.kotlinbukkitapi.utils

import org.junit.Assert.*
import org.junit.Test

class TimeFormatUtilsTest {

    @Test
    fun `should format time to human readable (EN - SINGLE)`() {
        val time = 61

        assertEquals("1 minute 1 second", time.formatter.EN)
    }

    @Test
    fun `should format time to human readable (EN - PLURAL)`() {
        val time = 330

        assertEquals("5 minutes 30 seconds", time.formatter.EN)
    }

    @Test
    fun `should format time to human readable (PT-BR - SINGLE)`() {
        val time = 61

        assertEquals("1 minuto 1 segundo", time.formatter.PTBR)
    }

    @Test
    fun `should format time to human readable (PT-BR - PLURAL)`() {
        val time = 330

        assertEquals("5 minutos 30 segundos", time.formatter.PTBR)
    }

    @Test
    fun `should format time to human readable with new formatter (EN)`() {
        val time = 3601

        val formater = "%HOUR %MIN %SEC"

        assertEquals("1 hour 1 second", time.formatter.format(ENFormat, formater))
    }

    @Test
    fun `should format time to human readable with new formatter and spacer (EN)`() {
        val time = 3601

        val formater = "%HOUR;%MIN;%SEC"
        val spacer = ';'

        assertEquals("1 hour;1 second", time.formatter.format(ENFormat, formater, spacer))
    }

    @Test
    fun `should format time to human readable with new formatter without hour (EN)`() {
        val time = 3661

        val formater = "%MIN %SEC"

        assertEquals("1 minute 1 second", time.formatter.format(ENFormat, formater))
    }
}