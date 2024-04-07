package com.bintang.quexp.ui.fragment.scan

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.Settings
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bintang.quexp.R
import com.bintang.quexp.databinding.FragmentZxingQrCodeBinding
import com.bintang.quexp.ui.animation.Preset
import com.bintang.quexp.util.createAlertDialog
import com.bintang.quexp.util.viewmodel.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanOptions
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.Locale

class ZxingQrCodeFragment : Fragment(), TextToSpeech.OnInitListener {

    private var _binding: FragmentZxingQrCodeBinding? = null
    val binding get() = _binding!!

    private lateinit var viewModel: ViewModelFactory
    private val qrCodeViewModel: QrCodeViewModel by viewModels { viewModel }

    private lateinit var loading: AlertDialog

    private lateinit var viewKonfetti: KonfettiView

    private lateinit var mediaPlayer: MediaPlayer

    private lateinit var tts: TextToSpeech

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private var placeId = ""
    private lateinit var scanOptions: ScanOptions

    private val fragmentLauncher = registerForActivityResult(ScanContract()) {
        if (it.contents != null) {
            if (it.contents.startsWith("quexp-")) {
                placeId = it.contents
                getLocation()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentZxingQrCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        viewModel = ViewModelFactory.getInstance(requireContext())
        viewKonfetti = binding.viewKonfetti

        setupPermissions()
        createLoading()

        qrCodeViewModel.apply {
            isLoading.observe(viewLifecycleOwner) {
                showLoading(it)
            }
            message.observe(viewLifecycleOwner) {
                it.getContentIfNotHandled()?.let {
                    binding.txtScan.text = it
                    if (it.startsWith("Berhasil")) {
                        soundEffect()
                        explode()
                    }
                }
            }
            placeDesc.observe(viewLifecycleOwner){
                it.getContentIfNotHandled()?.let {
                    binding.txtTts.apply {
                        text = it
                    }
                }
            }
        }

        setupScan()
        setupTTS()

        binding.apply {

            btnScan.setOnClickListener {
                fragmentLauncher.launch(scanOptions)
            }

            btnTts.setOnClickListener {
                if (txtTts.text.isNotEmpty()){
                    if (txtTtsStatus.text.equals("Play")) {
                        btnTts.setImageDrawable(resources.getDrawable(R.drawable.ic_btn_stop, requireContext().theme))
                        txtTtsStatus.text = "Stop"
                        tts.speak(txtTts.text.toString(), TextToSpeech.QUEUE_FLUSH, null, "")
                    } else {
                        btnTts.setImageDrawable(resources.getDrawable(R.drawable.ic_btn_play, requireContext().theme))
                        txtTtsStatus.text = "Play"
                        tts.stop()
                    }
                }
            }
        }
    }

    private fun setupTTS() {
        tts = TextToSpeech(activity, this, "com.google.android.tts")
        binding.btnTts.isEnabled = false
        tts.setOnUtteranceProgressListener(object  : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
            }

            override fun onDone(utteranceId: String?) {
                binding.apply {
                    btnTts.setImageDrawable(resources.getDrawable(R.drawable.ic_btn_play, requireContext().theme))
                    txtTtsStatus.text = "Play"
                }
            }

            override fun onError(utteranceId: String?) {
                binding.apply {
                    btnTts.setImageDrawable(resources.getDrawable(R.drawable.ic_btn_play, requireContext().theme))
                    txtTtsStatus.text = "Play"
                }
                Toast.makeText(requireContext(), "Error text to speech", Toast.LENGTH_SHORT).show()
            }

        })
        tts.setSpeechRate(0.7F)
    }

    private fun setupScan() {
        scanOptions = ScanOptions()
        scanOptions.apply {
            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            setPrompt("Quexp QR Code")
            setOrientationLocked(false)
            setBeepEnabled(false)
            setTorchEnabled(false)
            setCaptureActivity(SmallCaptureActivity::class.java)
        }

        binding.btnFlashlight.setOnCheckedChangeListener({ view, isChecked ->
            if (isChecked) {
                scanOptions.setTorchEnabled(true)
            } else {
                scanOptions.setTorchEnabled(false)
            }
        })

        fragmentLauncher.launch(scanOptions)
    }

    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location != null) {
                        val geocoder = Geocoder(requireContext(), Locale.getDefault())
                        val list: List<Address> =
                            geocoder.getFromLocation(
                                location.latitude,
                                location.longitude,
                                1
                            ) as List<Address>
                        val latitude = list[0].latitude
                        val longitude = list[0].longitude
                        qrCodeViewModel.scanQrCode(placeId, latitude, longitude)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_REQ
        )
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),

                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),

                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun setupPermissions() {
        val permission =
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.CAMERA)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(android.Manifest.permission.CAMERA),
            CAMERA_REQ
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_REQ -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        requireContext(),
                        "You need the camera permission to use this app",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            LOCATION_REQ -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getLocation()
                }
            }
        }
    }

    private fun createLoading() {
        loading = createAlertDialog(requireContext())
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            loading.show()
        } else {
            loading.dismiss()
        }
    }

    companion object {
        private const val CAMERA_REQ = 101
        private const val LOCATION_REQ = 69
    }

    private fun explode() {
        for (i in 1..3) {
            viewKonfetti.start(Preset.explode())
        }
    }

    private fun soundEffect() {
        mediaPlayer = MediaPlayer.create(requireActivity(), com.bintang.quexp.R.raw.beep)
        mediaPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (tts.isSpeaking) {
            tts.stop()
        }
        tts.shutdown()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale("id", "ID"))

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(requireContext(), "Text to speech not support", Toast.LENGTH_SHORT).show()
            } else {
                binding.btnTts.isEnabled = true
            }
        }
    }
}