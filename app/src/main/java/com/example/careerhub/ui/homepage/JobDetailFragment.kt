package com.example.careerhub.ui.homepage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.careerhub.R
import com.example.careerhub.databinding.FragmentJobDetailBinding
import com.example.careerhub.utils.SessionManager
import com.example.careerhub.viewmodel.ApplyJobViewModel
import com.example.careerhub.viewmodel.InformationUserViewModel
import com.example.careerhub.viewmodel.JobInformationViewModel
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class JobDetailFragment : Fragment() {
    private lateinit var binding: FragmentJobDetailBinding
    private lateinit var jobInformationViewModel: JobInformationViewModel
    private lateinit var inforUser: InformationUserViewModel
    private lateinit var jobapply: ApplyJobViewModel
    private val args: JobDetailFragmentArgs by navArgs()
    private val PICK_FILE_REQUEST_CODE = 1
    private var selectedFileUri: Uri? = null
    private var selectedFileName: String? = null
    private var selectedFileMimeType: String? = null
    private val sessionManager: SessionManager by lazy { SessionManager(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        jobInformationViewModel = ViewModelProvider(this)[JobInformationViewModel::class.java]
        jobapply = ViewModelProvider(this)[ApplyJobViewModel::class.java]
        inforUser = ViewModelProvider(this)[InformationUserViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJobDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        jobInformationViewModel.getInformationById(args.jobInformationId)
        jobInformationViewModel.jobInformationById.observe(viewLifecycleOwner) {
            if (it != null && it.jobinformations.isNotEmpty()) {
                binding.jobTitle.text = it.jobinformations[0].job_name
                binding.jobDescriptionContent.text = it.jobinformations[0].job_description
                binding.location.text = it.jobinformations[0].job_location
                binding.timeandsalary.text =
                    "${it.jobinformations[0].job_minsalary}$ - ${it.jobinformations[0].job_maxsalary}$ \n${it.jobinformations[0].job_endtime}"
            } else {
                Toast.makeText(context, "No job information available", Toast.LENGTH_SHORT).show()
            }
        }

        binding.iconBack.setOnClickListener {
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(R.id.homePageFragment)
        }

        binding.applyNowButton.setOnClickListener {
            openFilePicker()
        }

        binding.seeMoreText.setOnClickListener {
            val navController = NavHostFragment.findNavController(this)
            navController.navigate(R.id.listAllJobFragment)
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                handleFileSelection(uri)
            }
        }
    }

    private fun handleFileSelection(uri: Uri) {
        requireContext().contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                selectedFileName = cursor.getString(nameIndex)
                selectedFileUri = uri
                uploadFile(uri)
            }
        }
    }

    private fun prepareFilePart(fileUri: Uri): MultipartBody.Part {
        val inputStream = requireContext().contentResolver.openInputStream(fileUri)
        val tempFile = File.createTempFile("temp", ".pdf", requireContext().cacheDir)
        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }
        val requestFile = tempFile.asRequestBody("application/pdf".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("CV", tempFile.name, requestFile)
    }

    private fun prepareDescription(descriptionString: String): RequestBody {
        return descriptionString.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    private fun uploadFile(uri: Uri) {

        val cvPart = prepareFilePart(uri)
        var jobseekerId = 0
        inforUser.getUser("truongnqt11@gmail.com")
        inforUser.user.observe(viewLifecycleOwner) {
            jobseekerId = it.jobseeker[0].jobseeker_id
        }
        val coverLetterPart = prepareDescription("Cover letter")

        applyForJob(
            args.jobInformationId.toInt(),
            jobseekerId,
            cvPart,
            coverLetterPart
        ) { success, message ->
            if (success) {
                Toast.makeText(context, "Applied for job successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to apply for job: $message", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun applyForJob(
        jobId: Int,
        jobseekerId: Int,
        cvPart: MultipartBody.Part,
        coverLetterPart: RequestBody,
        callback: (Boolean, String?) -> Unit
    ) {
        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("job_id", jobId.toString())
            .addFormDataPart("jobseeker_id", jobseekerId.toString())
            .addPart(cvPart)
            .addFormDataPart("cover_letter", "cover_letter.txt", coverLetterPart)
            .addFormDataPart("apply_date", SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()))
            .addFormDataPart("status", "applied")
            .build()

        val request = Request.Builder()
            .url("http://139.59.242.16/applyJob")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                callback(false, e.message)
            }

            override fun onResponse(call: Call, response: Response) {

            }
        })
    }
}
