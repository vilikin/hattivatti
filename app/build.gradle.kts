import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.transformers.*
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("org.springframework.boot.experimental.thin-launcher") version "1.0.28.RELEASE"
	id("org.springframework.boot") version "2.7.5"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "link.hattivatti"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

extra["springCloudVersion"] = "2021.0.5"
extra["awsLambdaEventsVersion"] = "3.11.0"
extra["awsLambdaCoreVersion"] = "1.2.2"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")
    implementation("org.springframework.cloud:spring-cloud-starter-function-web")
    implementation("org.springframework.cloud:spring-cloud-function-kotlin")
    implementation("org.springframework.cloud:spring-cloud-function-adapter-aws")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.4")

    implementation(platform("software.amazon.awssdk:bom:2.15.0"))
    implementation("software.amazon.awssdk:dynamodb")
    implementation("software.amazon.awssdk:dynamodb-enhanced")

    implementation("javax.xml.bind:jaxb-api")
    implementation("org.glassfish.jaxb:jaxb-runtime")

    implementation("com.amazonaws:aws-lambda-java-events:${property("awsLambdaEventsVersion")}")
    implementation("com.amazonaws:aws-lambda-java-core:${property("awsLambdaCoreVersion")}")

	testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.assertj:assertj-core:3.23.1")

    testImplementation("io.mockk:mockk:1.13.2")

    testImplementation("org.testcontainers:testcontainers:1.17.6")
    testImplementation("org.testcontainers:junit-jupiter:1.17.6")
    testImplementation("org.testcontainers:mockserver:1.17.6")
    testImplementation("org.testcontainers:localstack:1.17.6")
    testImplementation("org.mock-server:mockserver-client-java:5.14.0")

    testRuntimeOnly("com.amazonaws:aws-java-sdk-core:1.12.349")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<Jar> {
    manifest {
        attributes(mapOf(
            "Main-Class" to "link.hattivatti.app.ApplicationKt"
        ))
    }
}

tasks.withType<ShadowJar> {
    archiveClassifier.set("aws")
    dependencies {
        exclude(dependency("org.springframework.cloud:spring-cloud-function-web"))
    }
    // Required for Spring
    mergeServiceFiles()
    append("META-INF/spring.handlers")
    append("META-INF/spring.schemas")
    append("META-INF/spring.tooling")
    transform(PropertiesFileTransformer().apply {
        paths = listOf("META-INF/spring.factories")
        mergeStrategy = "append"
    })
}

tasks {
    assemble {
        dependsOn(shadowJar, thinJar)
    }
}
