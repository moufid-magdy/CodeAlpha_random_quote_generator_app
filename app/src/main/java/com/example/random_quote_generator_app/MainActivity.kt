package com.example.random_quote_generator_app

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ShareCompat
import com.example.random_quote_generator_app.databinding.ActivityMainBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getQuote()

        binding.nextBtn.setOnClickListener{
            getQuote()
        }

        binding.shareBtn.setOnClickListener {
            shareQuote()
        }

    }

    private fun getQuote(){
        setInProgress(true)
        GlobalScope.launch {
            try {
                val response = RetrofitInstance.quoteApi.getRandomQuote()
                runOnUiThread{
                    setInProgress(false)
                    response.body()?.first()?.let{
                        setUI(it)
                    }
                }
            }catch (e : Exception){
                runOnUiThread{
                    setInProgress(false)
                    Toast.makeText(applicationContext, "Please, check internet connection", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun shareQuote(){
        val quote = binding.quoteTv.text.toString()
        val author = binding.authorTv.text.toString()

        val shareMessage = "$quote\n\n- $author"

        val intent = ShareCompat.IntentBuilder.from(this)
            .setType("text/plain")
            .setText(shareMessage)
            .setChooserTitle("Share Quote")
            .createChooserIntent()

        startActivity(intent)
    }

    private fun setUI(quote: QuoteModel){
        binding.quoteTv.text = quote.q
        binding.authorTv.text = quote.a
    }

    private fun setInProgress(inProgress : Boolean){
        if (inProgress){
            binding.progressBar.visibility = View.VISIBLE
            binding.nextBtn.visibility = View.GONE
        }else{
            binding.progressBar.visibility = View.GONE
            binding.nextBtn.visibility = View.VISIBLE
        }
    }
}