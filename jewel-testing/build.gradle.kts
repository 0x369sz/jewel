plugins {
	id("buildsrc.convention.jewel.common-conventions")
}

dependencies {
	implementation(project(":jewel-core"))

	testImplementation(kotlin("test"))
}
