package com.example.nationalpetition.external.petition;

import com.example.nationalpetition.external.petition.dto.response.PetitionRankResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Component
public class PetitionRankClientImpl implements PetitionRankClient {

	private final WebClient webClient;

	@Override
	public PetitionRankResponse getRank() {
		return webClient.post()
				.uri("https://www1.president.go.kr/api/petitions/rank")
				.header("X-Requested-With", "XMLHttpRequest")
				.header("Content-Length", "0")
				.retrieve()
				.bodyToMono(PetitionRankResponse.class)
				.block();
	}

}
