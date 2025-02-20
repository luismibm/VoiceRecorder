package com.luismibm.voicerecorder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.luismibm.voicerecorder.databinding.FragmentFirstBinding

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    var REQUEST_RECORD_AUDIO_PERMISSION = 200
    var permissionsToRecordAccepted = false
    var permissions = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)

        ActivityCompat.requestPermissions(this.requireActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION)

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