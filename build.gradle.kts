plugins {
	java
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
	// Spring Boot Dev Tools
	implementation("org.springframework.boot:spring-boot-devtools:3.1.2")
	// Spring Boot Starter Thymeleaf
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf:3.1.2")
	// Project Lombok
	compileOnly("org.projectlombok:lombok:1.18.28")
	annotationProcessor("org.projectlombok:lombok:1.18.28")


}

tasks.withType<Test> {
	useJUnitPlatform()
}
