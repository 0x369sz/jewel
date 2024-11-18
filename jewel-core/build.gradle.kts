plugins {
	id("buildsrc.convention.jewel.common-conventions")
	application
}

dependencies {
	implementation(project(":jewel-ui"))
	implementation(project(":jewel-laf"))
}

application {
	mainClass = "com.slobodanzivanovic.jewel.core.MainKt"
}
