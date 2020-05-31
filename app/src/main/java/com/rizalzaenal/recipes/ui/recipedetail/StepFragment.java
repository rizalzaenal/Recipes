package com.rizalzaenal.recipes.ui.recipedetail;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.rizalzaenal.recipes.R;
import com.rizalzaenal.recipes.data.model.Step;
import com.rizalzaenal.recipes.data.network.RetrofitClient;
import com.rizalzaenal.recipes.databinding.FragmentStepBinding;
import java.io.FileNotFoundException;

public class StepFragment extends Fragment {
    RecipeDetailViewModel viewModel;
    FragmentStepBinding binding;
    SimpleExoPlayer exoPlayer;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;

    private static final String EXTRA_STEP = "EXTRA_STEP";

    public StepFragment() {
        // Required empty public constructor
    }

    public static StepFragment newInstance(Step step) {
        StepFragment fragment = new StepFragment();
        return fragment;
    }

    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentStepBinding.bind(view);
        setHasOptionsMenu(false);

        viewModel = new ViewModelProvider(getActivity(), new ViewModelProvider.Factory() {
            @NonNull @Override public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                RecipeDetailViewModel viewModel =
                  new RecipeDetailViewModel(RetrofitClient.getInstance().getRecipeClient());
                return (T) viewModel;
            }
        }).get(RecipeDetailViewModel.class);


        viewModel.currentStep.observe(getViewLifecycleOwner(), step -> {
            play(step);
            binding.stepDescription.setText(step.getDescription());
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(step.getShortDescription());
        });

    }

    @Override public void onStart() {
        super.onStart();
    }

    @Override public void onResume() {
        super.onResume();
        play(viewModel.currentStep.getValue());
    }

    @Override public void onPause() {
        super.onPause();
        //releasePlayer();
    }

    @Override public void onStop() {
        super.onStop();
        releasePlayer();
    }


    private void play(Step step) {
        String url = "";
        if (step != null && step.getVideoURL() != null) {
            url = step.getVideoURL();
        } else if (step != null && step.getThumbnailURL() != null) {
            url = step.getThumbnailURL();
        }

        final String finalUrl = url;

        if (exoPlayer == null) {
            exoPlayer = new SimpleExoPlayer.Builder(getContext()).build();
            binding.videoView.setPlayer(exoPlayer);
            exoPlayer.addListener(new Player.EventListener() {

                @Override public void onPlayerError(ExoPlaybackException error) {
                    if (finalUrl.isEmpty()) {
                        Toast.makeText(getContext(), getString(R.string.video_not_found),
                          Toast.LENGTH_LONG)
                          .show();
                    } else {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG)
                          .show();
                    }
                }
            });
        }

        exoPlayer.seekTo(currentWindow, playbackPosition);
        exoPlayer.setPlayWhenReady(playWhenReady);
        exoPlayer.prepare(createMediaSource(url), false, false);
    }

    private MediaSource createMediaSource(String url) {
        String userAgent = "recipe-exoplayer";

        MediaSource source = new ProgressiveMediaSource
          .Factory(new DefaultDataSourceFactory(getContext(), userAgent),
          new DefaultExtractorsFactory())
          .createMediaSource(Uri.parse(url));

        return source;
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            playWhenReady = exoPlayer.getPlayWhenReady();
            currentWindow = exoPlayer.getCurrentWindowIndex();
            playbackPosition = exoPlayer.getCurrentPosition();
            exoPlayer.release();
            exoPlayer = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step, container, false);
    }
}