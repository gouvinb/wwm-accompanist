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
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")

        // Workaround for https://youtrack.jetbrains.com/issue/KT-51379
        exclusiveContent {
            forRepository {
                ivy {
                    url = uri("https://download.jetbrains.com/kotlin/native/builds")
                    name = "Kotlin Native"
                    patternLayout {
                        // example download URLs:
                        // https://download.jetbrains.com/kotlin/native/builds/releases/1.7.20/linux-x86_64/kotlin-native-prebuilt-linux-x86_64-1.7.20.tar.gz
                        // https://download.jetbrains.com/kotlin/native/builds/releases/1.7.20/windows-x86_64/kotlin-native-prebuilt-windows-x86_64-1.7.20.zip
                        // https://download.jetbrains.com/kotlin/native/builds/releases/1.7.20/macos-x86_64/kotlin-native-prebuilt-macos-x86_64-1.7.20.tar.gz
                        listOf(
                            "macos-x86_64",
                            "macos-aarch64",
                            "osx-x86_64",
                            "osx-aarch64",
                            "linux-x86_64",
                            "windows-x86_64",
                        ).forEach { os ->
                            listOf("dev", "releases").forEach { stage ->
                                artifact("$stage/[revision]/$os/[artifact]-[revision].[ext]")
                            }
                        }
                    }
                    metadataSources { artifact() }
                }
            }
            filter { includeModuleByRegex(".*", ".*kotlin-native-prebuilt.*") }
        }
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
