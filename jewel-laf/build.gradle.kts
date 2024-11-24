plugins {
	id("buildsrc.convention.jewel.common-conventions")
}

dependencies {
	implementation(project(":jewel-util"))
	implementation("com.miglayout:miglayout-swing:5.2")
	implementation("com.jgoodies:jgoodies-forms:1.9.0")
}
