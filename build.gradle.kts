import org.gradle.kotlin.dsl.jacoco
import org.gradle.kotlin.dsl.test

plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.1.2"
	id("io.spring.dependency-management") version "1.1.2"
}

group = "backendnovice"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	// Spring Boot Starter Web
	implementation("org.springframework.boot:spring-boot-starter-web:3.1.2")
	// Spring Boot Starter Data JPA
	implementation("org.springframework.boot:spring-boot-starter-data-jpa:3.1.2")
	// MariaDB Java Client
	implementation("org.mariadb.jdbc:mariadb-java-client:3.1.4")
	// H2 Database Engine
	testImplementation("com.h2database:h2:2.2.220")
	// Spring Boot Dev Tools
	implementation("org.springframework.boot:spring-boot-devtools:3.1.2")
	// Spring Boot Starter Thymeleaf
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.1.2")
	// Project Lombok
	compileOnly("org.projectlombok:lombok:1.18.28")
	annotationProcessor("org.projectlombok:lombok:1.18.28")
}

// JaCoCo default configurations.
jacoco {
	toolVersion = "0.8.9"
	reportsDirectory.set(layout.buildDirectory.dir("reports/jacoco"))
}

tasks.withType<Test> {
	useJUnitPlatform()
}
tasks.test {
	// Report always generated after test run.
	finalizedBy(tasks.jacocoTestReport)
}
tasks.jacocoTestReport {
	// Test always required before generating report.
	dependsOn(tasks.test)
	// Set report output.
	reports {
		xml.required.set(false)
		csv.required.set(false)
		html.outputLocation.set(file("${buildDir}/jacocoResult"))
	}
}
tasks.jacocoTestCoverageVerification {
	violationRules {
		rule {
			limit {
				// Set code coverage options.
				minimum = "0.75".toBigDecimal()
			}
		}
	}
}
