package com.loycompany.hopesoninter.adapters.pager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;

import com.loycompany.hopesoninter.R;
import com.loycompany.hopesoninter.classes.Post;
import com.loycompany.hopesoninter.classes.PostMedia;
import com.loycompany.hopesoninter.helpers.DataParser;
import com.loycompany.hopesoninter.network.URLs;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class PostMediaViewPagerAdapter extends PagerAdapter {
    Context context;
    List<PostMedia> postMedia;

    LayoutInflater mLayoutInflater;

    public PostMediaViewPagerAdapter(Context context, List<PostMedia> postMedia) {
        this.context = context;
        this.postMedia = postMedia;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return postMedia.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((CardView) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View itemView = mLayoutInflater.inflate(R.layout.card_post_media, container, false);

        // Adding the View
        Objects.requireNonNull(container).addView(itemView);

        ImageView imageView = itemView.findViewById(R.id.image_view);

        if (postMedia.size() > 0){
            String path = postMedia.get(position).url;
            if (path != null){
                List<String> choppedPath = DataParser.javaexplode("/", path);
                choppedPath.set(0, "storage");
                String correctedPath = DataParser.javaimpload(choppedPath, "/");

                Picasso.get()
                        .load(URLs.getStorageAddress() + correctedPath)
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.ic_waiting)
                        .error(R.drawable.ic_error)
                        .into(imageView);
            }
        } else {
            /*Picasso.get()
                    .load("www.nothing.cc")
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_waiting)
                    .error(R.drawable.ic_error)
                    .into(imageView);*/
        }


        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((CardView) object);
    }
}
