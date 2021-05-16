package dev.chara.thunderscout.ui.about;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.danielstone.materialaboutlibrary.MaterialAboutFragment;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;

import dev.chara.thunderscout.BuildConfig;
import dev.chara.thunderscout.R;
import dev.chara.thunderscout.databinding.FragmentAboutBinding;

public final class AboutFragment extends MaterialAboutFragment {

    private FragmentAboutBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAboutBinding.inflate(inflater, container, false);

        binding.toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        binding.toolbar.setNavigationOnClickListener(v -> NavHostFragment.findNavController(this).popBackStack());

        // Inflate MaterialAboutList and add to view hierarchy
        View contents = super.onCreateView(inflater, binding.contentRoot, savedInstanceState);
        binding.contentRoot.addView(contents);

        return binding.getRoot();
    }

    @Override
    protected MaterialAboutList getMaterialAboutList(Context context) {
        MaterialAboutCard.Builder titleCard = new MaterialAboutCard.Builder();

        titleCard.addItem(new MaterialAboutTitleItem.Builder()
                .text(R.string.app_name)
                .icon(R.mipmap.ic_launcher)
                .build());

        titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Version " + BuildConfig.VERSION_NAME)
                .icon(R.drawable.ic_info_24dp)
                .build());

        titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("View on Google Play")
                .icon(R.drawable.ic_google_play_24dp)
                .setOnClickAction(() -> {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity().getPackageName())));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
                    }
                })
                .build());

        titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Contribute on GitHub")
                .subText("19lmyers/ThunderScout")
                .icon(R.drawable.ic_github_24dp)
                .setOnClickAction(() -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://github.com/19lmyers/ThunderScout"));
                    startActivity(i);
                })
                .build());

        titleCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Open source licenses")
                .icon(R.drawable.ic_code_24dp)
                .setOnClickAction(() -> {
                    startActivity(new Intent(getContext(), OssLicensesMenuActivity.class));
                })
                .build());

        MaterialAboutCard.Builder authorCard = new MaterialAboutCard.Builder();
        authorCard.title("Created by Luke Myers");

        authorCard.addItem(new MaterialAboutActionItem.Builder()
                .text("Chief Delphi")
                .subText("@19lmyers")
                .icon(R.drawable.ic_forum_24dp)
                .setOnClickAction(() -> {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse("https://www.chiefdelphi.com/u/19lmyers"));
                    startActivity(i);
                })
                .build());

        //TODO support future updates

        //TODO credit Team 980

        return new MaterialAboutList.Builder()
                .addCard(titleCard.build())
                .addCard(authorCard.build())
                .build();
    }

    @Override
    protected int getTheme() {
        return R.style.ThunderScout_MaterialAboutFragment;
    }
}
