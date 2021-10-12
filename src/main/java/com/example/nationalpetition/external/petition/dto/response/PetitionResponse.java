package com.example.nationalpetition.external.petition.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PetitionResponse {

	private String title;

	private String content;

	private String petitionsCount;

	private String status;

	private String category;

	private String createdAt;

	private String finishedAt;

	public PetitionResponse(String title, String content, String petitionsCount, String status, String category, String createdAt, String finishedAt) {
		this.title = title;
		this.content = content;
		this.petitionsCount = petitionsCount;
		this.status = status;
		this.category = category;
		this.createdAt = createdAt;
		this.finishedAt = finishedAt;
	}

	public static PetitionResponse of(String title, String content, String petitionsCount, String status, String category, String createdAt, String finishedAt) {
		return new PetitionResponse(title, content, petitionsCount, status, category, createdAt, finishedAt);
	}

}
