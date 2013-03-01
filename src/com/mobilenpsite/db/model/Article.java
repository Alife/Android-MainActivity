package com.mobilenpsite.db.model;

import java.util.Date;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

import com.mobilenpsite.configs.Config;

@Table(name = "Article")
public class Article {

	@Id(column = "Id")
	public Integer Id;
	public Integer ArticleId;
	public String Title;
	public String Summary;
	public String Content;
	public String Link;
	public Integer ColumnId;
	public Date ReleaseDate;
	public String ImageUrl;
	public boolean IsImage;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public Integer getArticleId() {
		return this.ArticleId;
	}

	public void setArticleId(Integer articleId) {
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

	public Integer getColumnId() {
		return this.ColumnId;
	}

	public void setColumnId(Integer columnId) {
		this.ColumnId = columnId;
	}

	public Date getReleaseDate() {
		return this.ReleaseDate;
	}

	public void setReleaseDate(Date dateCreated) {
		this.ReleaseDate = dateCreated;
	}

	public String getLink() {
		return Link;
	}

	public void setLink(String link) {
		Link = link;
	}

	public String getImageUrl() {
		return ImageUrl;
	}

	public void setImageUrl(String imageUrl) {
		ImageUrl = imageUrl;
	}

	public String getReleaseDateString() {
		String dateString = "";
		if (ReleaseDate != null)
			dateString = ReleaseDate.toLocaleString();
		return dateString;
	}

	public String getRealLink() {
		return Config.Url_Host + Link;
	}

	public String getRealImageUrl() {
		return Config.Url_ImgHost + ImageUrl;
	}

	public Object getIsImage() {
		return "".equals(ImageUrl);
	}

	public void setIsImage(boolean isImage) {
		IsImage = isImage;
	}

}