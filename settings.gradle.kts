val app = "app"
val library = "library"
val feature = "feature"
val thirdParty = "third-party"

/**
 * Calling includeProject(name, type) is shorthand for:
 *
 *   ```kts
 *   include(":$type-$name")
 *   pjt(":$type-$name").projectDir = new File("$type/$name")
 *   ```
 *
 * Note that <name> directly controls the Gradle pjt name, and also indirectly sets:
 *    - the pjt name in the IDE
 *    - the Maven artifactId
 */
fun includeProject(path: String, typeOfModule: String) {
    val (fullName, projectDir) = when (typeOfModule) {
        app -> path to path
        library, feature, thirdParty -> "$typeOfModule:$path" to "$typeOfModule/$path"
        else -> throw GradleException("Unrecognized :$typeOfModule:$path module.")
    }
    settings.include(":$fullName")
    project(":$fullName").projectDir = File(projectDir)
}

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "wwm-accompanist"
rootProject.buildFileName = "build.gradle.kts"

// App
includeProject("application", app)

// Libs
includeProject("accompanist-util", library)
includeProject("logger", library)
includeProject("environment", library)

includeProject("audio", library)
includeProject("backlight", library)
includeProject("bar", library)
includeProject("launcher", library)
includeProject("screenshot", library)
includeProject("theme", library)
includeProject("wallpaper", library)

// samples
// includeProject("example-a", SAMPLE)
