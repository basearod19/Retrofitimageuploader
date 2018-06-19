package erthru.retrofitimageuploader

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.vincent.filepicker.Constant.*
import com.vincent.filepicker.activity.ImagePickActivity
import com.vincent.filepicker.filter.entity.ImageFile
import erthru.retrofitimageuploader.network.ApiConfig
import erthru.retrofitimageuploader.network.responsemodel.Default
import kotlinx.android.synthetic.main.activity_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import retrofit2.Call
import retrofit2.Response
import java.io.File


class UploadActivity : AppCompatActivity() {

    lateinit var imagename:MultipartBody.Part

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        supportActionBar?.title = "Upload"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnPickUpload.setOnClickListener {

            if(EasyPermissions.hasPermissions(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                val i = Intent(this,ImagePickActivity::class.java)
                i.putExtra(MAX_NUMBER,1)
                startActivityForResult(i, REQUEST_CODE_PICK_IMAGE)
            }else{
                EasyPermissions.requestPermissions(this,"This application need your permission to access photo gallery.",991,android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }

        }

        btnUpload.setOnClickListener {

            upload()

        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId){

            android.R.id.home->{
                this.finish()
            }

        }

        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null){

            btnPickUpload.visibility = View.GONE
            btnUpload.visibility = View.VISIBLE
            imgUpload.visibility = View.VISIBLE

            val pickedImg = data.getParcelableArrayListExtra<ImageFile>(RESULT_PICK_IMAGE)[0]?.path
            val requestBody = RequestBody.create(MediaType.parse("multipart"), File(pickedImg))
            imagename = MultipartBody.Part.createFormData("imagename",File(pickedImg)?.name,requestBody)

            Glide.with(this).load(pickedImg).into(imgUpload)

        }



    }

    private fun upload(){

        val loading = ProgressDialog(this)
        loading.setMessage("Please wait...")
        loading.show()

        val call = ApiConfig().instance().upload(imagename)
        call.enqueue(object : retrofit2.Callback<Default>{

            override fun onFailure(call: Call<Default>?, t: Throwable?) {
                loading.dismiss()
                Toast.makeText(applicationContext,"Connection error",Toast.LENGTH_SHORT).show()
                Log.d("ONFAILURE",t.toString())
            }

            override fun onResponse(call: Call<Default>?, response: Response<Default>?) {

                loading.dismiss()
                Toast.makeText(applicationContext,response?.body()?.message,Toast.LENGTH_SHORT).show()

                if(response?.body()?.message?.contains("Success",true)!!){
                    this@UploadActivity.finish()
                }

            }


        })

    }



}
