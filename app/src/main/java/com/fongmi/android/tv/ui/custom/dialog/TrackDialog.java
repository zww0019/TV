package com.fongmi.android.tv.ui.custom.dialog;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;

import com.fongmi.android.tv.bean.Track;
import com.fongmi.android.tv.databinding.DialogTrackBinding;
import com.fongmi.android.tv.player.Players;
import com.fongmi.android.tv.ui.adapter.TrackAdapter;
import com.fongmi.android.tv.ui.custom.SpaceItemDecoration;
import com.fongmi.android.tv.ui.custom.TrackNameProvider;
import com.google.android.exoplayer2.Tracks;

import java.util.ArrayList;
import java.util.List;

import tv.danmaku.ijk.media.player.misc.IjkTrackInfo;

public final class TrackDialog extends BaseDialog implements TrackAdapter.OnClickListener {

    private final TrackNameProvider provider;
    private final TrackAdapter adapter;
    private DialogTrackBinding binding;
    private Listener listener;
    private Players player;
    private int type;

    public static TrackDialog create() {
        return new TrackDialog();
    }

    public TrackDialog() {
        this.adapter = new TrackAdapter(this);
        this.provider = new TrackNameProvider();
    }

    public TrackDialog type(int type) {
        this.type = type;
        return this;
    }

    public TrackDialog player(Players player) {
        this.player = player;
        return this;
    }

    public TrackDialog listener(Listener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    protected ViewBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return binding = DialogTrackBinding.inflate(inflater, container, false);
    }

    @Override
    protected void initView() {
        binding.recycler.setHasFixedSize(true);
        binding.recycler.addItemDecoration(new SpaceItemDecoration(1, 16));
        binding.recycler.setAdapter(adapter.addAll(getTrack()));
        binding.recycler.scrollToPosition(adapter.getSelected());
    }

    private List<Track> getTrack() {
        List<Track> items = new ArrayList<>();
        if (player.isExo()) addExoTrack(items);
        if (player.isIjk()) addIjkTrack(items);
        return items;
    }

    private void addExoTrack(List<Track> items) {
        List<Tracks.Group> groups = player.exo().getCurrentTracks().getGroups();
        for (int i = 0; i < groups.size(); i++) {
            Tracks.Group trackGroup = groups.get(i);
            if (trackGroup.getType() != type) continue;
            for (int j = 0; j < trackGroup.length; j++) {
                Track item = new Track(type, provider.getTrackName(trackGroup.getTrackFormat(j)));
                item.setSelected(trackGroup.isTrackSelected(j));
                item.setPlayer(player.getPlayer());
                item.setGroup(i);
                item.setTrack(j);
                items.add(item);
            }
        }
    }

    private void addIjkTrack(List<Track> items) {
        int track = player.ijk().getSelectedTrack(type);
        List<IjkTrackInfo> trackInfos = player.ijk().getTrackInfo();
        for (int i = 0; i < trackInfos.size(); i++) {
            IjkTrackInfo trackInfo = trackInfos.get(i);
            if (trackInfo.getTrackType() != type) continue;
            Track item = new Track(type, provider.getTrackName(trackInfo));
            item.setPlayer(player.getPlayer());
            item.setSelected(track == i);
            item.setTrack(i);
            items.add(item);
        }
    }

    @Override
    public void onItemClick(Track item) {
        if (listener != null) listener.onTrackClick(item);
        player.setTrack(List.of(item));
        dismiss();
    }

    public interface Listener {

        void onTrackClick(Track item);
    }
}