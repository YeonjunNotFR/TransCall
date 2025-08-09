package com.youhajun.core.stt

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import com.youhajun.transcall.core.common.DefaultDispatcher
import com.youhajun.transcall.core.common.MainImmediateDispatcher
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

@ServiceScoped
class StreamSpeechRecognizerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    @MainImmediateDispatcher private val mainDispatcher: CoroutineDispatcher,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher
) : RecognitionListener {

    companion object {
        private const val RESTART_DELAY_MILLIS = 300L
    }

    private var isRunning = false
    private lateinit var locale: Locale
    private val scope = CoroutineScope(SupervisorJob() + mainDispatcher)
    private val recognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
        setRecognitionListener(this@StreamSpeechRecognizerManager)
    }

    private val _resultFlow = MutableStateFlow<SttResult>(SttResult.Idle)
    val resultFlow: StateFlow<SttResult> = _resultFlow.asStateFlow()

    fun start(locale: Locale = Locale.getDefault()) {
        if (isRunning) return
        isRunning = true
        this.locale = locale
        restartListening()
    }

    fun stop() {
        isRunning = false
        scope.launch {
            recognizer.stopListening()
            recognizer.cancel()
        }
    }

    fun destroy() {
        isRunning = false
        scope.launch {
            recognizer.destroy()
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val text = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull().orEmpty()
        if (text.isNotBlank()) _resultFlow.value = SttResult.Partial(text)
    }

    override fun onResults(results: Bundle?) {
        val text = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull().orEmpty()
        if (text.isNotBlank()) _resultFlow.value = SttResult.Final(text)

        restartListening()
    }

    override fun onError(error: Int) {
        _resultFlow.value = SttResult.Error(error)
        restartListening()
    }


    private fun restartListening() {
        scope.launch {
            withContext(defaultDispatcher) { delay(RESTART_DELAY_MILLIS) }
            if (isRunning) runCatching {
                recognizer.startListening(createIntent())
            }.onFailure {
                _resultFlow.value = SttResult.Error(-1)
            }
        }
    }

    private fun createIntent(): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
    }

    override fun onReadyForSpeech(params: Bundle?) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() {}
    override fun onEvent(eventType: Int, params: Bundle?) {}
}
