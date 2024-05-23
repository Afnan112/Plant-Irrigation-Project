
package com.example.plantirrigation

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.plantirrigation.databinding.ActivityMainBinding
import com.example.plantirrigation.databinding.ActivityScanBinding
import com.example.plantirrigation.ml.PlantsModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ScanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanBinding
    private lateinit var imageView: ImageView
    private lateinit var button: Button

    private lateinit var tvOutput: TextView
    private lateinit var tvWater_rate : TextView
    private lateinit var tvPlant_info : TextView

    private val GALLERY_REQUEST_CODE = 123
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        imageView = binding.imageView
        button = binding.btnCaptureImg
        tvOutput = binding.tvOutput
        tvWater_rate = binding.wateRate
        tvPlant_info = binding.plantInfo

        val buttonLoad = binding.btnLoadImg



        button.setOnClickListener {
                takePicturePreview.launch(null)

        }

        buttonLoad.setOnClickListener {

                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                val mimeTypes = arrayOf("image/jpeg","image/png","image/jpg")
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes)
                intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                onresult.launch(intent)

        }
    }




    // launch camera and take picture
    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview()){bitmap->
        if(bitmap != null){
            imageView.setImageBitmap(bitmap)
            outputGenerator(bitmap)
        }
    }
    //to get image from gallery
    private val onresult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        Log.i("TAG","This is the result: ${result.data} ${result.resultCode}")
        onResultReceived(GALLERY_REQUEST_CODE, result)
    }

    private fun onResultReceived(requestCode:Int, result: ActivityResult?){
        when(requestCode){
            GALLERY_REQUEST_CODE ->{
                if(result?.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { uri ->
                        Log.i("TAG", "onResultReceived: $uri")
                        val bitmap =
                            BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
                        imageView.setImageBitmap(bitmap)
                        outputGenerator(bitmap)
                    }
                }else {
                    Log.e("TAG", "onActivityResult: error in selecting image")
                }
            }
        }
    }



    private fun outputGenerator(bitmap: Bitmap){
        val model = PlantsModel.newInstance(this)



// Creates inputs for reference.
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)

        var imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224,224,ResizeOp.ResizeMethod.BILINEAR))
            .build()

        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap)

        tensorImage = imageProcessor.process(tensorImage)

        inputFeature0.loadBuffer(tensorImage.buffer)

// Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray

        var maxIdx = 0
        outputFeature0.forEachIndexed { index, fl ->
            if (outputFeature0[maxIdx] < fl) {
                maxIdx = index
            }
        }

        val labels = application.assets.open("labels.txt").bufferedReader().readLines()

        var label: String = labels[maxIdx]
        tvOutput.setText(label)
        readData(label)

        model.close()

    }

    private fun readData(label: String) {
        database = Firebase.database.reference

        database.child("Plants").child(label).get().addOnSuccessListener {


                val waterRate = it.child("water_rate").value.toString()
                val more = it.child("more").value.toString()
                tvWater_rate.setText(waterRate)
                tvPlant_info.setText(more)
                Toast.makeText(this,"Successfuly Read",Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(this,"Can't Read the Data!!",Toast.LENGTH_SHORT).show()
        }
    }
}