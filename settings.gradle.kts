rootProject.name = "jewel"

dependencyResolutionManagement {
	@Suppress("UnstableApiUsage")
	repositories {
		mavenCentral()
	}
}

plugins {
	id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.0")
}

include(":jewel-core")
include(":jewel-ui")
include(":jewel-os")
include(":jewel-testing")
