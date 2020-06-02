package com.google.sps.data;

import java.util.*;

/** Class containing user comments */
public final class Comment {

  private final String title;
  private final String author;
  private final Date date;
  private final String comment;

  public Comment(String title, String author, Date date, String comment) {
    this.title = title;
    this.author = author;
    this.date = date;
    this.comment = comment;
  }

  public String getTitle() {
    return title;
  }

  public String getAuthor() {
    return author;
  }

  public Date getDate() {
    return date;
  }

  public String getComment() {
    return comment;
  }
}
