package com.testziliao.app.data.remote

import com.testziliao.app.BuildConfig

object ContentUrlResolver {
    fun resolve(pathOrUrl: String): String {
        if (pathOrUrl.startsWith("http://") || pathOrUrl.startsWith("https://")) {
            return pathOrUrl
        }

        val base = BuildConfig.CONTENT_BASE_URL.trimEnd('/')
        val path = pathOrUrl.trimStart('/')
        return "$base/$path"
    }
}
