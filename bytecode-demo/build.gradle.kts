plugins {
    id("java")
}

tasks.test {
    testLogging {
        events("passed", "failed", "skipped")
        showStandardStreams = true
    }
}
