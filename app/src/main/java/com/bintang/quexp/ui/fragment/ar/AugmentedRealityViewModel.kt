package com.bintang.quexp.ui.fragment.ar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bintang.quexp.data.local.ARData

class AugmentedRealityViewModel : ViewModel() {

    private val _arData = MutableLiveData<MutableList<ARData>>()
    val arData: LiveData<MutableList<ARData>> = _arData

    val dataAR: List<ARData> = listOf(
        ARData(0,"Candi Apit", "", "models/candiapit.glb", 0),
        ARData(1, "Gerbang", "", "models/gerbang.glb", 0),
        ARData(2, "Temple", "", "models/temple.glb", 0),
        ARData(3, "Tugu", "", "models/tugu.glb", 0),
    )

    fun getArData(){
        _arData.value = dataAR as MutableList<ARData>
    }

}