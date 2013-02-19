package com.pentasoft.db.model;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "Article")
public class Article {

	@Id(column = "ArticleId")
	public int ArticleId;
	public String Title;
	public String Summary;
	public String Content;
	// public String Link;
	public int ColumnId;
	public Date DateCreated;

	public int getArticleId() {
		return this.ArticleId;
	}

	public void setArticleId(int articleId) {
		this.ArticleId = articleId;
	}

	public String getTitle() {
		return this.Title;
	}

	public void setTitle(String title) {
		this.Title = title;
	}

	public String getSummary() {
		return this.Summary;
	}

	public void setSummary(String summary) {
		this.Summary = summary;
	}

	public String getContent() {
		return this.Content;
	}

	public void setContent(String content) {
		this.Content = content;
	}

	public int getColumnId() {
		return this.ColumnId;
	}

	public void setColumnId(int columnId) {
		this.ColumnId = columnId;
	}

	public Date getDateCreated() {
		return this.DateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.DateCreated = dateCreated;
	}

	// public String getLink() {
	// return Link;
	// }
	//
	// public void setLink(String link) {
	// Link = link;
	// }

}