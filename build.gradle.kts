plugins {
	java
	id("org.springframework.boot") version "3.2.0"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "com.wahook_java"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral();
	maven("https://jitpack.io")
}

extra["springModulithVersion"] = "1.1.0"

dependencies {
	// implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-data-rest")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.modulith:spring-modulith-starter-core")
	// implementation("org.springframework.modulith:spring-modulith-starter-mongodb")
	implementation("io.minio:minio:+")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	// runtimeOnly("org.mariadb.jdbc:mariadb-java-client")
	implementation("org.mariadb.jdbc:mariadb-java-client")
	implementation("com.github.serilogj:serilogj:0.6.1")
	///HikariCP
	implementation("com.zaxxer:HikariCP:5.1.0")
	
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.modulith:spring-modulith-starter-test")
	
}

// bootJar {
//     archiveFileName = "wahook.jar" // Specify the desired JAR file name
// }

dependencyManagement {
	imports {
		mavenBom("org.springframework.modulith:spring-modulith-bom:${property("springModulithVersion")}")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
