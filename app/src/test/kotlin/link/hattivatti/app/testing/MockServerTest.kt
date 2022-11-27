package link.hattivatti.app.testing

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

    protected val mockServerClient = MockServerClient(
        mockServerContainer.host,
        mockServerContainer.serverPort
    )

    @BeforeEach
    fun resetMockServer() {
        mockServerClient.reset()
    }
}
