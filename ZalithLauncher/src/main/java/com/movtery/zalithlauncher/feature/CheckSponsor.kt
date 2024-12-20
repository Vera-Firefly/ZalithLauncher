package com.movtery.zalithlauncher.feature

import android.content.Context
import com.movtery.zalithlauncher.R
import com.movtery.zalithlauncher.feature.log.Logging
import com.movtery.zalithlauncher.ui.subassembly.about.SponsorItemBean
import com.movtery.zalithlauncher.ui.subassembly.about.SponsorMeta
import com.movtery.zalithlauncher.utils.PathAndUrlManager
import com.movtery.zalithlauncher.utils.http.CallUtils
import com.movtery.zalithlauncher.utils.http.CallUtils.CallbackListener
import com.movtery.zalithlauncher.utils.stringutils.StringUtils
import net.kdt.pojavlaunch.Tools
import okhttp3.Call
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.util.Objects

class CheckSponsor {
    companion object {
        private var sponsorData: ArrayList<SponsorItemBean>? = null
        private var isChecking = false

        @JvmStatic
        fun getSponsorData(): List<SponsorItemBean>? {
            return sponsorData
        }

        @JvmStatic
        fun check(context: Context, listener: CheckListener) {
            if (isChecking) {
                listener.onFailure()
                return
            }
            isChecking = true

            sponsorData?.let {
                listener.onSuccessful(sponsorData)
                isChecking = false
                return
            }

            val token = context.getString(R.string.private_api_token)
            CallUtils(object : CallbackListener {
                override fun onFailure(call: Call?) {
                    listener.onFailure()
                    isChecking = false
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call?, response: Response?) {
                    if (!response!!.isSuccessful) {
                        Logging.e("CheckSponsor", "Unexpected code ${response.code}")
                    } else {
                        runCatching {
                            Objects.requireNonNull(response.body)
                            val responseBody = response.body!!.string()

                            val originJson = JSONObject(responseBody)
                            val rawBase64 = originJson.getString("content")
                            //base64解码，因为这里读取的是一个经过Base64加密后的文本
                            val rawJson = StringUtils.decodeBase64(rawBase64)

                            val sponsorMeta =
                                Tools.GLOBAL_GSON.fromJson(rawJson, SponsorMeta::class.java)
                            if (sponsorMeta.sponsors.isEmpty()) {
                                listener.onFailure()
                                return
                            }
                            sponsorData = ArrayList()
                            for (sponsor in sponsorMeta.sponsors) {
                                sponsorData?.add(
                                    SponsorItemBean(
                                        sponsor.name,
                                        sponsor.time,
                                        sponsor.amount
                                    )
                                )
                            }
                            listener.onSuccessful(sponsorData)
                        }.getOrElse { e ->
                            Logging.e("Load Sponsor Data", e.toString())
                            listener.onFailure()
                        }
                    }
                    isChecking = false
                }
            }, PathAndUrlManager.URL_GITHUB_HOME + "sponsor.json", if (token == "DUMMY") null else token).enqueue()
        }
    }

    interface CheckListener {
        fun onFailure()

        fun onSuccessful(data: List<SponsorItemBean>?)
    }
}
