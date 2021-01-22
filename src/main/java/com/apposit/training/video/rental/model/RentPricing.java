package com.apposit.training.video.rental.model;

import com.apposit.training.video.rental.data.auditing.Audited;

import com.apposit.training.video.rental.data.AbstractIntegerIdObject;

@Audited
public class RentPricing extends AbstractIntegerIdObject {

	private static final long serialVersionUID = 0L;

	private Double dailyRate;
	private VideoTypeEnum videoTypeId;

	public RentPricing(int id) {
		super(id);
	}

	public RentPricing() {
		super();
	}

	@Override
	public boolean isReadOnly() {

		return false;
	}

	public Double getDailyRate() {
		return this.dailyRate;
	}

	public void setDailyRate(Double dailyRate) {
		this.dailyRate = dailyRate;
	}

	public VideoTypeEnum getVideoTypeId() {
		return this.videoTypeId;
	}

	public void setVideoTypeId(VideoTypeEnum videoTypeId) {
		this.videoTypeId = videoTypeId;
	}

}
