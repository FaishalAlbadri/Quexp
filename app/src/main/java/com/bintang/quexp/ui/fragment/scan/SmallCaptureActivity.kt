package com.bintang.quexp.ui.fragment.scan

import android.view.View
import com.bintang.quexp.R
import com.journeyapps.barcodescanner.CaptureActivity
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class SmallCaptureActivity : CaptureActivity() {
    override fun initializeContent(): DecoratedBarcodeView {
        setContentView(R.layout.custom_scan_qrcode)
        return findViewById<View>(R.id.qrcode) as DecoratedBarcodeView
    }
}