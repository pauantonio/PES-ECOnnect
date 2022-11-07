package com.econnect.client.Forum;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.econnect.API.ForumService;
import com.econnect.API.ServiceFactory;
import com.econnect.Utilities.BitmapLoader;
import com.econnect.Utilities.ExecutionThread;
import com.econnect.Utilities.PopupMessage;
import com.econnect.Utilities.ShareManager;
import com.econnect.client.BuildConfig;
import com.econnect.client.Profile.VisitUserProfileActivity;
import com.econnect.client.R;

public class ForumController {

    private final ForumFragment _fragment;
    private boolean _listContainsAllTags = true;
    private final ActivityResultLauncher<Intent> _activityLauncher;

    public ForumController(ForumFragment fragment) {
        this._fragment = fragment;
        _activityLauncher = fragment.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::launchNewPostCallback
        );
    }

    private void launchNewPostCallback(ActivityResult result) {
        // Called once the user returns from new post screen
        if (result.getResultCode() != Activity.RESULT_CANCELED) {
            updateData();
        }
    }

    public void updateData() {
        _fragment.setTagsDropdownText("");
        _fragment.enableInput(false);
        // Populate tag dropdown
        updateTagList();
        // Populate post list (no tag)
        updatePostsList(null);
    }

    private void updateTagList() {
        ExecutionThread.nonUI(()-> {
            try {
                // Get types
                ForumService service = ServiceFactory.getInstance().getForumService();
                ForumService.Tag[] tags = service.getAllTags();

                ExecutionThread.UI(_fragment, () -> {
                    _fragment.setTagsDropdownElements(tags);
                });
            }
            catch (Exception e) {
                ExecutionThread.UI(_fragment, ()->{
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_fetch_tags) + "\n" + e.getMessage());
                });
            }
        });
    }

    private void updatePostsList(String tag) {
        ExecutionThread.nonUI(()-> {
            // Keep track of whether the list is dirty (skip unnecessary calls to backend)
            _listContainsAllTags = (tag == null);
            try {
                // Get products of all types
                ForumService service = ServiceFactory.getInstance().getForumService();
                ForumService.Post[] posts = service.getPosts(1000, tag);

                ExecutionThread.UI(_fragment, () -> {
                    _fragment.setPostElements(posts);
                    _fragment.enableInput(true);
                    if (!_listContainsAllTags) backPressedHandler.setEnabled(true);
                });
            } catch (Exception e) {
                ExecutionThread.UI(_fragment, () -> {
                    PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_fetch_posts) + "\n" + e.getMessage());
                    _fragment.enableInput(true);
                });
            }
        });
    }



    // Update product list when dropdown or search text change

    AdapterView.OnItemClickListener tagsDropdown() {
        return (parent, view, position, id) -> {
            // Update list
            updatePostsList((String) parent.getItemAtPosition(position));
        };
    }

    TextWatcher tagFilterText() {
        return new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // If the list is dirty and the new text is blank, delete filter
                if (!_listContainsAllTags && s.toString().trim().isEmpty()) {
                    _fragment.enableInput(false);
                    updatePostsList(null);
                }
            }
        };
    }

    final IPostCallback postCallback = new IPostCallback() {
        @Override
        public void tagClicked(String tag) {
            // Called when a tag from the post body is clicked. Update the search bar and the post list
            _fragment.setTagsDropdownText(tag);
            _fragment.enableInput(false);
            updatePostsList(tag);
        }

        @Override
        public void linkClicked(String url) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            _fragment.requireActivity().startActivity(browserIntent);
        }

        @Override
        public void share(ForumService.Post post) {
            _fragment.enableInput(false);
            ExecutionThread.nonUI(()->{
                // [username] on ECOnnect: [text]  Check out ECOnnect at ...
                String text = _fragment.getString(R.string.on_econnect, post.username) + "\n" + post.text + "\n\n" +
                        _fragment.getString(R.string.check_out_econnect) +
                        _fragment.getString(R.string.playstore_base_url) + BuildConfig.APPLICATION_ID;
                Bitmap bmp = BitmapLoader.fromURL(post.imageurl);
                if (bmp != null) ShareManager.shareTextAndImage(text, bmp, _fragment.requireContext());
                else ShareManager.shareText(text, _fragment.requireContext());
                ExecutionThread.UI(_fragment, ()-> _fragment.enableInput(true));
            });
        }

        @Override
        public void delete(ForumService.Post post, int position) {
            // Show confirmation dialog
            PopupMessage.yesNoDialog(_fragment, _fragment.getString(R.string.delete_post), _fragment.getString(R.string.want_to_delete_post), (dialog, id) -> {
                // Execute in non-ui thread
                ExecutionThread.nonUI(()-> {
                    // Delete post
                    ForumService service = ServiceFactory.getInstance().getForumService();
                    try {
                        service.deletePost(post.postid);
                        ExecutionThread.UI(_fragment, ()-> _fragment.deletePost(position));
                    }
                    catch (Exception e) {
                        ExecutionThread.UI(_fragment, ()-> PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_delete_post) + "\n" + e.getMessage()));
                    }
                });
            });
        }

        @Override
        public void report(ForumService.Post post, int position) {
            // Show confirmation dialog
            PopupMessage.yesNoDialog(_fragment, _fragment.getString(R.string.report_post), _fragment.getString(R.string.want_to_report_post), (dialog, id) -> {
                // Execute in non-ui thread
                ExecutionThread.nonUI(()-> {
                    // Delete post
                    ForumService service = ServiceFactory.getInstance().getForumService();
                    try {
                        service.reportPost(post.postid);
                        ExecutionThread.UI(_fragment, ()-> {
                            _fragment.deletePost(position);
                            PopupMessage.showToast(_fragment, _fragment.getString(R.string.post_reported));
                        });
                    }
                    catch (Exception e) {
                        ExecutionThread.UI(_fragment, ()-> PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_report_post) + "\n" + e.getMessage()));
                    }
                });
            });
        }

        @Override
        public void vote(ForumService.Post post, boolean isLike, boolean remove) {
            // Execute in non-ui thread
            ExecutionThread.nonUI(()-> {
                // Vote post
                ForumService service = ServiceFactory.getInstance().getForumService();
                try {
                    service.likePost(post.postid, isLike, remove);
                }
                catch (Exception e) {
                    ExecutionThread.UI(_fragment, ()-> {
                        PopupMessage.warning(_fragment, _fragment.getString(R.string.could_not_cast_vote) + "\n" + e.getMessage());
                    });
                }
            });
        }

        @Override
        public void usernameClicked(int userId) {
            Intent intent = new Intent(_fragment.getContext(), VisitUserProfileActivity.class);
            intent.putExtra("username", userId);
            _activityLauncher.launch(intent);
        }

    };

    View.OnClickListener addPostOnClick() {
        return (view) -> {
            // Launch new activity PostActivity
            Intent intent = new Intent(_fragment.getContext(), PostActivity.class);
            _activityLauncher.launch(intent);
        };
    }

    final OnBackPressedCallback backPressedHandler = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (!_fragment.getTagsDropdownText().isEmpty()) {
                _fragment.setTagsDropdownText("");
            }
            else {
                setEnabled(false);
                _fragment.requireActivity().onBackPressed();
            }
        }
    };

}
