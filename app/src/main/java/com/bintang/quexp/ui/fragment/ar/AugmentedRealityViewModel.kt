package com.bintang.quexp.ui.fragment.ar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintang.quexp.data.local.ARData

class AugmentedRealityViewModel : ViewModel() {

    private val _arData = MutableLiveData<MutableList<ARData>>()
    val arData: LiveData<MutableList<ARData>> = _arData

    private val _discoveredData = MutableLiveData<MutableList<String>>()
    val discoveredData: LiveData<MutableList<String>> = _discoveredData

    val dataAR: List<ARData> = listOf(
        ARData(0,"Candi Apit", "", "models/candiapit.glb", 0),
        ARData(1, "Gerbang", "", "models/gerbang.glb", 0),
        ARData(2, "Temple", "", "models/temple.glb", 0),
        ARData(3, "Tugu", "", "models/tugu.glb", 0),
    )

    val dataDiscovered: List<String> = listOf(
        "Cari tempat yang tidak reflektif.",
        "Gerakkan handphone sesuai dengan arahan kamera (Gerakan memutar).",
        "Terus gerakkan handphone sampai ada titik-titik putih pada permukaan.",
        "Tekan/pencet titik-titik putih untuk bisa memunculkan objek.",
        "Objek yang sudah bisa diputar dan digerakkan.",
        "Jika ingin mengganti objek, tekan pada list objek yang ada pada bagian bawah."
    )

    fun getArData(){
        _arData.value = dataAR as MutableList<ARData>
    }

    fun getDiscoveredData(){
        _discoveredData.value = dataDiscovered as MutableList<String>
    }

}