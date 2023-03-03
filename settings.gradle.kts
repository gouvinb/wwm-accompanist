import Settings_gradle.ModuleType.APP
import Settings_gradle.ModuleType.LIBRARY
import java.util.*

enum class ModuleType(private val parentDirName: String) {
    /**
     * Binary project type.
     */
    APP("app"),

    /**
     * Library project type.
     */
    LIBRARY("library"),

    /**
     * Example project type for libraries
     */
    SAMPLE("sample");

    fun value() = this.parentDirName

    fun pathStrProject() = "$parentDirName/"

    fun lowercase() = toString().lowercase(Locale.getDefault())

    companion object {
        private val CONSTANTS = HashMap<Any, ModuleType>()

        init {
            values().forEach { CONSTANTS[it.value()] = it }
        }

        fun fromValue(value: Any) = CONSTANTS[value] ?: throw IllegalArgumentException(value.toString())
        fun fromPath(path: String) = path.split("/")[0].run {
            CONSTANTS[this]
                ?: CONSTANTS[path.split("/").take(2).joinToString("/")]
                ?: throw IllegalArgumentException("'$path' has no valid type (catch: [$this]).")
        }
    }
}

/**
 * Calling includeProject(name, type) is shorthand for:
 *
 *   ```kts
 *   include(":$type-$name")
 *   pjt(":$type-$name").projectDir = new File("$type/$name")
 *   ```
 *
 * Note that <name> directly controls the Gradle project name, and also indirectly sets:
 *    - the pjt name in the IDE
 *    - the Maven artifactId
 */
fun includeProject(path: String, typeOfModule: ModuleType) {
    val (fullName, projectDir) = when (typeOfModule) {
        APP -> path to path
        else -> "${typeOfModule.lowercase()}-$path" to "${typeOfModule.pathStrProject()}$path"

    }

    settings.include(":$fullName")
    project(":$fullName").projectDir = File(projectDir)
}


pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

// dependencyResolutionManagement {
//     repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
//     repositories {
//         // Register the AndroidX snapshot repository first so snapshots don't attempt (and fail)
//         // to download from the non-snapshot repositories
//         google()
//         mavenCentral()
//         gradlePluginPortal()
//     }
// }

rootProject.name = "wwm-accompanist"
rootProject.buildFileName = "build.gradle.kts"

// App
includeProject("application", APP)

// Libs
includeProject("audio", LIBRARY)
includeProject("backlight", LIBRARY)
includeProject("bar", LIBRARY)
includeProject("launcher", LIBRARY)
includeProject("screenshot", LIBRARY)
includeProject("theme", LIBRARY)
includeProject("wallpaper", LIBRARY)

// samples
// includeProject("example-a", SAMPLE)
