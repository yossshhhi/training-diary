plugins {
    id("java")
}
group = "kz.yossshhhi"
version = "1.0-SNAPSHOT"
val junitVersion = "5.10.2"
val mockitoVersion = "5.11.0"
val lombokVersion = "1.18.32"
val slf4jVersion = "2.0.12"
val logbackVersion = "1.4.14"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.projectlombok:lombok:$lombokVersion")
    annotationProcessor("org.projectlombok:lombok:$lombokVersion")
    implementation("org.slf4j:slf4j-api:$slf4jVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    testImplementation("org.mockito:mockito-junit-jupiter:$mockitoVersion")

    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks.test {
    useJUnitPlatform()
}


tasks.jar {
    manifest {
        attributes["Main-Class"] = "kz.yossshhhi.Application"
    }
    from(sourceSets["main"].output)
}
