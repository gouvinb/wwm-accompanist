package io.github.gouvinb.wwmaccompanist.env

expect open class EnvironmentVariables {
    val _env: String?
    val user: String
    val path: String
    val pwd: String
    val home: String
    val shlvl: String
}
