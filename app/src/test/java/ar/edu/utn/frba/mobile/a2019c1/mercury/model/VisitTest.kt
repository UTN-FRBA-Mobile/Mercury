package ar.edu.utn.frba.mobile.a2019c1.mercury.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class VisitTest {

    private val visit: Visit
    private val today = LocalDate.of(2019, 1, 1)

    init {
        val someClient = Client("Juan", "1111111111", "Somewhere")
        visit = Visit(someClient, LocalTime.of(12, 0))
    }

    @Test
    fun `A Visit with future visit dates has the most recent one as the next visit date`() {
        val tomorrow = today.plusDays(1)
        val theDayAfterTomorrow = tomorrow.plusDays(1)

        visit.addVisitOnDate(theDayAfterTomorrow)
        visit.addVisitOnDate(tomorrow)

        assertThat(visit.nextVisitDate(today)).isEqualTo(tomorrow)
    }

    @Test
    fun `A Visit with a visit date today has it as the next visit date`() {
        visit.addVisitOnDate(today)

        assertThat(visit.nextVisitDate(today)).isEqualTo(today)
    }

    @Test
    fun `A Visit with no future visit dates has no next visit date`() {
        val yesterday = today.minusDays(1)

        visit.addVisitOnDate(yesterday)

        assertThat(visit.nextVisitDate(today)).isNull()
    }

}