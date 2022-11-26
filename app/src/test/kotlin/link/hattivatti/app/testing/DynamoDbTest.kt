package link.hattivatti.app.testing

import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedAsyncClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient

@Testcontainers
open class DynamoDbTest {
    companion object {
        private val localstackImage = DockerImageName.parse("localstack/localstack:1.2.0")

        @JvmStatic
        @Container
        protected val container: LocalStackContainer = LocalStackContainer(localstackImage)
            .withServices(DYNAMODB)
    }

    protected val dynamoDbAsyncClient: DynamoDbAsyncClient = DynamoDbAsyncClient.builder()
        .endpointOverride(container.getEndpointOverride(DYNAMODB))
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(container.accessKey, container.secretKey)
            )
        )
        .region(Region.of(container.region))
        .build()

    protected val dynamoDbEnhancedAsyncClient: DynamoDbEnhancedAsyncClient = DynamoDbEnhancedAsyncClient.builder()
        .dynamoDbClient(dynamoDbAsyncClient)
        .build()
}
