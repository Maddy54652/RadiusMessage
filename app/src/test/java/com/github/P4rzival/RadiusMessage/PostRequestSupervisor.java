package com.github.P4rzival.RadiusMessage;

import org.json.*;

import java.util.ArrayDeque;

public class PostRequestSupervisor {

    private boolean shouldContinue = false;

    JSONObject post;

    static ArrayDeque<PostRequestSupervisor> waitingPostRequestSupervisors
            = new ArrayDeque<PostRequestSupervisor>();

    public PostRequestSupervisor(JSONObject newPost) {
        prsContinueFalse();
        this.post = newPost;
        waitingPostRequestSupervisors.add(this);
        spinlock();
    }

    private void spinlock() {
        DatabaseAccessor.databaseRequestApproval(this);
        while(!prsContinue()); // Spinlock

        //(new PostDrawer()).createPost(this.post);

        // Remove ourselves from the queue now that we're done
        waitingPostRequestSupervisors.remove(this);
    }

    boolean prsContinue() {
        return this.shouldContinue;
    }

    boolean prsContinueTrue() {
        return this.shouldContinue = true;
    }

    boolean prsContinueFalse() {
        return this.shouldContinue = false;
    }

    boolean prsContinueToggle() {
        return this.shouldContinue = !this.shouldContinue;
    }
}