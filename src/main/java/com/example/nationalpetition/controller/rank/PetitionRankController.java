package com.example.nationalpetition.controller.rank;

import com.example.nationalpetition.controller.ApiResponse;
import com.example.nationalpetition.external.petition.PetitionRankClient;
import com.example.nationalpetition.external.petition.dto.response.PetitionRankResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PetitionRankController {

	private final PetitionRankClient petitionRankClient;

	@Operation(summary = "실시간으로 국민청원 인기 TOP5을 조회하는 API")
	@GetMapping("/api/v1/petition/rank")
	public ApiResponse<PetitionRankResponse> getPetitionRank() {
		return ApiResponse.success(petitionRankClient.getRank());
	}

}
