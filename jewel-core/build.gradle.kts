plugins {
	id("buildsrc.convention.jewel.common-conventions")
	application
}

dependencies {
	implementation(project(":jewel-ui"))
	implementation(project(":jewel-os"))
}

application {
	mainClass = "com.slobodanzivanovic.jewel.core.MainKt"
}
