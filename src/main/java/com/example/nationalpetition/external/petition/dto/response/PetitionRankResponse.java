package com.example.nationalpetition.external.petition.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PetitionRankResponse {

	private String status;
	private List<RankInfo> item;

	@Getter
	@AllArgsConstructor
	@NoArgsConstructor
	private static class RankInfo {
		private String id;
		private String title;
		private String agreement;
		private String category;
		private LocalDate created;
		private LocalDate finished;
	}

}
