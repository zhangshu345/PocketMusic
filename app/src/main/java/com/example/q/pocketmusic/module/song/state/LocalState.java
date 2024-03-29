package com.example.q.pocketmusic.module.song.state;

import android.database.SQLException;

import androidx.annotation.NonNull;

import com.dell.fortune.tools.toast.ToastUtil;
import com.example.q.pocketmusic.config.constant.Constant;
import com.example.q.pocketmusic.data.bean.Song;
import com.example.q.pocketmusic.data.bean.local.Img;
import com.example.q.pocketmusic.data.bean.local.LocalSong;
import com.example.q.pocketmusic.data.db.LocalSongDao;
import com.example.q.pocketmusic.module.song.SongActivityPresenter;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.ForeignCollection;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 鹏君 on 2017/5/3.
 */
//本地状态
public class LocalState extends BaseState implements IState {
    private LocalSong localSong;
    private SongActivityPresenter.IView activity;

    public LocalState(Song song, LocalSong localSong, SongActivityPresenter.IView activity) {
        super(song, Constant.LOCAL);

        this.activity = activity;
        this.localSong = localSong;
    }

    @Override
    public void loadPic() {
        ArrayList<String> imgUrls = getLocalImgs();
        activity.setPicResult(imgUrls, getLoadingWay());
    }

    @NonNull
    private ArrayList<String> getLocalImgs() {
        LocalSongDao localSongDao = new LocalSongDao(activity.getCurrentContext());
        ArrayList<String> imgUrls = new ArrayList<>();
        LocalSong localSong = localSongDao.findBySongId(this.localSong.getId());
        if (localSong == null) {
            ToastUtil.showToast("曲谱消失在了异次元。");
            activity.finish();
            return new ArrayList<>();
        }
        ForeignCollection<Img> imgs = localSong.getImgs();
        CloseableIterator<Img> iterator = imgs.closeableIterator();
        try {
            while (iterator.hasNext()) {
                Img img = iterator.next();
                imgUrls.add(img.getUrl());
            }
        } finally {
            try {
                iterator.close();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
        return imgUrls;
    }
}
