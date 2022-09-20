package com.neonusa.submission1.ui.add

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.github.drjacky.imagepicker.ImagePicker
import com.neonusa.submission1.core.data.source.remote.network.State
import com.neonusa.submission1.databinding.ActivityAddBinding
import com.squareup.picasso.Picasso
import com.techiness.progressdialoglibrary.ProgressDialog
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import java.io.File

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private val viewModel: AddViewModel by inject()
    private lateinit var progressDialog: ProgressDialog

    private var fileImage: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)

        binding.ivPhoto.setOnClickListener {
            pickImage()
        }

        binding.buttonAdd.setOnClickListener {
            val requestImageFile = fileImage?.asRequestBody("image/*".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                "jpg",
                requestImageFile!!
            )

            val desc = binding.edAddDescription.text.toString().toRequestBody("text/plain".toMediaType())
            val lat = (2).toString().toRequestBody("text/plain".toMediaType())
            val lon = (2).toString().toRequestBody("text/plain".toMediaType())

            viewModel.createStory(imageMultipart,desc,lat,lon).observe(this){
                when (it.state) {
                    State.SUCCESS -> {
                        progressDialog.dismiss()
                        finish()
                    }
                    State.ERROR -> {
                        progressDialog.dismiss()
                        Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
                    }
                    State.LOADING -> {
                        progressDialog.show()
                    }
                }
            }

        }
    }

    private fun pickImage() {
        ImagePicker.with(this)
            .crop()
            .maxResultSize(1080, 1080, true)
            .createIntentFromDialog { launcher.launch(it) }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data!!
            fileImage = File(uri.path ?: "")
            Picasso.get().load(uri).into(binding.ivPhoto)
        }
    }

}