package com.example

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlin.test.*

class ServerTest {

    @Test
    fun `test root endpoint`() = testApplication {
        // loads default configuration
        configure()
        // verify server root returns 200
        val response = client.get("/test1")
        assertEquals("html", response.contentType()?.contentSubtype)
        assertEquals(HttpStatusCode.OK, client.get("/").status)
        assertContains(response.bodyAsText(), "Hellow from vishu")
    }

}
