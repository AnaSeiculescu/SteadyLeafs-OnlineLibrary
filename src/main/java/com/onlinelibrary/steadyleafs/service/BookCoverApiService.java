package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.OpenLibraryCovers;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
//@AllArgsConstructor
public class BookCoverApiService {
	private static final String OPEN_LIBRARY_API_URL = "https://openlibrary.org/search.json?title=";

	RestTemplate restTemplate = new RestTemplate();

//	private final String bookTitle;

	public String getCoverUrl(String bookTitle) {
		ResponseEntity<OpenLibraryCovers[]> response = restTemplate.getForEntity(OPEN_LIBRARY_API_URL + bookTitle, OpenLibraryCovers[].class);

		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			OpenLibraryCovers[] covers = response.getBody();

			if (covers.length > 0 && covers[0] != null && covers[0].getDocs() != null) {
				int coverId = covers[0].getDocs().getCover_i();
				return "https://covers.openlibrary.org/b/id/" + coverId + "-M.jpg";
			}
		}

		return null;
	}

}
