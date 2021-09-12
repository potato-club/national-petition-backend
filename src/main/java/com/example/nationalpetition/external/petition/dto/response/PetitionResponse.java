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

}
