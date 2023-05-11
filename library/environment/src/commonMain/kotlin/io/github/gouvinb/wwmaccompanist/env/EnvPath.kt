package io.github.gouvinb.wwmaccompanist.env

import okio.Path

expect object EnvPath {
    val pathsList: List<Path>

    val commandPathList: List<List<Path>>
    val commandPathFlatList: List<Path>

    val commandPathNameList: List<List<String>>
    val commandPathNameFlatList: List<String>

    val commandNameList: List<List<String>>
    val commandNameFlatList: List<String>

    /**
     * Returns `true` if this [EnvPath] contains the specified [other] sequence of characters as a substring.
     */
    operator fun contains(other: CharSequence): Boolean
}
