package io.github.gouvinb.wwmaccompanist.env

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.posix.STDOUT_FILENO
import platform.posix.TIOCGWINSZ
import platform.posix.ioctl
import platform.posix.winsize

actual class WinSize {
    actual var wsCol: UShort = 0u
        private set
    actual var wsRow: UShort = 0u
        private set
    actual var wsXPixel: UShort = 0u
        private set
    actual var wsYPixel: UShort = 0u
        private set

    init {
        memScoped {
            val winsize = alloc<winsize>()
            ioctl(STDOUT_FILENO, TIOCGWINSZ, winsize.ptr)

            wsCol = winsize.ws_col
            wsRow = winsize.ws_row
            wsXPixel = winsize.ws_xpixel
            wsYPixel = winsize.ws_ypixel
        }
    }
}
