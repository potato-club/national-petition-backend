package com.example.nationalpetition.external.petition;

import com.example.nationalpetition.external.petition.dto.response.PetitionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "petitionClient", url = "https://fogojgtvd5.execute-api.ap-northeast-2.amazonaws.com")
public interface PetitionClient {

	@GetMapping("/petitions")
	PetitionResponse getPetitionInfo(@RequestParam("id") Long id);

}
