package com.movtery.zalithlauncher.ui.fragment

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.movtery.anim.AnimPlayer
import com.movtery.anim.animations.Animations
import com.movtery.zalithlauncher.R
import com.movtery.zalithlauncher.databinding.FragmentAboutBinding
import com.movtery.zalithlauncher.feature.CheckSponsor
import com.movtery.zalithlauncher.feature.CheckSponsor.Companion.check
import com.movtery.zalithlauncher.feature.CheckSponsor.Companion.getSponsorData
import com.movtery.zalithlauncher.feature.log.Logging
import com.movtery.zalithlauncher.task.TaskExecutors
import com.movtery.zalithlauncher.ui.dialog.TipDialog
import com.movtery.zalithlauncher.ui.subassembly.about.AboutItemBean
import com.movtery.zalithlauncher.ui.subassembly.about.AboutItemBean.AboutItemButtonBean
import com.movtery.zalithlauncher.ui.subassembly.about.AboutRecyclerAdapter
import com.movtery.zalithlauncher.ui.subassembly.about.SponsorItemBean
import com.movtery.zalithlauncher.ui.subassembly.about.SponsorRecyclerAdapter
import com.movtery.zalithlauncher.utils.PathAndUrlManager
import com.movtery.zalithlauncher.utils.ZHTools
import com.movtery.zalithlauncher.utils.stringutils.StringUtils
import net.kdt.pojavlaunch.Tools

class AboutFragment : FragmentWithAnim(R.layout.fragment_about) {
    companion object {
        const val TAG: String = "AboutFragment"
    }

    private lateinit var binding: FragmentAboutBinding
    private val mAboutData: MutableList<AboutItemBean> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        loadSponsorData()
        loadAboutData(requireContext().resources)

        binding.appInfo.text = StringUtils.insertNewline(StringUtils.insertSpace(getString(R.string.about_version_name), ZHTools.getVersionName()),
            StringUtils.insertSpace(getString(R.string.about_version_code), ZHTools.getVersionCode()),
            StringUtils.insertSpace(getString(R.string.about_last_update_time), ZHTools.getLastUpdateTime(requireContext())),
            StringUtils.insertSpace(getString(R.string.about_version_status), ZHTools.getVersionStatus(requireContext())))
        binding.appInfo.setOnClickListener{ StringUtils.copyText("text", binding.appInfo.text.toString(), requireContext()) }

        binding.returnButton.setOnClickListener { ZHTools.onBackPressed(requireActivity()) }
        binding.githubButton.setOnClickListener { Tools.openURL(requireActivity(), PathAndUrlManager.URL_HOME) }
        binding.licenseButton.setOnClickListener { Tools.openURL(requireActivity(), "https://www.gnu.org/licenses/gpl-3.0.html") }
        binding.supportDevelopment.setOnClickListener {
            TipDialog.Builder(requireActivity())
                .setTitle(R.string.request_sponsorship_title)
                .setMessage(R.string.request_sponsorship_message)
                .setConfirm(R.string.about_button_support_development)
                .setConfirmClickListener { Tools.openURL(requireActivity(), PathAndUrlManager.URL_SUPPORT) }
                .buildDialog()
        }

        val aboutAdapter = AboutRecyclerAdapter(this.mAboutData)
        binding.aboutRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = aboutAdapter
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun loadAboutData(resources: Resources) {
        mAboutData.clear()

        mAboutData.add(
            AboutItemBean(
                resources.getDrawable(R.drawable.ic_pojav_full, requireContext().theme),
                "PojavLauncherTeam",
                getString(R.string.about_PojavLauncher_desc),
                AboutItemButtonBean(requireActivity(), "Github", "https://github.com/PojavLauncherTeam/PojavLauncher")
            )
        )
        mAboutData.add(
            AboutItemBean(
                resources.getDrawable(R.drawable.image_about_movtery, requireContext().theme),
                "墨北MovTery",
                getString(R.string.about_MovTery_desc),
                AboutItemButtonBean(
                    requireActivity(),
                    getString(R.string.about_access_space),
                    "https://space.bilibili.com/2008204513"
                )
            )
        )
        mAboutData.add(
            AboutItemBean(
                resources.getDrawable(R.drawable.image_about_mcmod, requireContext().theme),
                "MC 百科",
                getString(R.string.about_mcmod_desc),
                AboutItemButtonBean(
                    requireActivity(),
                    getString(R.string.about_access_link),
                    PathAndUrlManager.URL_MCMOD)
            )
        )
        mAboutData.add(
            AboutItemBean(
                resources.getDrawable(R.drawable.image_about_verafirefly, requireContext().theme),
                "Vera-Firefly",
                getString(R.string.about_VeraFirefly_desc),
                AboutItemButtonBean(
                    requireActivity(),
                    getString(R.string.about_access_space),
                    "https://space.bilibili.com/1412062866"
                )
            )
        )
        mAboutData.add(
            AboutItemBean(
                resources.getDrawable(R.drawable.image_about_lingmuqiuzhu, requireContext().theme),
                "柃木湫竹",
                getString(R.string.about_LingMuQiuZhu_desc),
                AboutItemButtonBean(
                    requireActivity(),
                    getString(R.string.about_access_space),
                    "https://space.bilibili.com/515165764"
                )
            )
        )
        mAboutData.add(
            AboutItemBean(
                resources.getDrawable(R.drawable.image_about_shirosakimio, requireContext().theme),
                "ShirosakiMio",
                getString(R.string.about_ShirosakiMio_desc),
                AboutItemButtonBean(
                    requireActivity(),
                    getString(R.string.about_access_space),
                    "https://space.bilibili.com/35801833"
                )
            )
        )
        mAboutData.add(
            AboutItemBean(
                resources.getDrawable(R.drawable.image_about_bangbang93, requireContext().theme),
                "bangbang93",
                getString(R.string.about_bangbang93_desc),
                AboutItemButtonBean(
                    requireActivity(),
                    getString(R.string.about_button_support_development),
                    "https://afdian.com/a/bangbang93"
                )
            )
        )
    }

    private fun loadSponsorData() {
        check(requireContext(), object : CheckSponsor.CheckListener {
            override fun onFailure() { setSponsorVisible(false) }
            override fun onSuccessful(data: List<SponsorItemBean>?) { setSponsorVisible(true) }
        })
    }

    private fun setSponsorVisible(visible: Boolean) {
        TaskExecutors.runInUIThread {
            try {
                binding.sponsorLayout.visibility = if (visible) View.VISIBLE else View.GONE

                if (visible) {
                    binding.sponsorRecycler.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = SponsorRecyclerAdapter(getSponsorData())
                    }
                }
            } catch (e: Exception) {
                Logging.e("setSponsorVisible", e.toString())
            }
        }
    }

    override fun slideIn(animPlayer: AnimPlayer) {
        animPlayer.apply(AnimPlayer.Entry(binding.infoLayout, Animations.BounceInDown))
            .apply(AnimPlayer.Entry(binding.operateLayout, Animations.BounceInLeft))
            .apply(AnimPlayer.Entry(binding.returnButton, Animations.FadeInLeft))
            .apply(AnimPlayer.Entry(binding.githubButton, Animations.FadeInLeft))
            .apply(AnimPlayer.Entry(binding.supportDevelopment, Animations.FadeInLeft))
    }

    override fun slideOut(animPlayer: AnimPlayer) {
        animPlayer.apply(AnimPlayer.Entry(binding.infoLayout, Animations.FadeOutUp))
        animPlayer.apply(AnimPlayer.Entry(binding.operateLayout, Animations.FadeOutRight))
    }
}

