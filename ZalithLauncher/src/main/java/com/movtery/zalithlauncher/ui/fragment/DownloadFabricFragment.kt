package com.movtery.zalithlauncher.ui.fragment

import com.movtery.zalithlauncher.R
import net.kdt.pojavlaunch.modloaders.FabriclikeUtils
import net.kdt.pojavlaunch.modloaders.ModloaderDownloadListener

class DownloadFabricFragment : DownloadFabricLikeFragment(FabriclikeUtils.FABRIC_UTILS, R.drawable.ic_fabric), ModloaderDownloadListener {
    companion object {
        const val TAG: String = "DownloadFabricFragment"
    }
}