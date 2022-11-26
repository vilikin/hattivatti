package link.hattivatti.app.testing

import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

@Testcontainers
open class DynamoDbTest {
    companion object {
        private val localstackImage = DockerImageName.parse("localstack/localstack:1.2.0")

        @JvmStatic
        @Container
        protected val container: LocalStackContainer = LocalStackContainer(localstackImage)
            .withServices(DYNAMODB)
    }

    protected val dynamoDbClient: DynamoDbClient = DynamoDbClient.builder()
        .endpointOverride(container.getEndpointOverride(DYNAMODB))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(container.accessKey, container.secretKey)
            )
        )
        .region(Region.of(container.region))
        .build()

    protected val dynamoDbEnhancedClient: DynamoDbEnhancedClient = DynamoDbEnhancedClient.builder()
        .dynamoDbClient(dynamoDbClient)
        .build()
}
