package io.sphere.sdk.reviews.commands.updateactions;

import io.sphere.sdk.commands.UpdateActionImpl;
import io.sphere.sdk.reviews.Review;

import javax.annotation.Nullable;

public class SetAuthorName extends UpdateActionImpl<Review> {
    @Nullable
    private final String authorName;

    private SetAuthorName(@Nullable final String authorName) {
        super("setAuthorName");
        this.authorName = authorName;
    }

    public static SetAuthorName of(@Nullable final String authorName) {
        return new SetAuthorName(authorName);
    }

    @Nullable
    public String getAuthorName() {
        return authorName;
    }
}
