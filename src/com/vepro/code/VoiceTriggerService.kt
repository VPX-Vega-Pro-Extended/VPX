package com.vepro.code

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import java.util.Locale

/**
 * Always-on voice trigger for VPX.
 *
 * Listens for a short speech segment and opens MainActivity when
 * the trigger phrase "هی وی پی ایکس" is detected.
 *
 * The recognizer is restarted after every result/error so the service
 * can continue listening without keeping a single recognizer instance
 * alive indefinitely.
 */
class VoiceTriggerService : Service() {

    companion object {

        private const val CHANNEL_ID = "vepro_voice_trigger"
        private const val NOTIFICATION_ID = 4023

        private const val ACTION_START =
            "com.vepro.code.START_VOICE_TRIGGER"

        private const val ACTION_STOP =
            "com.vepro.code.STOP_VOICE_TRIGGER"

        private const val RESTART_DELAY_MS = 350L

        fun start(context: android.content.Context) {

            val intent = Intent(
                context,
                VoiceTriggerService::class.java
            ).setAction(ACTION_START)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: android.content.Context) {

            val intent = Intent(
                context,
                VoiceTriggerService::class.java
            ).setAction(ACTION_STOP)

            context.startService(intent)
        }
    }

    private var recognizer: SpeechRecognizer? = null
    private var restartPending = false

    private val restartRunnable = Runnable {
        restartPending = false
        startListening()
    }

    override fun onCreate() {
        super.onCreate()

        VpxLogger.info("VoiceTriggerService", "onCreate")

        if (!Prefs(this).voiceTriggerEnabled()) {
            stopSelf()
            return
        }

        createNotificationChannel()

        startForeground(
            NOTIFICATION_ID,
            buildNotification()
        )

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            checkSelfPermission(
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            stopSelf()
            return
        }

        if (!SpeechRecognizer.isRecognitionAvailable(this)) {
            stopSelf()
            return
        }

        createRecognizer()
        startListening()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(
        intent: Intent?,
        flags: Int,
        startId: Int
    ): Int {

        VpxLogger.info(
            "VoiceTriggerService",
            "onStartCommand action=${intent?.action}"
        )

        when (intent?.action) {

            ACTION_STOP -> {
                VpxLogger.info(
                    "VoiceTriggerService",
                    "ACTION_STOP received"
                )

                Prefs(this).setVoiceTriggerEnabled(false)
                stopListeningAndDestroy()
                stopForeground(true)
                stopSelf()
                return START_NOT_STICKY
            }

            ACTION_START,
            null -> {
                VpxLogger.info(
                    "VoiceTriggerService",
                    "ACTION_START received enabled=${Prefs(this).voiceTriggerEnabled()}"
                )
                if (!Prefs(this).voiceTriggerEnabled()) {
                    stopListeningAndDestroy()
                    stopSelf()
                    return START_NOT_STICKY
                }

                if (recognizer == null) {
                    createRecognizer()
                }

                startListening()
            }
        }

        return START_NOT_STICKY
    }

    private fun stopListeningAndDestroy() {
        restartPending = false
        android.os.Handler(
            android.os.Looper.getMainLooper()
        ).removeCallbacks(restartRunnable)

        try {
            recognizer?.cancel()
        } catch (_: Throwable) {
        }

        try {
            recognizer?.destroy()
        } catch (_: Throwable) {
        }

        recognizer = null
    }

    private fun createRecognizer() {
        VpxLogger.info(
            "VoiceTriggerService",
            "createRecognizer"
        )

        recognizer?.destroy()

        recognizer =
            SpeechRecognizer.createSpeechRecognizer(this)

        recognizer?.setRecognitionListener(
            object : RecognitionListener {

                override fun onReadyForSpeech(
                    params: android.os.Bundle?
                ) {
                }

                override fun onBeginningOfSpeech() {
                }

                override fun onRmsChanged(
                    rmsdB: Float
                ) {
                }

                override fun onBufferReceived(
                    buffer: ByteArray?
                ) {
                }

                override fun onEndOfSpeech() {
                    scheduleRestart()
                }

                override fun onError(
                    error: Int
                ) {
                    scheduleRestart()
                }

                override fun onResults(
                    results: android.os.Bundle?
                ) {

                    val matches =
                        results?.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                        )

                    if (matches != null) {
                        for (text in matches) {
                            android.util.Log.d("VPX_VOICE", "partial=[$text] phrase=[${Prefs(this@VoiceTriggerService).voiceTriggerPhrase()}] trigger=${isTrigger(text)}")
                            if (isTrigger(text)) {
                                openMainActivity()
                                break
                            }
                        }
                    }

                    scheduleRestart()
                }

                override fun onPartialResults(
                    partialResults: android.os.Bundle?
                ) {

                    val matches =
                        partialResults?.getStringArrayList(
                            SpeechRecognizer.RESULTS_RECOGNITION
                        )

                    if (matches != null) {
                        for (text in matches) {
                            android.util.Log.d("VPX_VOICE", "partial=[$text] phrase=[${Prefs(this@VoiceTriggerService).voiceTriggerPhrase()}] trigger=${isTrigger(text)}")
                            if (isTrigger(text)) {
                                openMainActivity()
                                break
                            }
                        }
                    }
                }

                override fun onEvent(
                    eventType: Int,
                    params: android.os.Bundle?
                ) {
                }
            }
        )
    }

