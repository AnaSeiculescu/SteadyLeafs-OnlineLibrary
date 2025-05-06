package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Doc;
import com.onlinelibrary.steadyleafs.model.OpenLibraryCovers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class BookCoverApiService {
	private static final String OPEN_LIBRARY_API_URL = "https://openlibrary.org/search.json?title=";

	RestTemplate restTemplate = new RestTemplate();

	public String getCoverUrl(String bookTitle) {
		ResponseEntity<OpenLibraryCovers> response = restTemplate.getForEntity(OPEN_LIBRARY_API_URL + bookTitle, OpenLibraryCovers.class);

		if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
			OpenLibraryCovers data = response.getBody();

			if (data.getDocs() != null && !data.getDocs().isEmpty()) {
				Doc firstDoc = data.getDocs().get(0);

				System.out.println("FIRST DOC = " + firstDoc);

				int coverId = firstDoc.getCover_i();
				return "https://covers.openlibrary.org/b/id/" + coverId + "-M.jpg";
			}
		}

		return null;
	}

}
