package cn.csnbgsh.herbarium

import android.content.Intent
import android.os.Bundle
import com.cylee.androidlib.base.BaseActivity

/**
 * Created by cylee on 16/3/30.
 */
class InitActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
        findViewById(R.id.init_root).postDelayed(Runnable {
            startMainActivity()
        }, 2000)
    }

    fun startMainActivity() {
        var intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
