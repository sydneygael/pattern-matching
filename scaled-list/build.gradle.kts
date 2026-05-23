plugins {
    id("java")
}

sourceSets {
    named("main") {
        java.setSrcDirs(listOf("main/java"))
        resources.setSrcDirs(listOf("main/resources"))
    }
    named("test") {
        java.setSrcDirs(listOf("test/java"))
    }
}

tasks.test {
    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = true
    }
}

