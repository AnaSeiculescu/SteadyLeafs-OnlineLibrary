package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.model.Doc;
import com.onlinelibrary.steadyleafs.model.OpenLibraryCovers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class BookCoverApiService {
	private static final String OPEN_LIBRARY_API_URL = "https://openlibrary.org/search.json?title=";
	private static final String DEFAULT_COVER_URL = "https://images.unsplash.com/photo-1621944190310-e3cca1564bd7?q=80&w=1974&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D";

	RestTemplate restTemplate = new RestTemplate();

	public String getCoverUrl(String bookTitle) {
		try {
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
		} catch (HttpServerErrorException e) {
			log.error("Error fetching book cover from Open Library API: {}", e.getMessage());
		} catch (Exception e) {
			log.error("Unexpected error: {}", e.getMessage());
		}

		return DEFAULT_COVER_URL;
	}

}
