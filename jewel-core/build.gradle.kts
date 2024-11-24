plugins {
	id("buildsrc.convention.jewel.common-conventions")
	application
}

dependencies {
	implementation(project(":jewel-laf"))
	implementation(project(":jewel-ui"))
	implementation(project(":jewel-util"))
}

application {
	mainClass = "com.slobodanzivanovic.jewel.core.MainKt"
}