    private fun startListening() {
        VpxLogger.debug(
            "VoiceTriggerService",
            "startListening"
        )

        val speech = recognizer ?: return

        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
            checkSelfPermission(
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        try {

            speech.cancel()

            val intent =
                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {

                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )

                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE,
                        "fa-IR"
                    )

                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                        "fa-IR"
                    )

                    putExtra(
                        RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                        true
                    )

                    putExtra(
                        RecognizerIntent.EXTRA_MAX_RESULTS,
                        5
                    )

                    putExtra(
                        RecognizerIntent.EXTRA_CALLING_PACKAGE,
                        packageName
                    )
                }

            speech.startListening(intent)

        } catch (_: Exception) {

            scheduleRestart()
        }
    }

    private fun scheduleRestart() {

        if (restartPending) {
            return
        }

        restartPending = true

        android.os.Handler(
            android.os.Looper.getMainLooper()
        ).postDelayed(
            restartRunnable,
            RESTART_DELAY_MS
        )
    }

    private fun isTrigger(text: String): Boolean {

        val normalized =
            text
                .trim()
                .lowercase(Locale.ROOT)
                .replace(
                    Regex("[\\u200c\\u200f\\u202a-\\u202e]"),
                    ""
                )
                .replace(
                    Regex("\\s+"),
                    " "
                )

        val phrase =
            Prefs(this)
                .voiceTriggerPhrase()
                .trim()
                .lowercase(Locale.ROOT)
                .replace(
                    Regex("[\\u200c\\u200f\\u202a-\\u202e]"),
                    ""
                )
                .replace(
                    Regex("\\s+"),
                    " "
                )

        if (phrase.isEmpty()) {
            return false
        }

        val compact =
            normalized.replace(" ", "")

        val compactPhrase =
            phrase.replace(" ", "")

        return normalized.contains(phrase) ||
            compact.contains(compactPhrase)
    }

    private fun openMainActivity() {
        VpxLogger.info(
            "VoiceTriggerService",
            "openMainActivity requested"
        )

        val intent =
            Intent(this, MainActivity::class.java).apply {
                flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP

                putExtra("voice_trigger", true)
            }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                val creatorOptions =
                    android.app.ActivityOptions.makeBasic().apply {
                        setPendingIntentCreatorBackgroundActivityStartMode(
                            android.app.ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
                        )
                    }

                val pendingIntent =
                    PendingIntent.getActivity(
                        this,
                        7702,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or
                            PendingIntent.FLAG_IMMUTABLE,
                        creatorOptions.toBundle()
                    )

                val sendOptions =
                    android.app.ActivityOptions.makeBasic().apply {
                        setPendingIntentBackgroundActivityStartMode(
                            android.app.ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
                        )
                    }

                VpxLogger.info(
                    "VoiceTriggerService",
                    "sending MainActivity PendingIntent with BAL allowed"
                )

                pendingIntent.send(
                    this,
                    0,
                    null,
                    null,
                    null,
                    null,
                    sendOptions.toBundle()
                )
            } else {
                startActivity(intent)
            }

        } catch (e: Exception) {
            VpxLogger.error(
                "VoiceTriggerService",
                "openMainActivity failed: ${e.javaClass.simpleName}: ${e.message}"
            )
        }
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        val manager =
            getSystemService(
                NotificationManager::class.java
            )

        val channel =
            NotificationChannel(
                CHANNEL_ID,
                "VPX voice assistant",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description =
                    "Keeps VPX voice activation available."
            }

        manager.createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {

        val stopIntent = Intent(this, VoiceTriggerService::class.java).apply {
            action = ACTION_STOP
        }

        val stopPendingIntent = PendingIntent.getService(
            this,
            7701,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE
                } else {
                    0
                }
        )

        return if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        ) {

            Notification.Builder(
                this,
                CHANNEL_ID
            )
                .setContentTitle("VPX")
                .setContentText(
                    "فعال‌سازی صوتی VPX فعال است"
                )
                .setSmallIcon(
                    android.R.drawable.ic_btn_speak_now
                )
                .setOngoing(true)
                .addAction(
                    Notification.Action.Builder(
                        null,
                        "غیرفعال‌سازی",
                        stopPendingIntent
                    ).build()
                )
                .build()

        } else {

            @Suppress("DEPRECATION")
            Notification.Builder(this)
                .setContentTitle("VPX")
                .setContentText(
                    "فعال‌سازی صوتی VPX فعال است"
                )
                .setSmallIcon(
                    android.R.drawable.ic_btn_speak_now
                )
                .setOngoing(true)
                .addAction(
                    Notification.Action(
                        0,
                        "غیرفعال‌سازی",
                        stopPendingIntent
                    )
                )
                .build()
        }
    }
}
