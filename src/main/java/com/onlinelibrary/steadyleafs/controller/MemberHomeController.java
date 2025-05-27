package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.BookSearchFilter;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.BookReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.SignedInMemberDto;
import com.onlinelibrary.steadyleafs.service.BookService;
import com.onlinelibrary.steadyleafs.service.MemberHomeService;
import com.onlinelibrary.steadyleafs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/memberHome")
@RequiredArgsConstructor
public class MemberHomeController {
	private final BookService bookService;
	private final MemberHomeService memberHomeService;
	private final UserService userService;

	private SignedInMemberDto getLoggedInMember(Authentication authentication) {
		User currentUser = userService.getLoggedInUser(authentication);
		Member linkedMember = currentUser.getMember();
		SignedInMemberDto currentMember = memberHomeService.getMemberWithBorrowedBooks(linkedMember.getId());

		return currentMember;
	}

	@GetMapping()
	public String getMemberHomePage(Model model, Authentication authentication) {
		SignedInMemberDto currentMember = getLoggedInMember(authentication);

		model.addAttribute("currentMember", currentMember);
		model.addAttribute("activePage", "home");

		return "members/home";
	}

	@GetMapping("/seeAllBooks")
	public String getAllBooks(
			Model model,
			Authentication authentication,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int size
	) {
		SignedInMemberDto currentMember = getLoggedInMember(authentication);

		Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
		Page<BookReturnDto> bookPage = bookService.getAllBooks(pageable);

		model.addAttribute("currentMember", currentMember);
		model.addAttribute("activePage", "allBooks");
		model.addAttribute("filterOptions", BookSearchFilter.values());

		model.addAttribute("bookPage", bookPage);
		model.addAttribute("bookList", bookPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", bookPage.getTotalPages());
		model.addAttribute("pageSize", size);

		return "members/books/allBooksForMembers";
	}

	@PostMapping("/add")
	public String BorrowBook(Model model, @ModelAttribute BookReturnDto book, Authentication authentication) {
		SignedInMemberDto currentMember = getLoggedInMember(authentication);
		memberHomeService.borrowBook(book, currentMember);
		model.addAttribute("bookList", currentMember.getBorrowedBooks());

		return "redirect:/memberHome";
	}

	@PostMapping("/delete")
	public String returnMyBook(@ModelAttribute Book book, Authentication authentication) {
		SignedInMemberDto currentMember = getLoggedInMember(authentication);
		memberHomeService.returnMyBook(book.getId());

		return "redirect:/memberHome";
	}

	@GetMapping("/search")
	public String searchBooks(
			Model model,
			Authentication authentication,
			@RequestParam(required = false) String filter,
			@RequestParam(required = false) String value)
	{
		SignedInMemberDto currentMember = getLoggedInMember(authentication);

		if (filter == null || filter.isEmpty() || value == null || value.trim().isEmpty()) {
			List<BookReturnDto> bookList = bookService.getAllBooks();

			model.addAttribute("currentMember", currentMember);
			model.addAttribute("bookList", bookList);
			model.addAttribute("filter", null);
			model.addAttribute("value", "");

			return "members/books/allBooksForMembers";
		}

		List<BookReturnDto> bookList;

		switch (filter) {
			case "TITLE":
				bookList = bookService.getBookByTitle(value);
				break;
			case "AUTHOR":
				bookList = bookService.getBookByAuthor(value);
				break;
			default:
				bookList = bookService.getAllBooks();
		}

		model.addAttribute("currentMember", currentMember);
		model.addAttribute("bookList", bookList);
		model.addAttribute("filter", filter);

		return "members/books/bookSearchResult";
	}

}
