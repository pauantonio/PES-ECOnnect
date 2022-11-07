package com.econnect.client.Forum;

import com.econnect.API.ForumService;

public interface IPostCallback {
    void tagClicked(String tag);
    void linkClicked(String url);
    void share(ForumService.Post post);
    void delete(ForumService.Post post, int position);
    void report(ForumService.Post post, int position);
    void vote(ForumService.Post post, boolean isLike, boolean remove);
    void usernameClicked(int userId);
}
