package link.hattivatti.app.testing

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockserver.client.MockServerClient
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@Testcontainers
open class MockServerTest {
    companion object {
        private val mockServerImage = DockerImageName
            .parse("mockserver/mockserver")
            .withTag("mockserver-" + MockServerClient::class.java.getPackage().implementationVersion)

        @JvmStatic
        @Container
        protected val mockServerContainer = MockServerContainer(mockServerImage)
    }

    protected lateinit var mockServerClient: MockServerClient

    @BeforeEach
    fun beforeEach() {
        mockServerClient = MockServerClient(
            mockServerContainer.host,
            mockServerContainer.serverPort
        )
    }

    @AfterEach
    fun afterEach() {
        mockServerClient.close()
    }
}