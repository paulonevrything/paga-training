package com.apposit.training.video.rental.model;

import com.apposit.training.video.rental.data.auditing.Audited;

import com.apposit.training.video.rental.data.AbstractIntegerIdObject;

@Audited
public class Video extends AbstractIntegerIdObject {

	private static final long serialVersionUID = 0L;

	private String imgUrl;
	private Integer maximumAge;
	private String title;
	private VideoGenreEnum videoGenreId;
	private VideoTypeEnum videoTypeId;
	private Integer yearReleased;

	public Video(int id) {
		super(id);
	}

	public Video() {
		super();
	}

	@Override
	public boolean isReadOnly() {

		return false;
	}

	public String getImgUrl() {
		return this.imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getMaximumAge() {
		return this.maximumAge;
	}

	public void setMaximumAge(Integer maximumAge) {
		this.maximumAge = maximumAge;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public VideoGenreEnum getVideoGenreId() {
		return this.videoGenreId;
	}

	public void setVideoGenreId(VideoGenreEnum videoGenreId) {
		this.videoGenreId = videoGenreId;
	}

	public VideoTypeEnum getVideoTypeId() {
		return this.videoTypeId;
	}

	public void setVideoTypeId(VideoTypeEnum videoTypeId) {
		this.videoTypeId = videoTypeId;
	}

	public Integer getYearReleased() {
		return this.yearReleased;
	}

	public void setYearReleased(Integer yearReleased) {
		this.yearReleased = yearReleased;
	}

}
