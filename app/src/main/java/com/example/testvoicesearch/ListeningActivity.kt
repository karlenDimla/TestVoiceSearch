package com.example.testvoicesearch

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.testvoicesearch.databinding.ActivityListeningBinding

class ListeningActivity  : AppCompatActivity(), RecognitionListener {

    private lateinit var binding: ActivityListeningBinding
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognizerIntent: Intent
    private var lastResult: String = ""

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListeningBinding.inflate(layoutInflater)
        setContentView(binding.root)

        speechRecognizer = SpeechRecognizer.createOnDeviceSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(this)
        speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_PARTIAL_RESULTS,
            true
        )
    }

    override fun onStart() {
        super.onStart()
         Log.d("KLDIMSUM", "STARTING LISTENER °°°°°°°°°°°°°°°")
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    override fun onStop() {
        super.onStop()
         Log.d("KLDIMSUM", "KILLING LISTENER °°°°°°°°°°°°°°°")
        speechRecognizer.destroy()
    }

    override fun onReadyForSpeech(p0: Bundle?) {
        Log.d("KLDIMSUM", "READY FOR SPEECH °°°°°°°°°°°°°°°°")
    }

    override fun onBeginningOfSpeech() {
        Log.d("KLDIMSUM", "BEGINNING OF SPEECH -------")
    }

    override fun onRmsChanged(p0: Float) {
    }

    override fun onBufferReceived(p0: ByteArray?) {
    }

    override fun onEndOfSpeech() {
        Log.d("KLDIMSUM", "END OF SPEECH -------")
    }

    override fun onError(p0: Int) {
        if (p0 == SpeechRecognizer.ERROR_NO_MATCH) {
            Log.d("KLDIMSUM", "ERROR:NO MATCH")
            restartListener()
        } else if(p0 == SpeechRecognizer.ERROR_CLIENT) {
             Log.d("KLDIMSUM", "ERROR:CLIENT")
            Toast.makeText(this, "ERROR:CLIENT", Toast.LENGTH_LONG).show()
        } else if (p0 == SpeechRecognizer.ERROR_RECOGNIZER_BUSY)   {
             Log.d("KLDIMSUM", "ERROR:BUSY")
        }
    }

    private fun restartListener() {
        Log.d("KLDIMSUM", "RESTARTING LISTENER °°°°°°°°°°°°°°°")
        speechRecognizer.stopListening()
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    // Only called when onBeginningOfSpeech has been called
    override fun onResults(bundle: Bundle) {
        val data = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        binding.recognizedSpeech.text = getUpdatedString(data?.toList())
        Log.d("KLDIMSUM", "Sending to Search: $lastResult")
        Toast.makeText(this, "Sending to Search: $lastResult", Toast.LENGTH_LONG).show()
        onBackPressed()
    }

    @SuppressLint("SetTextI18n")
    override fun onPartialResults(bundle: Bundle?) {
        val data = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        binding.recognizedSpeech.text = getUpdatedString(data?.toList())
        Log.d("KLDIMSUM", "PARTIAL RESULT RECEIVED: $lastResult")
    }

    override fun onEvent(p0: Int, p1: Bundle?) {
        TODO("Not yet implemented")
    }

    private fun getUpdatedString(result: List<String>?): String {
        if (result.isNullOrEmpty()) return ""
        if (result[0].length == lastResult.length) return lastResult
        lastResult = result[0]
        return lastResult
    }
}