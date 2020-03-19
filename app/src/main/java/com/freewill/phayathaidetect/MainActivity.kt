package com.freewill.phayathaidetect

import android.Manifest
import android.app.Activity
import android.app.Application
import android.app.PendingIntent.getActivity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.fg.mdp.fwgfacilitiesfinder.clients.APIClient
import com.fg.mdp.fwgfacilitiesfinder.model.responsSend.Information
import com.fg.mdp.fwgfacilitiesfinder.model.responsSend.NurseItem
import com.freewill.phayathaidetect.extension.delayFunction
import com.freewill.phayathaidetect.extension.getIMEI
import com.freewill.phayathaidetect.extension.toast
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.kevinvista.bluetoothscanner.Device
import me.kevinvista.bluetoothscanner.DeviceAdapter
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {


    //https://github.com/kevin-vista/bluetooth-scanner

    private val ACCESS_COARSE_LOCATION_CODE = 1

    private val REQUEST_ENABLE_BLUETOOTH = 2

    private val SCAN_MODE_ERROR = 3

    private var bluetoothReceiverRegistered: Boolean = false

    private var scanModeReceiverRegistered: Boolean = false

    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    internal var textDeviceName: TextView? = null

    internal var textDeviceMac: TextView? = null

    internal var textDevicePaired: TextView? = null

    internal var textDeviceSignal: TextView? = null

    private var mBluetoothAdapter: BluetoothAdapter? = null

    private val mBluetoothDevice: BluetoothDevice? = null

    private var recyclerView: RecyclerView? = null

    private var deviceAdapter: DeviceAdapter? = null

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    private var devices: ArrayList<Device>? = ArrayList()

    private var checkBluetoothStop = true

    private val handler = Handler()
    internal var scanTask: Runnable = object : Runnable {
        override fun run() {

//            Executors.newSingleThreadScheduledExecutor().schedule({
//                scanBluetooth()
//            }, 10, TimeUnit.SECONDS)

        //  handler.postDelayed(this, 5000)


            GlobalScope.launch(context = Dispatchers.Main) {
                println("launched coroutine 1")
                Log.e("launched coroutine 1","launched coroutine 1")
                // repeat(5, {TimeUnit.SECONDS})
//                    delay(5000)
//                    get(devices)

                repeat(5000) {
                   // Log.e("♥️", "AOM BNK48")
                    scanBluetooth()
                    delay(5000)
                }

                // scanBluetooth()
            }

            

        }
    }

    private val bluetoothReceiver = object : BroadcastReceiver() {


        override fun onReceive(context: Context, intent: Intent) {
            Log.e(TAG, "onReceive: Execute")
            val action = intent.action


            if (BluetoothDevice.ACTION_FOUND == action) {
                val device =
                    intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                val deviceName = device.name
                val paired = device.bondState == BluetoothDevice.BOND_BONDED
                val deviceAddress = device.address
                val deviceRSSI = intent.extras!!.getShort(BluetoothDevice.EXTRA_RSSI, 0.toShort())
                val mDevice = Device(deviceName, paired, deviceAddress, deviceRSSI)

                devices?.remove(scannedDevice(mDevice))
                // devices?.clear()
                devices?.add(mDevice)
                deviceAdapter?.notifyDataSetChanged()
                 get(devices)


//                GlobalScope.launch(context = Dispatchers.Main) {
//                    println("launched coroutine 1")
//                    Log.e("launched coroutine 1","launched coroutine 1")
//                    // repeat(5, {TimeUnit.SECONDS})
////                    delay(5000)
////                    get(devices)
//
//                    repeat(5000) {
//                        Log.e("♥️", "AOM BNK48")
//                        delay(5000)
//                    }
//
//                    // scanBluetooth()
//                }



//                Executors.newSingleThreadScheduledExecutor().schedule({
//                    get(devices)
//                }, 5, TimeUnit.SECONDS)

//                Timer().schedule(object : TimerTask() {
//                    override fun run() {
//                        get(devices)
//                    }
//                }, 2500)


//            delayFunction({
//
//                     get(devices)
//
//                }, 5000)

                   // get(devices)


//                if (checkBluetoothStop == false) {
//
//                } else {
//                    checkBluetoothStop = false
//                    checkBluetooth(true)
//                }


            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
                if (devices?.size == 0) {
                    Log.e(TAG, "onReceive: No device")
                }
            }
        }

        private fun scannedDevice(d: Device): Device? {
            for (device in devices!!) {
                if (d.address == device.address) {
                    return device
                }
            }
            return null
        }

        /*private boolean containsDevice(Device d) {
            for (Device device : devices) {
                if (d.getAddress().equals(device.getAddress())) {
                    return true;
                }
            }
            return false;
        }*/
    }


    private val scanModeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, SCAN_MODE_ERROR)
            if (scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE || scanMode == BluetoothAdapter.SCAN_MODE_NONE) {
                Toast.makeText(context, "อุปกรณ์มองไม่เห็นจากด้านนอก", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contextOfApplication = applicationContext


        initView()
        //Request Permission

        initData()
        handler.post(scanTask)
        deviceAdapter?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()

        if (checkBluetoothStop) {
            checkBluetooth(true)
        }

        handler.post(scanTask)
    }


    override fun onRestart() {
        super.onRestart()
        handler.post(scanTask)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bluetoothReceiverRegistered) {
            unregisterReceiver(bluetoothReceiver)
        }
        if (scanModeReceiverRegistered) {
            unregisterReceiver(scanModeReceiver)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.set_visible -> {
                val visibleIntent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
                visibleIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 120)
                startActivity(visibleIntent)
                Toast.makeText(this, "โปรดกลับสู่แอปเพื่อขออนุมัติ", Toast.LENGTH_LONG).show()
                //register scanModeReceiver
                scanModeReceiverRegistered = true
                val intentFilter = IntentFilter()
                intentFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)
                registerReceiver(scanModeReceiver, intentFilter)

            }
            R.id.settings -> {
//                val intent = Intent(this, SettingsFragment::class.java)
//                startActivity(intent)
            }
            R.id.about -> Toast.makeText(this, "Written by kevin-vista", Toast.LENGTH_SHORT).show()
            R.id.feedback -> {
                val emailIntent = Intent(Intent.ACTION_SENDTO)
                emailIntent.data = Uri.parse("mailto:kevin-vista@outlook.com")
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback - Bluetooth Scanner")
                startActivity(emailIntent)
            }
            else -> {
            }
        }
        return true
    }

    private fun initView() {
        swipeRefreshLayout = findViewById(R.id.swipe_refresh) as SwipeRefreshLayout
        swipeRefreshLayout!!.setColorSchemeResources(R.color.colorPrimary)
        swipeRefreshLayout!!.setOnRefreshListener(this)
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView

        devices = ArrayList<Device>()
        deviceAdapter = DeviceAdapter(devices!!)
        recyclerView!!.adapter = deviceAdapter
        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
    }

    private fun initData() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    }

    suspend  fun scanBluetooth() {
        bluetoothReceiverRegistered = true
        val filter = IntentFilter()
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(bluetoothReceiver, filter)
        if (mBluetoothAdapter?.isDiscovering!!) {
            mBluetoothAdapter?.cancelDiscovery()
        }
        mBluetoothAdapter!!.startDiscovery()
    }

    override fun onRefresh() {
        runOnUiThread {
            if (mBluetoothAdapter != null) {
                if (!mBluetoothAdapter!!.isEnabled) {
                    //mBluetoothAdapter.enable();
                    val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                    startActivityForResult(enableIntent, REQUEST_ENABLE_BLUETOOTH)
                }
                handler.post(scanTask)
            }
            deviceAdapter!!.notifyDataSetChanged()
            swipeRefreshLayout!!.isRefreshing = false
        }
    }

    companion object {

        val TAG = "MainActivity"

        var contextOfApplication: Context? = null
    }


    fun get(devices: MutableList<Device>?) {

        //checkLoadProcessBar.value = LoadProcessBar(false)


        var dataList: ArrayList<NurseItem> = ArrayList()


        devices?.forEach { devices ->
            devices.let {

            }?.let {
                dataList.add(
                    NurseItem(
                        devices.signal!!,
                        devices?.address.toString(),
                        devices?.name
                    )
                )
            }
        }


        val date = Date()
        val dateFormatWithZone =
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val currentDate = dateFormatWithZone.format(date)


        val map: HashMap<String, Any> = hashMapOf(
            "information" to Information(getIMEI(applicationContext), currentDate),
            "nurse" to dataList
        )
        var gson = Gson()

        /*
          var dataJson = JSONObject()
          dataJson.put("information", Information())
          dataJson.put("nurse", dataList)*/


        val body: RequestBody =
            RequestBody.create(MediaType.parse("application/json"), gson.toJson(map))


        val call = APIClient.client.getSendDetect(body)
        call.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            //  .debounce(150,TimeUnit.MILLISECONDS)
            //.filter()
            //.collect()

            .subscribe(object : Observer<Response<JsonObject>> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {

                    Log.e("Call request", d.toString())
                }

                override fun onNext(response: Response<JsonObject>) {
                    Log.e("Call request onNext", response.toString())

                    //   mainLockerListener?.onStopProcessbar(false)
                    if (response.isSuccessful) {

                        //checkLoadProcessBar.value = com.fg.mdp.fwgfacilitiesfinder.model.login.LoadProcessBar(true)

                        //  if(access == true){
                        // mainLockerListener?.onSuccess(response.body().toString())

                        // }

                        toast("  detected " + "\t" + response.body().toString().toString())




                        Log.e("Call request", response.body().toString())
                    } else {
                        Log.e("Call request error", response.errorBody()?.string())

                        toast("  detected error " + "\t" + response.toString().toString())
                        //  mainLockerListener?.onFailure(response.message())
                    }
                }

                override fun onError(e: Throwable) {
                    // mainLockerListener?.onStopProcessbar(false)
                    //checkLoadProcessBar.value = com.fg.mdp.fwgfacilitiesfinder.model.login.LoadProcessBar(true)
                    Log.e("Call  onError", e.toString())

                    toast("  detected error " + "\t" + e.toString())
                    // mainLockerListener?.onFailure(e.toString())
                }


            })

        //return mutablePost!!
        //  return mutablePostList

    }


    private fun checkBluetooth(checkBluetooth: Boolean) {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
        } else {
            if (!mBluetoothAdapter.isEnabled) {
                // Bluetooth is not enable :)

                if (checkBluetooth == true) {
                    var activity = this
                    if (activity != null) {
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BLUETOOTH)
                    }

                }

            } else {

                requesLocationPermission()

            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == REQUEST_ENABLE_BLUETOOTH) {

            // Usuario ha activado el bluetooth
            if (resultCode == Activity.RESULT_OK) {

                Log.e("REQUEST_ENABLE_BLUETOOTH OK", "REQUEST_ENABLE_BLUETOOTH Ok ")
                checkBluetoothStop = true
                requesLocationPermission()

            } else if (resultCode == Activity.RESULT_CANCELED) { // User refuses to enable bluetooth
                // context!!.toast(getString(R.string.no_bluetooth_msg))
                checkBluetoothStop = false

            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun requesLocationPermission() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    if (report!!.areAllPermissionsGranted()) {
                        Toast.makeText(
                            this@MainActivity,
                            "All permissions are granted!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    // check for permanent denial of any permission
                    if (report!!.isAnyPermissionPermanentlyDenied()) {
                        // show alert dialog navigating to Settings
                        //  showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                }

            }).check()
    }


}