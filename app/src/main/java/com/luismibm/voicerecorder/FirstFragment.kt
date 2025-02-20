package com.luismibm.voicerecorder

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.luismibm.voicerecorder.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    var REQUEST_RECORD_AUDIO_PERMISSION = 200
    var permissionsToRecordAccepted = false
    var permissions = arrayOf(Manifest.permission.RECORD_AUDIO)

    var mediaRecorder: MediaRecorder? = null
    var mediaPlayer: MediaPlayer? = null


    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        ActivityCompat.requestPermissions(this.requireActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION)

        val logo = binding.logo
        val record = binding.record
        val stop = binding.stop
        val play = binding.play

        var route = getContext()?.getExternalFilesDir(null)?.absolutePath
        var fileName = "$route/audiorecord.3gp"

        record.setOnClickListener {
            logo.setImageDrawable(resources.getDrawable(R.drawable.recording))
            if (mediaRecorder == null) {
                mediaRecorder = MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setOutputFile(fileName)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    try {
                        prepare()
                        start()
                    } catch (e: Exception) {
                        Log.e("RECORDING", "can't start recording")
                    }
                }
            }

        }

        stop.setOnClickListener {
            logo.setImageResource(R.drawable.logo)
            if (mediaRecorder != null) {
                mediaRecorder?.apply {
                    stop()
                    reset()
                    release()
                }
                mediaRecorder = null
            } else if (mediaPlayer != null) {
                mediaPlayer?.apply {
                    stop()
                    release()
                }
                mediaPlayer = null
            }
        }

        play.setOnClickListener {
            logo.setImageResource(R.drawable.playing)
            if (mediaRecorder == null && mediaPlayer == null) {
                mediaPlayer = MediaPlayer().apply {
                    try {
                        setDataSource(fileName)
                        prepare()
                        start()
                        setOnCompletionListener {
                            stop.callOnClick()
                        }
                    } catch (e: Exception) {
                        Log.e("RECORDING", "can't start reproducing")
                    }
                }
            }
        }

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) { REQUEST_RECORD_AUDIO_PERMISSION -> {
            permissionsToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (!permissionsToRecordAccepted) {
                Toast.makeText(requireContext(), "PERMISSION NEEDED", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
        }}
    }

}