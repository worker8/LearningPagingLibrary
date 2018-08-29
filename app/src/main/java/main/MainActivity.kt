package beepbeep.learningpaginglibrary

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainButton1.setOnClickListener {
            startActivity(Intent(this, AnimalActivity::class.java))
        }

        mainButton2.setOnClickListener {
            startActivity(Intent(this, SushiActivity::class.java))
        }
    }
}
