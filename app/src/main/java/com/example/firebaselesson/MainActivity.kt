package com.example.firebaselesson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.firebaselesson.Constants.NEWS
import com.example.firebaselesson.databinding.ActivityMainBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        FirebaseFirestore.getInstance().collection(NEWS).get().addOnCompleteListener {
//            if(it.isSuccessful){
//                it.result.documents.forEach {
//                    println(it)
//                }
//            }
//        }

        listeners()
    }

    private fun listeners() {
        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val content = binding.etContent.text.toString().trim()

            val reference = FirebaseFirestore.getInstance().collection(NEWS).document()
            val news = News(id = reference.id, title = title, content = content)

            FirebaseFirestore.getInstance().collection(NEWS).document(reference.id).set(news)
//            FirebaseFirestore.getInstance().collection(NEWS).add(news).addOnSuccessListener {
//                it.update(mapOf("id" to it.id))
//            }
        }

        FirebaseFirestore.getInstance().collection(NEWS).addSnapshotListener { value, error ->
            if(error == null) {
                value?.documentChanges?.forEach {
                    println("type: ${it.type} ${it.document} ")
                    val news = it.document.toObject(News::class.java)
                    when(it.type){
                        DocumentChange.Type.ADDED ->{
                            val tvNews = TextView(this)
                            tvNews.text = "${news.title} ${news.content}"
                            binding.llNews.addView(tvNews)
                        }
                        DocumentChange.Type.REMOVED ->{
                            binding.llNews.removeViewAt(it.oldIndex)
                        }
                        DocumentChange.Type.MODIFIED ->{}
                    }
                }
            }
        }
    }
}