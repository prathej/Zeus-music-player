package com.android.player

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.Message
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.player.PlayerViewModel.Companion.getPlayerViewModelInstance
import com.android.player.model.ASong
import com.android.player.service.OnPlayerServiceListener
import com.android.player.service.PlayerService


open class BaseSongPlayerActivity : AppCompatActivity(), OnPlayerActionCallback,
    OnPlayerServiceListener {


    private var mService: PlayerService? = null
    private var mBound = false
    val playerViewModel: PlayerViewModel = getPlayerViewModelInstance()
    private var mSong: ASong? = null
    private var mSongList: MutableList<ASong>? = null
    private var msg = 0


    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                ACTION_PLAY_SONG_IN_LIST -> {
                    if (mSongList.isNullOrEmpty()) mSong?.let { mService?.play(it) }
                    else mSong?.let { mService?.play(mSongList!!, it) }
                }
                ACTION_PLAY_LIST -> mSongList?.let { mService?.play(it) }
                ACTION_PLAY_SONG -> mSong?.let { mService?.play(it) }
                ACTION_STOP -> {
                    mService?.stop()
                    playerViewModel.stop()
                }
                ACTION_PAUSE -> mService?.pause()
            }
        }
    }

    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private val mConnection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to PlayerService, cast the IBinder and get PlayerService instance
            val binder = service as PlayerService.LocalBinder
            mService = binder.service
            mBound = true
            mHandler.sendEmptyMessage(msg)
            mService?.addListener(this@BaseSongPlayerActivity)
        }

        override fun onServiceDisconnected(classname: ComponentName) {
            mBound = false
        }
    }


    override fun onStart() {
        super.onStart()
        playerViewModel.setPlayer(this)
    }

    override fun setBufferingData(isBuffering: Boolean) {
        playerViewModel.setBuffering(isBuffering)
    }

    override fun setVisibilityData(isVisibility: Boolean) {
        playerViewModel.setVisibility(isVisibility)
    }

    private fun bindPlayerService() {
        // Bind to PlayerService
        val intent = Intent(this, PlayerService::class.java)
        ContextCompat.startForegroundService(this, intent)
        if (!mBound) bindService(intent, mConnection, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        // Unbind from the service
        if (mBound) {
            unbindService(mConnection)
            mBound = false
        }
        super.onDestroy()
    }

    override fun play(songList: MutableList<ASong>?, song: ASong) {
        msg = ACTION_PLAY_SONG_IN_LIST
        mSong = song
        mSongList = songList
        if (mService == null) bindPlayerService()
        else mHandler.sendEmptyMessage(msg)
    }

    override fun play(songList: MutableList<ASong>) {
        msg = ACTION_PLAY_LIST
        mSongList = songList
        if (mService == null) bindPlayerService()
        else mHandler.sendEmptyMessage(msg)
    }

    override fun play(song: ASong) {
        msg = ACTION_PLAY_SONG
        mSong = song
        if (mService == null) bindPlayerService()
        else mHandler.sendEmptyMessage(msg)
    }

    override fun pause() {
        msg = ACTION_PAUSE
        if (mService == null) bindPlayerService()
        else mHandler.sendEmptyMessage(msg)
    }

    override fun stop() {
        msg = ACTION_STOP
        if (mService == null) bindPlayerService()
        else mHandler.sendEmptyMessage(msg)
    }

    override fun clearAllItemsInQueue() {
        mService?.clearQueue()
    }

    override fun addToQueue(songList: ArrayList<ASong>) {
        mService?.addToQueue(songList)
    }

    override fun shuffle(isShuffle: Boolean) {
        mService?.onShuffle(isShuffle)
    }

    override fun repeatAll(isRepeatAll: Boolean) {
        mService?.onRepeatAll(isRepeatAll)
    }

    override fun onRepeat(isRepeat: Boolean) {
        mService?.onRepeat(isRepeat)
    }

    override fun updateSongData(song: ASong?) {
        playerViewModel.setData(song)
    }

    override fun setPlayStatus(isPlay: Boolean) {
        playerViewModel.setPlayStatus(isPlay)
    }

    override fun updateSongProgress(duration: Long, position: Long) {
        playerViewModel.setChangePosition(position, duration)
    }

    override fun onSongEnded() {
        playerViewModel.onComplete()
    }

    override fun playOnCurrentQueue(song: ASong) {
        mService?.playOnCurrentQueue(song)
    }

    override fun skipToNext() {
        mService?.skipToNext()
    }

    override fun skipToPrevious() {
        mService?.skipToPrevious()
    }

    override fun seekTo(position: Long?) {
        position?.let { nonNullPosition ->
            mService?.seekTo(nonNullPosition)
        }
    }


    companion object {

        private val TAG = BaseSongPlayerActivity::class.java.name
        const val SONG_LIST_KEY = "SONG_LIST_KEY"
        private const val ACTION_PLAY_SONG = 1
        private const val ACTION_PLAY_LIST = 2
        private const val ACTION_PLAY_SONG_IN_LIST = 3
        private const val ACTION_ADD_TO_QUEUE = 4
        private const val ACTION_PAUSE = 5
        private const val ACTION_STOP = 6
        private const val ACTION_SKIP_TO_NEXT = 7
        private const val ACTION_SKIP_TO_PREVIOUS = 8
        private const val ACTION_SEEK_TO = 9
        private const val ACTION_PLAY_ON_CURRENT_QUEUE = 10
    }
}