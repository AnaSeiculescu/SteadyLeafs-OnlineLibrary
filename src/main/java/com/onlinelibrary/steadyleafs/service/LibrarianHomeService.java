package com.onlinelibrary.steadyleafs.service;

import com.onlinelibrary.steadyleafs.repository.BookRepository;
import com.onlinelibrary.steadyleafs.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibrarianHomeService {
	private final MemberRepository memberRepository;
	private final BookRepository bookRepository;


}
