package com.google.sps.data;

import java.util.*;

/** Class containing user comments */
public final class Comment {

  private String title;
  private String author;
  private long timestamp;
  private long rating;
  private String comment;
  private String imageUrl;
  private final String userId;
  private final long id;

  private Comment(CommentBuilder builder) {
    this.title = builder.title;
    this.author = builder.author;
    this.timestamp = builder.timestamp;
    this.rating = builder.rating;
    this.comment = builder.comment;
    this.imageUrl = builder.imageUrl;
    this.userId = builder.userId;
    this.id = builder.id;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public long getTimestamp() {
    return timestamp;
  }

  public long getRating() {
    return rating;
  }

  public String getComment() {
    return comment;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getUserId() {
    return userId;
  }

  public long getId() {
    return id;
  }

  /** Class used to build comments */
  public static class CommentBuilder {

    private String title;
    private String author;
    private long timestamp;
    private long rating;
    private String comment;
    private String imageUrl;
    private final String userId;
    private final long id;

    public CommentBuilder(long id, String userId) {
      this.id = id;
      this.userId = userId;
    }

    public CommentBuilder title(String title) {
      this.title = title;
      return this;
    }

    public CommentBuilder author(String author) {
      this.author = author;
      return this;
    }

    public CommentBuilder comment(String comment) {
      this.comment = comment;
      return this;
    }

    public CommentBuilder imageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
      return this;
    }

    public CommentBuilder timestamp(long timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    public CommentBuilder rating(long rating) {
      this.rating = rating;
      return this;
    }

    public Comment build() {
      Comment commentObject = new Comment(this);
      return commentObject;
    }
  }
}
