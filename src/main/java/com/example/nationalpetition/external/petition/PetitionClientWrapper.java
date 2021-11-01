package com.example.nationalpetition.external.petition;

import com.example.nationalpetition.external.petition.dto.response.PetitionResponse;
import com.example.nationalpetition.utils.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.nationalpetition.utils.error.ErrorCode.NOT_FOUND_EXCEPTION_PETITION;

@RequiredArgsConstructor
@Component
public class PetitionClientWrapper {

	private final PetitionClient petitionClient;

	public PetitionResponse getPetitionInfo(Long petitionId) {
		PetitionResponse response = petitionClient.getPetitionInfo(petitionId);
		if (response.isNotAvailable()) {
			throw new NotFoundException(String.format("해당하는 청원 글 (%s)은 존재하지 않습니다.", petitionId), NOT_FOUND_EXCEPTION_PETITION);
		}
		return response;
	}

}
