package com.youhajun.core.share

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShareUtil @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    companion object {
        private const val MIME_TEXT = "text/plain"
    }

    fun shareTextChooser(chooserTitle: String, message: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = MIME_TEXT
            putExtra(Intent.EXTRA_TEXT, message)
        }
        val shareIntent = Intent.createChooser(intent, chooserTitle)
        context.startActivity(shareIntent)
    }

    fun shareTextChooser(message: String) = shareTextChooser("", message)

    fun copyText(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
    }

    fun copyText(text: String) = copyText("", text)
}