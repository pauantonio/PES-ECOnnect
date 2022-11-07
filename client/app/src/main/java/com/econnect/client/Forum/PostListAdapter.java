package com.econnect.client.Forum;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.econnect.API.ForumService.Post;
import com.econnect.API.Translate.TranslateService;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.HashtagPatternMatcher;
import com.econnect.Utilities.PopupMessage;
import com.econnect.Utilities.Translate;
import com.econnect.Utilities.URLPatternMatcher;
import com.econnect.client.Profile.Medals.MedalUtils;
import com.econnect.client.R;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class PostListAdapter extends BaseAdapter {
    private final Fragment _owner;
    private final int _highlightColor;
    private final IPostCallback _callback;

    private static LayoutInflater _inflater = null;
    private final ArrayList<Post> _data;

    public PostListAdapter(Fragment owner, IPostCallback callback, int highlightColor, Post[] posts) {
        this._owner = owner;
        this._highlightColor = highlightColor;
        this._data = new ArrayList<>(Arrays.asList(posts));
        this._callback = callback;
        _inflater = (LayoutInflater) owner.requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _data.size();
    }

    @Override
    public Object getItem(int position) {
        return _data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Initialize view and product
        final Post p = _data.get(position);
        final View vi;
        if (convertView != null) vi = convertView;
        else vi = _inflater.inflate(R.layout.post_list_item, null);

        // Set time text
        TextView timeText = vi.findViewById(R.id.postTimeText);
        timeText.setText(timestampToString((long) p.timestamp));

        // Set author name
        TextView authorName = vi.findViewById(R.id.postUsernameText);
        if (p.authorbanned) {
            authorName.setText(p.username + " 💀️");
            authorName.setTypeface(authorName.getTypeface(), Typeface.BOLD_ITALIC);
            authorName.setOnClickListener(null);
            authorName.setTextColor(timeText.getCurrentTextColor()); // Use the color of the time text as default
        }
        else {
            authorName.setText(p.username);
            authorName.setTypeface(authorName.getTypeface(), Typeface.BOLD);
            authorName.setOnClickListener(view -> _callback.usernameClicked(p.userid));
            authorName.setTextColor(_highlightColor);
        }


        // Set medal
        ImageView medalImage = vi.findViewById(R.id.postMedalImage);
        if (p.medal == 0) {
            medalImage.setVisibility(View.GONE);
        }
        else {
            Drawable medalDrawable = MedalUtils.medalIcon(p.medal);
            medalImage.setVisibility(View.VISIBLE);
            medalImage.setImageDrawable(medalDrawable);
        }

        // Set post body
        setBody(vi, p.text);
        // Set listener for translating
        TextView translateButton = vi.findViewById(R.id.translate_button);
        translateButton.setVisibility(View.VISIBLE);
        translateButton.setOnClickListener(view -> translateTextAsync(vi, p.text));

        // Set likes and dislikes
        TextView likes = vi.findViewById(R.id.likesAmountText);
        likes.setText(String.format(Locale.getDefault(), "%d", p.likes));
        TextView dislikes = vi.findViewById(R.id.dislikesAmountText);
        dislikes.setText(String.format(Locale.getDefault(), "%d", p.dislikes));

        // Store id in view
        String postId = Integer.toString(p.postid);
        TextView hidden_id = vi.findViewById(R.id.hidden_PostId);
        hidden_id.setText(postId);

        // Set item image
        ImageView image = vi.findViewById(R.id.postImage);
        image.setVisibility(View.GONE);
        ExecutionThread.nonUI(()->{
            // Poll image.getWidth() until the layout has been inflated
            int width = -1;
            while (width == -1) {
                width = image.getWidth();
            }
            Bitmap bmp = p.getImage(width);
            if (bmp == null) return;
            ExecutionThread.UI(_owner, ()-> {
                // If the view has changed while we were fetching the image, do nothing
                if (hidden_id.getText().equals(postId)) {
                    image.setImageBitmap(bmp);
                    image.setVisibility(View.VISIBLE);
                }
            });
        });


        // Set listeners
        ImageButton shareButton = vi.findViewById(R.id.sharePostButton);
        shareButton.setOnClickListener(view -> _callback.share(p));

        ImageButton likeButton = vi.findViewById(R.id.likePostButton);
        likeButton.setOnClickListener(view -> voteButtonListener(vi, true, p));

        ImageButton dislikeButton = vi.findViewById(R.id.dislikePostButton);
        dislikeButton.setOnClickListener(view -> voteButtonListener(vi, false, p));

        // Highlight previous user choice
        if (p.useroption == Post.OPT_LIKE) {
            likeButton.setColorFilter(_highlightColor);
            dislikeButton.clearColorFilter();
        }
        else if (p.useroption == Post.OPT_DISLIKE) {
            dislikeButton.setColorFilter(_highlightColor);
            likeButton.clearColorFilter();
        }
        else if (p.useroption == Post.OPT_NONE) {
            likeButton.clearColorFilter();
            dislikeButton.clearColorFilter();
        }
        else {
            throw new RuntimeException("Invalid user option: " + p.useroption);
        }

        // Display delete and report buttons
        ImageButton deleteButton = vi.findViewById(R.id.deletePostButton);
        ImageButton reportButton = vi.findViewById(R.id.reportPostButton);
        if (p.ownpost) {
            deleteButton.setVisibility(View.VISIBLE);
            reportButton.setVisibility(View.GONE);
            deleteButton.setOnClickListener(view -> _callback.delete(p, position));
        }
        else {
            reportButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);
            reportButton.setOnClickListener(view -> _callback.report(p, position));
        }

        return vi;
    }

    private void setBody(View vi, String body) {
        TextView textBody = vi.findViewById(R.id.postContentText);
        // Required for clickable tags to work
        textBody.setMovementMethod(LinkMovementMethod.getInstance());
        Spannable spannable = new SpannableString(body);
        // Iterate for all instances of '#'
        new HashtagPatternMatcher().find(body, (start, end) -> {
            // Set color and make bold, also make clickable
            String tag = body.substring(start+1, end);
            spannable.setSpan(new TagClickableSpan(tag), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        });
        // Iterate for all URLs
        new URLPatternMatcher().find(body, (start, end) -> {
            // Set color and make bold, also make clickable
            String tag = body.substring(start, end);
            spannable.setSpan(new LinkClickableSpan(tag), start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        });

        textBody.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    private String timestampToString(long timestamp) {
        // Date takes milliseconds as input
        Date date = new Date(timestamp * 1000L);

        // Difference between dates, in seconds
        long diffSec = (System.currentTimeMillis() - date.getTime()) / 1000L;
        if (diffSec < 60) diffSec = 60;
        // Convert to hours + minutes
        int hours = (int) (diffSec / (60 * 60));
        int mins = (int) (((diffSec - hours * 3600) / 60) % 60);

        if (hours == 0) {
            int min_string_id = (mins == 1) ? R.string.minutes_ago_one : R.string.minutes_ago;
            return _owner.getString(min_string_id, mins);
        }
        else if (hours < 24) {
            int hour_string_id = (hours == 1) ? R.string.hours_ago_one : R.string.hours_ago;
            return _owner.getString(hour_string_id, hours);
        }
        else {
            DateFormat dateFormat = DateFormat.getDateInstance();
            return dateFormat.format(date);
        }
    }

    public void deleteItem(int position) {
        _data.remove(position);
        super.notifyDataSetChanged();
    }

    private class TagClickableSpan extends ClickableSpan {
        String _tag;
        public TagClickableSpan(String tag) { _tag = tag; }
        @Override
        public void onClick(@NonNull View view) {
            _callback.tagClicked(_tag);
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            // No underline, set color and make bold
            ds.setUnderlineText(false);
            ds.setColor(_highlightColor);
            ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        }
    }
    private class LinkClickableSpan extends ClickableSpan {
        String _url;
        public LinkClickableSpan(String url) { _url = url; }
        @Override
        public void onClick(@NonNull View view) {
            _callback.linkClicked(_url);
        }
        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            // Underline, set color and make bold
            ds.setUnderlineText(true);
            ds.setColor(_highlightColor);
            ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        }
    }

    private void voteButtonListener(View vi, boolean like, Post p) {
        final ImageButton button;
        final TextView text;
        final boolean removeThis, removeTwin;
        if (like) {
            button = vi.findViewById(R.id.likePostButton);
            text = vi.findViewById(R.id.likesAmountText);
            removeThis = (p.useroption == Post.OPT_LIKE);
            removeTwin = (p.useroption == Post.OPT_DISLIKE);
        }
        else {
            button = vi.findViewById(R.id.dislikePostButton);
            text = vi.findViewById(R.id.dislikesAmountText);
            removeThis = (p.useroption == Post.OPT_DISLIKE);
            removeTwin = (p.useroption == Post.OPT_LIKE);
        }

        // Decrement twin counter, emulate click on other counter
        if (removeTwin) {
            voteButtonListener(vi, !like, p);
        }

        // Update button color
        if (removeThis) button.clearColorFilter();
        else button.setColorFilter(_highlightColor);

        // Update text
        final int increment = removeThis ? -1 : 1;
        final int newCount;
        if (like) newCount = (p.likes += increment);
        else newCount = (p.dislikes += increment);
        text.setText(String.format(Locale.getDefault(), "%d", newCount));

        // Update user option
        if (removeThis) p.useroption = Post.OPT_NONE;
        else p.useroption = like ? Post.OPT_LIKE : Post.OPT_DISLIKE;

        // Call API
        _callback.vote(p, like, removeThis);
    }

    private void translateTextAsync(View vi, String text) {
        // Attempt to translate post body
        ExecutionThread.nonUI(()->{
            try {
                // Get translation. If it's the same, ignore
                TranslateService.Translation t = new TranslateService().translateToCurrentLang(_owner.requireContext(), text);
                if (!t.translatedText.equals(text)) {
                    // Set the translated text
                    String translation = t.translatedText + "\n\n" + Translate.id(R.string.original_text) + "\n" + text;
                    ExecutionThread.UI(_owner, ()-> setBody(vi, translation));
                }
                // Hide the translate button
                TextView translateButton = vi.findViewById(R.id.translate_button);
                ExecutionThread.UI(_owner, ()-> translateButton.setVisibility(View.GONE));
            }
            catch (Exception e) {
                ExecutionThread.UI(_owner, ()-> PopupMessage.showToast(_owner, Translate.id(R.string.could_not_translate)));
            }
        });
    }
}
