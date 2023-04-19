package com.fypvpreventor.VpreventorFYP.fragments

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.telephony.SmsManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.fypvpreventor.VpreventorFYP.ContactsApplication
import com.fypvpreventor.VpreventorFYP.R
import com.fypvpreventor.VpreventorFYP.database.ContactDao
import com.fypvpreventor.VpreventorFYP.database.Contacts
import com.fypvpreventor.VpreventorFYP.viewmodels.ContactsViewModel
import com.fypvpreventor.VpreventorFYP.viewmodels.ContactsViewModelFactory
import com.fypvpreventor.fypvpreventor.viewmodels.RecordingController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference

import java.util.*
import kotlin.math.sqrt


class SensorFragment : Fragment() {

    //declare variables needed to detect shake event
    private var sensorManager: SensorManager? = null
    private var acceleration = 10f
    private var currentAcceleration = 0f
    private var lastAcceleration = 0f
    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionCode = 101
    private val SEND_SMS_PERMISSION_CODE = 1
    private val MICROPHONE_PERMISSION_CODE = 2
    private lateinit var application: ContactsApplication
    private val CALL_PHONE_REQUEST_CODE = 123
    private lateinit var contactDao: ContactDao

    //reference to viewModel
    private val viewModel: ContactsViewModel by activityViewModels {
        ContactsViewModelFactory(
            ContactsApplication().database.contactsDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        application = requireActivity().application as ContactsApplication
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sensor, container, false)
        val imageView = view.findViewById<ImageView>(R.id.imageView3)

        imageView.setOnClickListener{
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Permission granted, make the call
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:+60108089521") //will change 999
                startActivity(callIntent)
            } else {
                // Permission not granted, request it
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.CALL_PHONE),
                    CALL_PHONE_REQUEST_CODE
                )
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val permissions = arrayOf(
            Manifest.permission.CALL_PHONE,
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        requestPermissions(permissions, permissionCode)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLocation()
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val viewModel = ViewModelProvider(this, ContactsViewModelFactory(application.database.contactsDao()))
            .get(ContactsViewModel::class.java)
        // Request permissions


        // 这里使用 viewModel 对象
        contactDao = application.database.contactsDao()

        currentAcceleration = SensorManager.GRAVITY_EARTH
        lastAcceleration = SensorManager.GRAVITY_EARTH
        if(checkPermission(Manifest.permission.SEND_SMS))
            if (checkPermission(Manifest.permission.RECORD_AUDIO)){
            Objects.requireNonNull(sensorManager)!!.registerListener(sensorListener, sensorManager!!
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
        }else{
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), MICROPHONE_PERMISSION_CODE)
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.SEND_SMS),SEND_SMS_PERMISSION_CODE)
        }

    }

    private val sensorListener: SensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            // Initialize recordingController
            val recordingController = RecordingController(requireActivity() as Activity, contactDao)
            lastAcceleration = currentAcceleration
            currentAcceleration = sqrt((x * x + y * y + z * z).toDouble()).toFloat()
            val delta: Float = currentAcceleration - lastAcceleration
            acceleration = acceleration * 0.9f + delta
            if (acceleration > 12) {
                Toast.makeText(requireContext(), "Shake event detected", Toast.LENGTH_SHORT).show()
                vibratePhone()
                sendMessage()
                sendLocationSMS()
                recordingController.startRecording()
            }
        }
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    private fun vibratePhone() {
        val v = activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        v.vibrate(VibrationEffect.createOneShot(5000, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    private fun sendMessage() {

        if(checkPermission(android.Manifest.permission.SEND_SMS)){
            myMessage()

        }else{
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.SEND_SMS),SEND_SMS_PERMISSION_CODE)
        }
    }

    private fun myMessage(){

        var contactList : List<Contacts>  = listOf()
        var myNumber = ""
        var myMsg = ""

        viewModel.allContacts.observe(this.viewLifecycleOwner){list ->
            contactList = list
        }
        if(contactList.isNotEmpty()){
            for(i in contactList){
                myNumber = i.phoneNumber
                myMsg = i.message
                if(myNumber == "" || myMsg ==""){
                    Toast.makeText(requireContext(),"Cant be empty",Toast.LENGTH_SHORT).show()
                }else{
                    if(TextUtils.isDigitsOnly(myNumber)){
                        val smsManager : SmsManager = SmsManager.getDefault()
                        //val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                        smsManager.sendTextMessage(myNumber,null,myMsg,null,null)
                        Toast.makeText(requireContext(), "Message Sent",Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(requireContext(),"Please Enter a Correct Number",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun fetchLocation() {

        if(ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), permissionCode)
            return
        }
        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                /*Toast.makeText(
                    requireContext(), currentLocation.latitude.toString() + " " +
                            currentLocation.longitude, Toast.LENGTH_SHORT
                ).show()*/
            }
        }
    }


    private fun sendLocationSMS() {
        var contactList: List<Contacts> = listOf()
        var myNumber = ""
        var myMsg = ""
        viewModel.allContacts.observe(this.viewLifecycleOwner) { list ->
            contactList = list
        }
        if (contactList.isNotEmpty()) {
            for (i in contactList) {
                myNumber = i.phoneNumber
                myMsg = i.message
                if (myNumber == "" || myMsg == "") {
                    Toast.makeText(requireContext(), "Cant be empty", Toast.LENGTH_SHORT).show()
                } else {
                    if (::currentLocation.isInitialized){
                        if (TextUtils.isDigitsOnly(myNumber)) {
                            val smsManager = SmsManager.getDefault()
                            val smsBody = StringBuffer()
                            smsBody.append("http://maps.google.com?q=")
                            smsBody.append(currentLocation.latitude)
                            smsBody.append(",")
                            smsBody.append(currentLocation.longitude)
                            smsManager.sendTextMessage(
                                myNumber,
                                null,
                                smsBody.toString(),
                                null,
                                null
                            )
                            Toast.makeText(requireContext(), "Location Message Sent",Toast.LENGTH_SHORT).show()
                        }
                        else{
                            Toast.makeText(requireContext(),"Please open GPS",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            permissionCode -> {
                // Check if all permissions are granted
                var allGranted = true
                for (i in grantResults.indices) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        allGranted = false
                        break
                    }
                }

                if (allGranted) {
                    // All permissions are granted, continue with the operation
                    if (checkPermission(Manifest.permission.SEND_SMS) && checkPermission(Manifest.permission.RECORD_AUDIO)) {
                        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)
                    }
                } else {
                    // At least one permission is not granted, show a message or take other actions
                    Toast.makeText(requireContext(), "Some permissions are not granted", Toast.LENGTH_SHORT).show()
                }
            }
            // Add other cases for other permission requests if necessary
        }
    }



    private fun checkPermission(permission : String): Boolean{
        val check = ContextCompat.checkSelfPermission(requireContext(),permission)
        return check == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        sensorManager?.registerListener(sensorListener, sensorManager!!.getDefaultSensor(
            Sensor .TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL
        )
        super.onResume()
    }

    override fun onPause() {
        sensorManager!!.unregisterListener(sensorListener)
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        sensorManager?.unregisterListener(sensorListener)
    }

}

