package io.github.gouvinb.wwmaccompanist.env

import kotlinx.cinterop.toKString
import platform.posix.getenv

actual open class XdgBaseDirectoryVariables : EnvironmentVariables() {
    actual val uid: String? = getenv("UID")?.toKString()

    actual val xdgConfigHome: String = getenv("XDG_CONFIG_HOME")?.toKString() ?: "$home/.config"
    actual val xdgCacheHome: String = getenv("XDG_CACHE_HOME")?.toKString() ?: "$home/.cache"
    actual val xdgDataHome: String = getenv("XDG_DATA_HOME")?.toKString() ?: "$home/.local/share"
    actual val xdgStateHome: String = getenv("XDG_STATE_HOME")?.toKString() ?: "$home/.local/state"
    actual val xdgRuntimeDir: String = getenv("XDG_RUNTIME_DIR")?.toKString() ?: uid?.let { "/run/user/$it" } ?: ""

    actual val xdgDesktopDir: String = getenv("XDG_DESKTOP_DIR")?.toKString() ?: "$home/Desktop"
    actual val xdgDocumentsDir: String = getenv("XDG_DOCUMENTS_DIR")?.toKString() ?: "$home/Documents"
    actual val xdgDownloadDir: String = getenv("XDG_DOWNLOAD_DIR")?.toKString() ?: "$home/Downloads"
    actual val xdgMusicDir: String = getenv("XDG_MUSIC_DIR")?.toKString() ?: "$home/Music"
    actual val xdgPicturesDir: String = getenv("XDG_PICTURES_DIR")?.toKString() ?: "$home/Pictures"
    actual val xdgPublicshareDir: String = getenv("XDG_PUBLICSHARE_DIR")?.toKString() ?: "$home/Public"
    actual val xdgTemplatesDir: String = getenv("XDG_TEMPLATES_DIR")?.toKString() ?: "$home/Templates"
    actual val xdgVideosDir: String = getenv("XDG_VIDEOS_DIR")?.toKString() ?: "$home/Videos"

    actual val xdgScreenshotsDir: String = getenv("XDG_SCREENSHOTS_DIR")?.toKString() ?: xdgPicturesDir

    actual val xdgDataDirs: String = getenv("XDG_DATA_DIRS")?.toKString() ?: "/usr/local/share:/usr/share"
    actual val xdgConfigDirs: String = getenv("XDG_CONFIG_DIRS")?.toKString() ?: "/etc/xdg"

    actual val tmpDir: String = getenv("TMPDIR")?.toKString() ?: "/tmp/"
}
