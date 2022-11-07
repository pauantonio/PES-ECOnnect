package com.econnect.client.Forum;

import android.os.Bundle;
import android.widget.AbsListView;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.ContextCompat;

import com.econnect.API.ForumService;
import com.econnect.Utilities.CustomFragment;
import com.econnect.client.R;
import com.econnect.client.databinding.FragmentForumBinding;

public class ForumFragment extends CustomFragment<FragmentForumBinding> {

    private final ForumController _ctrl = new ForumController(this);
    private boolean _isScrollingDown = false;
    private int _lastShownItem = 0;

    public ForumFragment() {
        super(FragmentForumBinding.class);
    }

    @Override
    protected void addListeners() {
        binding.tagDropdown.setOnItemClickListener(_ctrl.tagsDropdown());
        binding.tagDropdown.addTextChangedListener(_ctrl.tagFilterText());
        binding.pullToRefreshPosts.setOnRefreshListener(_ctrl::updateData);
        binding.addPostButton.setOnClickListener(_ctrl.addPostOnClick());

        binding.postList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {}

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > _lastShownItem) {
                    _isScrollingDown = true;
                } else if (firstVisibleItem < _lastShownItem) {
                    _isScrollingDown = false;
                }
                _lastShownItem = firstVisibleItem;

                if (_isScrollingDown) binding.addPostButton.hide();
                else binding.addPostButton.show();
            }
        });

        // Override back button
        requireActivity().getOnBackPressedDispatcher().addCallback(_ctrl.backPressedHandler);

        _ctrl.updateData();
    }

    void setTagsDropdownElements(ForumService.Tag[] allTags) {
        TagListAdapter adapter = new TagListAdapter(requireContext(), allTags);
        binding.tagDropdown.setAdapter(adapter);
    }

    String getTagsDropdownText() {
        return binding.tagDropdown.getText().toString();
    }
    void setTagsDropdownText(String text) {
        binding.tagDropdown.setText(text);
    }

    void setPostElements(ForumService.Post[] posts) {
        int highlightColor = ContextCompat.getColor(requireContext(), R.color.green);
        PostListAdapter _posts_adapter = new PostListAdapter(this, _ctrl.postCallback, highlightColor, posts);
        binding.postList.setAdapter(_posts_adapter);
        binding.postList.refreshDrawableState();
    }

    void enableInput(boolean enabled) {
        binding.pullToRefreshPosts.setRefreshing(!enabled);
        binding.postList.setEnabled(enabled);
        binding.tagBox.setEnabled(enabled);
    }

    void deletePost(int position) {
        PostListAdapter adapter = (PostListAdapter) binding.postList.getAdapter();
        adapter.deleteItem(position);
    }

}
