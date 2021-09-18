package com.example.nationalpetition.controller.rank;

import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.external.petition.PetitionRankClient;
import com.example.nationalpetition.external.petition.dto.response.PetitionRankResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PetitionRankController {

	private final PetitionRankClient petitionRankClient;

	@GetMapping("/api/v1/petition/rank")
	public ApiResponse<PetitionRankResponse> getPetitionRank() {
		return ApiResponse.success(petitionRankClient.getRank());
	}

}
