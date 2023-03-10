package io.github.gouvinb.wwmaccompanist.util.extension

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ResultExtensionTest {

    object FakeError : Error("Catch me !")
    class MockMessageError(override val message: String?) : Error()

    @Test
    fun catchMessageFailureTest() {
        val exception = assertFailsWith<MockMessageError> {
            runCatching { throw FakeError }
                .catchMessageFailure { msg -> throw MockMessageError(msg) }
        }

        assertEquals(FakeError.message, exception.message)
    }
}
