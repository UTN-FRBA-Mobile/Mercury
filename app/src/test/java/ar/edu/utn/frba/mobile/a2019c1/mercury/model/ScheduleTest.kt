package ar.edu.utn.frba.mobile.a2019c1.mercury.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.time.LocalTime

class ScheduleTest {

    private val schedule: Schedule = Schedule("Mar del Plata")

    @Test
    fun `Given a new Schedule, it has a name`() {
        assertThat(schedule.name).isEqualTo("Mar del Plata")
    }

    @Test
    fun `Given a new Schedule, it has no clients`() {
        assertThat(schedule.clients()).isEmpty()
    }

    @Test
    fun `Given a new Schedule, it has a duration of 0 days`() {
        assertThat(schedule.duration()).isZero()
    }

    @Test
    fun `Given a new Schedule, can add a new Client on a specific day`() {
        val aClient = Client("Tyrion Lannister", "1111111111", "King's Landing", LocalTime.of(10, 0))

        schedule.addClientOnDay(1, aClient)

        assertThat(schedule.clients()).hasSize(1)
        assertThat(schedule.clients()).contains(aClient)
    }

    @Test
    fun `Given a new Schedule, can add multiple new Clients on a specific day`() {
        val aClient = Client("Tyrion Lannister", "1111111111", "King's Landing", LocalTime.of(10, 0))
        val anotherClient = Client("Jaime Lannister", "1111111111", "King's Landing", LocalTime.of(12, 0))

        schedule.addClientOnDay(1, aClient)
        schedule.addClientOnDay(1, anotherClient)

        assertThat(schedule.clients()).hasSize(2)
        assertThat(schedule.clients()).containsExactlyInAnyOrder(aClient, anotherClient)
    }

    @Test
    fun `Given a Schedule with multiple Clients on a specific day, its duration is of one day`() {
        val aClient = Client("Tyrion Lannister", "1111111111", "King's Landing", LocalTime.of(10, 0))
        val anotherClient = Client("Jaime Lannister", "1111111111", "King's Landing", LocalTime.of(12, 0))

        schedule.addClientOnDay(1, aClient)
        schedule.addClientOnDay(1, anotherClient)

        assertThat(schedule.duration()).isEqualTo(1)
    }

    @Test
    fun `Given a new Schedule, can add multiple new Clients on a different days`() {
        val aClient = Client("Tyrion Lannister", "1111111111", "King's Landing", LocalTime.of(10, 0))
        val anotherClient = Client("Jaime Lannister", "1111111111", "King's Landing", LocalTime.of(12, 0))

        schedule.addClientOnDay(1, aClient)
        schedule.addClientOnDay(3, anotherClient)

        assertThat(schedule.clients()).hasSize(2)
        assertThat(schedule.clients()).containsExactlyInAnyOrder(aClient, anotherClient)
    }

    @Test
    fun `Given a Schedule with multiple Clients, its duration depends on the farthest day a client has to be visited`() {
        val aClient = Client("Tyrion Lannister", "1111111111", "King's Landing", LocalTime.of(10, 0))
        val anotherClient = Client("Jaime Lannister", "1111111111", "King's Landing", LocalTime.of(12, 0))

        schedule.addClientOnDay(1, aClient)
        schedule.addClientOnDay(3, anotherClient)

        assertThat(schedule.duration()).isEqualTo(3)
    }
}