package com.udacity.detail

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.udacity.MainActivity
import com.udacity.R
import com.udacity.databinding.FragmentDetailBinding
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: FragmentDetailBinding


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


        val filename: String = intent.getStringExtra("fileName").toString()


        //do this first     dont need this anymore
       // binding.downloadInfo = this


        // intentValue = "SUCCESS"       //this prints the status to the results activity
        binding.fileinfoname.text = filename
        binding.filestatusview.text = getString(R.string.success)
        binding.filestatusview.setTextColor(getColor(R.color.successtext))

    }
}










