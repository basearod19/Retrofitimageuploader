package erthru.retrofitimageuploader.network

import erthru.retrofitimageuploader.network.responsemodel.Default
import erthru.retrofitimageuploader.network.responsemodel.Gallery
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

class ApiConfig{

    companion object {

        const val BASE_URL = "http://192.168.43.39/anows/retrofitimageuploader/"
        const val IMAGE_URL = BASE_URL+"image/"

    }

    private fun retrofit() : Retrofit{
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    fun instance() : ApiInterface {

        return retrofit().create(ApiInterface::class.java)

    }


}

interface ApiInterface{


    @Multipart
    @POST("upload.php")
    fun upload(

            @Part imagename:MultipartBody.Part

    ) : Call<Default>

    @GET("gallery.php")
    fun gallery() : Call<Gallery>

    @GET("delete.php")
    fun delete(

            @Query("imageid") imageid:String?

    ) : Call<Default>

}