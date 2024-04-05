import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.careerhub.R
import com.example.careerhub.databinding.FragmentEditProfileBinding
import com.example.careerhub.utils.SessionManager
import com.example.careerhub.viewmodel.InformationUserViewModel
import com.mukesh.BuildConfig
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class EditProfileFragment : Fragment() {

    private val PICK_IMAGE_REQUEST = 1
    private var selectedImageUri: Uri? = null
    private lateinit var binding: FragmentEditProfileBinding
    private val sessionManager: SessionManager by lazy { SessionManager(requireContext()) }
    private lateinit var infoUser: InformationUserViewModel

    private val REQUEST_EXTERNAL_STORAGE = 1
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickImageFromGallery()
        } else {
            Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        infoUser = ViewModelProvider(this).get(InformationUserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Request storage permissions
        verifyStoragePermissions()

        val id = sessionManager.username.toString()
        infoUser.getUser(id)
        infoUser.user.observe(viewLifecycleOwner) { user ->
            val linkImage = "http://139.59.242.16/${user.jobseeker[0].avatar}"
            Glide.with(this)
                .load(linkImage)
                .transform(CircleCrop())
                .placeholder(R.drawable.img_illustrations_r)
                .into(binding.profileImage)
            binding.position.setText(user.jobseeker[0].email)
            binding.name.setText(user.jobseeker[0].name)
            binding.mobileNumber.setText(user.jobseeker[0].phone)
            binding.location.setText(user.jobseeker[0].address)
            binding.national.setText(user.jobseeker[0].national)
            binding.birthday.setText(user.jobseeker[0].birthday)
        }

        binding.profileImage.setOnClickListener {
            checkForPermission()
        }

        binding.applyNowButton.setOnClickListener {
            // Handle the update button click here
            if (selectedImageUri != null) {
                if (!validateInput()) {
                    return@setOnClickListener
                }
                val filePath = getPathFromUri(selectedImageUri!!)
                if (filePath != null) {
                    val file = File(filePath)
                    uploadImageToServer(file)
                } else {
                    Toast.makeText(requireContext(), "Unable to get image path", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verifyStoragePermissions() {
        // Check if we have write permission
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // We don't have permission, so prompt the user
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK) {
            data?.data?.also { uri ->
                selectedImageUri = uri
                // Use Glide or other libraries to load the image from MediaStore Uri
                Glide.with(this)
                    .load(selectedImageUri)
                    .transform(CircleCrop())
                    .placeholder(R.drawable.img_illustrations_r)
                    .into(binding.profileImage)
            }
        }
    }

    private fun getPathFromUri(uri: Uri): String? {
        var realPath: String? = null
        val wholeID = DocumentsContract.getDocumentId(uri)
        val id = wholeID.split(":").toTypedArray()[1]
        val column = arrayOf(MediaStore.Images.Media.DATA)
        val sel = MediaStore.Images.Media._ID + "=?"
        val cursor: Cursor? = requireContext().contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            column, sel, arrayOf(id), null
        )
        val columnIndex = cursor?.getColumnIndex(column[0])
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                realPath = cursor.getString(columnIndex!!)
            }
            cursor.close()
        }
        return realPath
    }

    private fun checkForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                intent.data = uri
                startActivity(intent)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
                )
            } else {
                pickImageFromGallery()
            }
        }
    }

    private fun uploadImageToServer(file: File) {
        val client = OkHttpClient()
        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        val multipartBody = MultipartBody.Part.createFormData("image", file.name, requestBody)
        val request = Request.Builder()
            .url("https://your-server.com/upload")
            .post(multipartBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle the error
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle the response
            }
        })
    }

    private fun validateInput(): Boolean {
        with(binding) {
            if (position.text.toString().isEmpty()) {
                position.error = "Please enter your position"
                return false
            }
            if (name.text.toString().isEmpty()) {
                name.error = "Please enter your name"
                return false
            }
            if (mobileNumber.text.toString().isEmpty()) {
                mobileNumber.error = "Please enter your mobile number"
                return false
            }
            if (location.text.toString().isEmpty()) {
                location.error = "Please enter your location"
                return false
            }
            if(binding.radioButton.isChecked())
            {
                binding.radioButton2.isChecked = false
            }
            if(binding.radioButton2.isChecked())
            {
                binding.radioButton.isChecked = false
            }
            if (!binding.radioButton.isChecked && !binding.radioButton2.isChecked) {
                Toast.makeText(requireContext(), "Please select your gender", Toast.LENGTH_SHORT).show()
                return false
            }
            if (national.text.toString().isEmpty()) {
                national.error = "Please enter your nationality"
                return false
            }
            if (national.text.toString().isEmpty()) {
                national.error = "Please enter your nationality"
                return false
            }
            if (!Regex("^\\d{4}-\\d{2}-\\d{2}$").matches(birthday.text.toString())) {
                birthday.error = "Please enter your birthday in format yyyy-mm-dd"
                return false
            }
            if (birthday.text.toString().isEmpty()) {
                birthday.error = "Please enter your birthday"
                return false
            }
        }
        return true
    }
}