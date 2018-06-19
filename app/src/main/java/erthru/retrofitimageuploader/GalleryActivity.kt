package erthru.retrofitimageuploader

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import erthru.retrofitimageuploader.adapter.RVAdapterGallery
import erthru.retrofitimageuploader.network.ApiConfig
import erthru.retrofitimageuploader.network.responsemodel.Default
import erthru.retrofitimageuploader.network.responsemodel.Gallery
import kotlinx.android.synthetic.main.activity_gallery.*
import retrofit2.Call
import retrofit2.Response

class GalleryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        supportActionBar?.title = "Gallery"

        rvGallery.setHasFixedSize(true)
        rvGallery.layoutManager = GridLayoutManager(this,2)

        fabMain.setOnClickListener {

            startActivity(Intent(this,UploadActivity::class.java))

        }

    }

    override fun onResume() {
        super.onResume()
        loadGallery()
    }

    private fun loadGallery(){

        val call = ApiConfig().instance().gallery()
        call.enqueue(object : retrofit2.Callback<Gallery>{

            override fun onFailure(call: Call<Gallery>?, t: Throwable?) {
                pbGallery.visibility = View.GONE
                Toast.makeText(applicationContext,"Connection error",Toast.LENGTH_SHORT).show()
                Log.d("ONFAILURE",t.toString())
            }

            override fun onResponse(call: Call<Gallery>?, response: Response<Gallery>?) {

                pbGallery.visibility = View.GONE
                val adapter = RVAdapterGallery(this@GalleryActivity, response?.body()?.result!!)
                adapter.notifyDataSetChanged()
                rvGallery.adapter = adapter

            }

        })

    }

    fun delete(imageid:String?){

        val loading = ProgressDialog(this)
        loading.setMessage("Deleting image...")
        loading.show()

        val call = ApiConfig().instance().delete(imageid)
        call.enqueue(object : retrofit2.Callback<Default>{

            override fun onFailure(call: Call<Default>?, t: Throwable?) {
                loading.dismiss()
                Toast.makeText(applicationContext,"Connection error",Toast.LENGTH_SHORT).show()
                Log.d("ONFAILURE",t.toString())
            }

            override fun onResponse(call: Call<Default>?, response: Response<Default>?) {

                loading.dismiss()
                Toast.makeText(applicationContext,response?.body()?.message,Toast.LENGTH_SHORT).show()

                if(response?.body()?.message?.contains("success",false)!!){
                    onResume()
                }

            }


        })

    }

}
