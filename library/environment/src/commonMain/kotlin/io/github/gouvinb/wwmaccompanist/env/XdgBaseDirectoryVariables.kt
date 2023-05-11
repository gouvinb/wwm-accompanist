package io.github.gouvinb.wwmaccompanist.env

expect open class XdgBaseDirectoryVariables() : EnvironmentVariables {
    val uid: String?

    val xdgConfigHome: String
    val xdgCacheHome: String
    val xdgDataHome: String
    val xdgStateHome: String
    val xdgRuntimeDir: String

    val xdgDesktopDir: String
    val xdgDocumentsDir: String
    val xdgDownloadDir: String
    val xdgMusicDir: String
    val xdgPicturesDir: String
    val xdgPublicshareDir: String
    val xdgTemplatesDir: String
    val xdgVideosDir: String

    val xdgScreenshotsDir: String

    val xdgDataDirs: String
    val xdgConfigDirs: String

    val tmpDir: String
}
