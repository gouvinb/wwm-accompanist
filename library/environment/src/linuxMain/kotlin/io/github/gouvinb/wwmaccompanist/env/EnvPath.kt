package io.github.gouvinb.wwmaccompanist.env

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

actual object EnvPath {
    actual val pathsList: List<Path>
        get() = EnvironmentVariables().path.split(":")
            .map { it.toPath() }
            .filter { FileSystem.SYSTEM.exists(it) }

    actual val commandPathList: List<List<Path>>
        get() = pathsList.map {
            try {
                FileSystem.SYSTEM.list(it)
            } catch (e: Exception) {
                throw IllegalStateException("Cannont list `$it`.", e)
            }
        }

    actual val commandPathFlatList: List<Path>
        get() = commandPathList.flatten()

    actual val commandPathNameList: List<List<String>>
        get() = commandPathList.map { group -> group.map { "$it" } }

    actual val commandPathNameFlatList: List<String>
        get() = commandNameList.flatten()

    actual val commandNameList: List<List<String>>
        get() = commandPathList.map { group -> group.map { it.name } }

    actual val commandNameFlatList: List<String>
        get() = commandPathFlatList.map { it.name }

    actual operator fun contains(other: CharSequence): Boolean = other in commandNameFlatList
}
