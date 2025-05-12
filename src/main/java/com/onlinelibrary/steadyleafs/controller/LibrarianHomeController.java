package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Librarian;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.*;
import com.onlinelibrary.steadyleafs.service.BookService;
import com.onlinelibrary.steadyleafs.service.MemberService;
import com.onlinelibrary.steadyleafs.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/librarianHome")
@RequiredArgsConstructor
public class LibrarianHomeController {
	private final UserService userService;
	private final BookService bookService;
	private final MemberService memberService;

	private Librarian getLoggedInLibrarian(Authentication authentication) {
		User currentUser = userService.getLoggedInUser(authentication);
		Librarian librarian = currentUser.getLibrarian();
		return librarian;
	}

	@GetMapping()
	public String getLibrarianHomePage(
			Model model,
			Authentication authentication,
			@RequestParam(required = false, defaultValue = "all") String filter
	) {

		Librarian currentLibrarian = getLoggedInLibrarian(authentication);

		List<BookReturnDto> bookList;

		switch (filter) {
			case "loaned":
				bookList = bookService.getLoanedBooks();
				break;
			case "available" :
				bookList = bookService.getAvailableBooks();
				break;
			default:
				bookList = bookService.getAllBooks();
		}

		for (BookReturnDto book : bookList) {
			if (book.getBorrowedById() != null) {
				MemberReturnDto member = memberService.getMemberById(book.getBorrowedById());
				book.setMemberReturnDto(member);
			}
		}

		model.addAttribute("bookList", bookList);
		model.addAttribute("filter", filter);
		model.addAttribute("currentLibrarian", currentLibrarian);
		model.addAttribute("activePage", "home");

		return "librarians/home";
	}

	@GetMapping("/books/create")
	public String getCreateBookForm(Model model, Authentication authentication) {
		Librarian currentLibrarian = getLoggedInLibrarian(authentication);

		model.addAttribute("currentLibrarian", currentLibrarian);
		model.addAttribute("book", new BookCreateDto());
		model.addAttribute("activePage", "createBook");
		return "librarians/books/createBookForm";
	}

	@PostMapping()
	public String createBook(Model model, @ModelAttribute @Valid BookCreateDto bookCreateDto) {
		bookService.createBook(bookCreateDto);
		model.addAttribute("bookList", bookService.getAllBooks());
		return "redirect:/librarianHome";
	}

	@GetMapping("/books/updateForm")
	public String getUpdateBookForm(Model model, @RequestParam int id, Authentication authentication) {
		Librarian currentLibrarian = getLoggedInLibrarian(authentication);

//		BookReturnDto book = bookService.getBookById(id);
		Book bookFromDatabase = bookService.getBookById(id);
		BookReturnDto book = new BookReturnDto().mapFromBook(bookFromDatabase);

		model.addAttribute("currentLibrarian", currentLibrarian);
		model.addAttribute("book", book);
		model.addAttribute("userId", id);
		return "librarians/books/updateBookForm";
	}

	@PostMapping("/books/update")
	public String updateBook(@RequestParam int id, @ModelAttribute @Valid BookUpdateDto bookUpdateDto) {
		bookService.updateBook(bookUpdateDto);
		return "redirect:/librarianHome";
	}

	@PostMapping("/books/delete")
	public String deleteBook(@RequestParam int id) {
		bookService.deleteBook(id);

		return "redirect:/librarianHome";
	}

	@GetMapping("/books/statistics")
	public String getBooksStatistics(Model model, Authentication authentication) {
		Librarian currentLibrarian = getLoggedInLibrarian(authentication);

		Map<String, Integer> statsMap = new LinkedHashMap<>();
		statsMap.put("All books", bookService.getNumberOfBooks());
		statsMap.put("Loaned books", bookService.getNumberOfLoanedBooks());
		statsMap.put("Available books", bookService.getNumberOfAvailableBooks());

		model.addAttribute("currentLibrarian", currentLibrarian);
		model.addAttribute("bookStats", statsMap);
		model.addAttribute("activePage", "statistics");

		return "librarians/books/statistics";
	}

	@GetMapping("/members")
	public String getAllMembers(Model model, Authentication authentication) {
		Librarian currentLibrarian = getLoggedInLibrarian(authentication);

		List<MemberReturnDto> memberList = memberService.getAllMembers();

		model.addAttribute("currentLibrarian", currentLibrarian);
		model.addAttribute("memberList", memberList);
		model.addAttribute("activePage", "allMembers");

		return "librarians/members/memberList";
	}

	@GetMapping("/members/updateForm")
	public String getUpdateMemberForm(Model model, @RequestParam int id, Authentication authentication) {
		Librarian currentLibrarian = getLoggedInLibrarian(authentication);

		MemberReturnDto memberReturnDto = memberService.getMemberById(id);

		model.addAttribute("currentLibrarian", currentLibrarian);
		model.addAttribute("memberUpdateDto", memberReturnDto);
		model.addAttribute("memberId", id);

		return "librarians/members/updateMemberForm";
	}

	@PostMapping("/members/update")
	public String updateMember(@ModelAttribute MemberUpdateDto memberUpdateDto, BindingResult bindingResult) {
		memberService.updateMember(memberUpdateDto);
		return "redirect:/librarianHome/members";
	}

	@PostMapping("/members/delete")
	public String deleteMember(@RequestParam int id) {
		memberService.deleteMember(id);

		return "redirect:/librarianHome/members";
	}

	@GetMapping("/members/borrowedBooks")
	public String getBorrowedBooksByMember(Model model, @RequestParam Integer id, Authentication authentication) {
		Librarian currentLibrarian = getLoggedInLibrarian(authentication);

		MemberReturnDto member = memberService.getMemberById(id);

		model.addAttribute("currentLibrarian", currentLibrarian);
		model.addAttribute("member", member);

		return "librarians/members/borrowedBooks";
	}
}
