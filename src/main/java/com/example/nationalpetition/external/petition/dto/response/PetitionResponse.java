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

	public PetitionResponse(String title, String content, String petitionsCount, String status) {
		this.title = title;
		this.content = content;
		this.petitionsCount = petitionsCount;
		this.status = status;
	}

	public static PetitionResponse of(String title, String content, String petitionsCount, String status) {
		return new PetitionResponse(title, content, petitionsCount, status);
	}

}
