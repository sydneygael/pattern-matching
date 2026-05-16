import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm")
}

kotlin {
    jvmToolchain(25)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_25
    }
}

val pythonTest by tasks.registering(Exec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Runs the Python visitor implementation tests."
    workingDir = projectDir.resolve("python")
    commandLine("uv", "run", "python", "-m", "pytest")
}

tasks.check {
    dependsOn(pythonTest)
}
