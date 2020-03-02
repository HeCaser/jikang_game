package com.example.game.service

import android.app.Service
import android.content.Intent
import android.media.SoundPool
import android.os.Binder
import android.os.IBinder
import android.util.Log

class BackGroupService : Service() {
    companion object {
        var afp: SoundPool? = null
    }
    override fun onBind(paramIntent: Intent): IBinder {
        Log.e("Service", "onCreate")
        return a()
    }

    internal inner class a : Binder() {
        protected fun bc(paramInt: Int) {
            if (afp == null) { //        if (Build.VERSION.SDK_INT >= 21) {
//          BackGroupService.afp = new SoundPool.Builder().setMaxStreams(2).build();
//        } else {
//          BackGroupService.afp = new SoundPool(2, 3, 1);
//        }
//        BackGroupService.afq.put(Integer.valueOf(1), Integer.valueOf(BackGroupService.afp.load(BackGroupService.this.getApplicationContext(), BackGroupService.a(BackGroupService.this)[BackGroupService.aft], 1)));
//        BackGroupService.afq.put(Integer.valueOf(2), Integer.valueOf(BackGroupService.afp.load(BackGroupService.this.getApplicationContext(), BackGroupService.b(BackGroupService.this)[BackGroupService.aft], 1)));
//      }
//      else
//      {
//        BackGroupService.afp.play(((Integer)BackGroupService.afq.get(Integer.valueOf(paramInt))).intValue(), 1.0F, 1.0F, 0, 0, 1.0F);
//        BackGroupService.afv = true;
            }
        }

        protected fun jN() { //      BackGroupService.afu = 0;
//      if (BackGroupService.afp != null)
//      {
//        BackGroupService.afp.stop(((Integer)BackGroupService.afq.get(Integer.valueOf(1))).intValue());
//        BackGroupService.afp.stop(((Integer)BackGroupService.afq.get(Integer.valueOf(2))).intValue());
//      }
//      BackGroupService.afv = false;
        }

        protected fun jO() {}
    }


}