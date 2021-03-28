package com.udacity.detail

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udacity.MainActivity
import com.udacity.R
import com.udacity.databinding.FragmentDetailBinding
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: FragmentDetailBinding

    // var status = " "
    var filename = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.fragment_detail)
        setSupportActionBar(toolbar)

        //set the title in the ActionBar
        this.title = "Files Downloaded"

        //go back to main activity
        binding.okButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        //return the same url
       val item1 = intent ?: return
       val itemvalue = PendingIntent.getActivity(applicationContext, 0, item1, 0)

      //  filename = "File Name: $items"
        filename = "File Name: $itemvalue"
        Log.i("URL LINK", filename)


        //do this first
        binding.downloadInfo = this
        if (filename != null) {
       // if (item != null) {
          // intentValue = "SUCCESS"       //this prints the status to the results activity
            binding.fileinfoname.text = "The file name: $filename"
            binding.filestatusview.text = getString(R.string.success)
            binding.filestatusview.setTextColor(getColor(R.color.successtext))

        } else {
            // FAILED
            binding.filestatusview.text = getString(R.string.failed)
            binding.filestatusview.setTextColor(getColor(R.color.failuretext))
        }
    }
}










