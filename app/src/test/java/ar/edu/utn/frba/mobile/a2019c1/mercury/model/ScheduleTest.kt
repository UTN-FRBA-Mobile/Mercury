package ar.edu.utn.frba.mobile.a2019c1.mercury.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalDate
import java.time.LocalTime

class ScheduleTest {

    private val schedule: Schedule = Schedule("Mar del Plata")

    @Test
    fun `Given a new Schedule, it has a name`() {
        assertThat(schedule.name).isEqualTo("Mar del Plata")
    }

    @Test
    fun `Given a new Schedule, it has no clients`() {
        assertThat(schedule.visits()).isEmpty()
    }

    @Test
    fun `Given a new Schedule, it has a duration of 0 days`() {
        assertThat(schedule.duration()).isZero()
    }

    @Test
    fun `Given a new Schedule, can add a new Client on a specific day`() {
        val aClient = Client("Tyrion Lannister", "1111111111", "King's Landing")
        val aVisit = Visit(aClient,LocalTime.of(10, 0))
        schedule.addClientOnDay(1, aVisit)

        assertThat(schedule.visits()).hasSize(1)
        assertThat(schedule.visits().contains(aVisit))
    }

    @Test
    fun `Given a new Schedule, can add multiple new Clients on a specific day`() {
        val aClient = Client("Tyrion Lannister", "1111111111", "King's Landing")
        val anotherClient = Client("Jaime Lannister", "1111111111", "King's Landing")

        val aVisit = Visit(aClient,LocalTime.of(10, 0))
        val anotherVisit = Visit(anotherClient,LocalTime.of(12, 0))


        schedule.addClientOnDay(1, aVisit)
        schedule.addClientOnDay(1, anotherVisit)

        assertThat(schedule.visits()).hasSize(2)
        assertThat(schedule.visits()).containsExactlyInAnyOrder(aVisit,anotherVisit)
    }

    @Test
    fun `Given a Schedule with multiple Clients on a specific day, its duration is of one day`() {
        val aClient = Client("Tyrion Lannister", "1111111111", "King's Landing")
        val anotherClient = Client("Jaime Lannister", "1111111111", "King's Landing")

        val aVisit = Visit(aClient,LocalTime.of(10, 0))
        val anotherVisit = Visit(anotherClient,LocalTime.of(12, 0))

        schedule.addClientOnDay(1, aVisit)
        schedule.addClientOnDay(1, anotherVisit)

        assertThat(schedule.duration()).isEqualTo(1)
    }

    @Test
    fun `Given a new Schedule, can add multiple new Clients on a different days`() {
        val aClient = Client("Tyrion Lannister", "1111111111", "King's Landing")
        val anotherClient = Client("Jaime Lannister", "1111111111", "King's Landing")

        val aVisit = Visit(aClient,LocalTime.of(10, 0))
        val anotherVisit = Visit(anotherClient,LocalTime.of(12, 0))

        schedule.addClientOnDay(1, aVisit)
        schedule.addClientOnDay(3, anotherVisit)

        assertThat(schedule.visits()).hasSize(2)
        assertThat(schedule.visits()).containsExactlyInAnyOrder(aVisit, anotherVisit)
    }

    @Test
    fun `Given a Schedule with multiple Clients, its duration depends on the farthest day a client has to be visited`() {
        val aClient = Client("Tyrion Lannister", "1111111111", "King's Landing")
        val anotherClient = Client("Jaime Lannister", "1111111111", "King's Landing")

        val aVisit = Visit(aClient,LocalTime.of(10, 0))
        val anotherVisit = Visit(anotherClient,LocalTime.of(12, 0))

        schedule.addClientOnDay(1, aVisit)
        schedule.addClientOnDay(3, anotherVisit)

        assertThat(schedule.duration()).isEqualTo(3)
    }

    @Test
    fun `Starting a Schedule with no clients, creates no events`() {
        val startDate: LocalDate = LocalDate.of(2019,1,1)

        schedule.startOn(startDate)

        assertThat(schedule.visitsOnDates()).isEmpty()
    }

    @Test
    fun `Starting a Schedule with a client visit on the first day, creates an event on the start date`() {
        val aClient = Client("Tyrion Lannister", "1111111111", "King's Landing")
        val aVisit = Visit(aClient,LocalTime.of(10, 0))
        schedule.addClientOnDay(1, aVisit)
        val startDate: LocalDate = LocalDate.of(2019,1,1)

        schedule.startOn(startDate)

        assertThat(schedule.visitsOnDates()).isNotEmpty
        assertThat(schedule.visitsOnDates().first()).isEqualTo(VisitOnDate(aVisit, startDate))
    }

    @Test
    fun `Starting a Schedule with a client visit on any given day, creates an event the same amount of days after the start date`() {
        val aClient = Client("Tyrion Lannister", "1111111111", "King's Landing")
        val aVisit = Visit(aClient,LocalTime.of(10, 0))
        schedule.addClientOnDay(3, aVisit)
        val startDate: LocalDate = LocalDate.of(2019,1,1)

        schedule.startOn(startDate)

        val twoDaysLater: LocalDate = LocalDate.of(2019,1,3)
        assertThat(schedule.visitsOnDates()).isNotEmpty
        assertThat(schedule.visitsOnDates().first()).isEqualTo(VisitOnDate(aVisit, twoDaysLater))
    }

    @Test
    fun `Starting a Schedule with client visits on different days, creates events on different dates`() {
        val aClient = Client("Tyrion Lannister", "1111111111", "King's Landing")
        val anotherClient = Client("Jaime Lannister", "1111111111", "King's Landing")
        val aVisit = Visit(aClient,LocalTime.of(10, 0))
        val anotherVisit = Visit(anotherClient,LocalTime.of(12, 0))
        schedule.addClientOnDay(1, aVisit)
        schedule.addClientOnDay(3, anotherVisit)
        val startDate: LocalDate = LocalDate.of(2019,1,1)

        schedule.startOn(startDate)

        val twoDaysLater: LocalDate = LocalDate.of(2019,1,3)
        assertThat(schedule.visitsOnDates()[0]).isEqualTo(VisitOnDate(aVisit, startDate))
        assertThat(schedule.visitsOnDates()[1]).isEqualTo(VisitOnDate(anotherVisit, twoDaysLater))
    }
}