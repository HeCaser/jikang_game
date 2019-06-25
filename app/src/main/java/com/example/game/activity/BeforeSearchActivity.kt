package com.example.game.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.game.R

class BeforeSearchActivity : AppCompatActivity() {

    private var mGameType = -1

    companion object {
        fun start(ctx: Context, type: Int) {
            Intent(ctx, BeforeSearchActivity::class.java).apply {
                putExtra("type", type)
                ctx.startActivity(this)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_before_search)
        mGameType = intent.getIntExtra("type", 1)
    }
}
