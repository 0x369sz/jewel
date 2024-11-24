version = "0.0.1"

plugins {
	`kotlin-dsl`
}

kotlin {
	jvmToolchain(21)
}

dependencies {
	implementation(libs.kotlinGradlePlugin)
}

println()
println("========================")
println("Jewel Version: $version")
println("Java Version: ${System.getProperty("java.version")}")
println("Gradle Version: ${gradle.gradleVersion}")
println("========================")
println()
