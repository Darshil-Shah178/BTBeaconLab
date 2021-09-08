package com.example.btbeaconlab

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var bluetoothAdapter: BluetoothAdapter? = null
    private val data = ArrayList<ItemsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        button.setOnClickListener {
            if (hasPermissions()) {
                startScan()
            } else {
                hasPermissions()
            }
        }
    }

    private var scanResults: HashMap<String, ScanResult>? = null

    companion object {
        const val SCAN_PERIOD: Long = 3000
    }

    private fun startScan() {
        data.clear()
        scanResults = HashMap()
        val scanCallback = BtleScanCallback()
        val bluetoothleScanner = bluetoothAdapter!!.bluetoothLeScanner
        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
            .build()
        val filter: List<ScanFilter>? = null
        val mHandler = Handler()
        mHandler.postDelayed({ bluetoothleScanner.stopScan(scanCallback) }, SCAN_PERIOD)
        var mScanning = true
        bluetoothleScanner!!.startScan(filter, settings, scanCallback)
    }

    private inner class BtleScanCallback : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            addScanResult(result)
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            for (result in results) {
                addScanResult(result)
            }
        }

        override fun onScanFailed(errorCode: Int) {
        }

        private fun addScanResult(result: ScanResult) {
            val device = result.device
            val deviceName = device.name ?: "N/A"
            val deviceAddress = device.address
            scanResults!![deviceAddress] = result
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                data.add(
                    ItemsViewModel(
                        deviceName,
                        result.device.address, result.rssi.toString(), result.isConnectable
                    )
                )
                val adapter = CustomAdapter(data.distinctBy { it.deviceAddress })
                recyclerview.adapter = adapter
            }
        }
    }

    private fun hasPermissions(): Boolean {
        if (bluetoothAdapter == null || !bluetoothAdapter!!.isEnabled) {
            return false
        } else if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1);
            return true // assuming that the user grants permission
        }
        return true
    }
}
