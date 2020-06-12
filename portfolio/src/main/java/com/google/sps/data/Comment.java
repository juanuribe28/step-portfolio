package com.google.sps.data;

import com.google.sps.data.Label;
import java.util.*;

/** Class containing user comments */
public final class Comment {

  private String title;
  private String author;
  private long timestamp;
  private long rating;
  private String comment;
  private String blobKeyString;
  private double sentimentScore;
  private List<Label> imageLabels;
  private final String userId;
  private final long id;

  private Comment(CommentBuilder builder) {
    this.title = builder.title;
    this.author = builder.author;
    this.timestamp = builder.timestamp;
    this.rating = builder.rating;
    this.comment = builder.comment;
    this.blobKeyString = builder.blobKeyString;
    this.sentimentScore = builder.sentimentScore;
    this.imageLabels = builder.imageLabels;
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

  public double getSentimentScore() {
    return sentimentScore;
  }

  public String getComment() {
    return comment;
  }

  public String getBlobKeyString() {
    return blobKeyString;
  }

  public List<Label> getImageLabels() {
    return imageLabels;
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
    private String blobKeyString;
    private double sentimentScore;
    private List<Label> imageLabels;
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

    public CommentBuilder blobKeyString(String blobKeyString) {
      this.blobKeyString = blobKeyString;
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
    
    public CommentBuilder sentimentScore(double sentimentScore) {
      this.sentimentScore = sentimentScore;
      return this;
    }

    public CommentBuilder imageLabels(List<Label> imageLabels) {
      this.imageLabels = imageLabels;
      return this;
    }

    public Comment build() {
      Comment commentObject = new Comment(this);
      return commentObject;
    }
  }
}
