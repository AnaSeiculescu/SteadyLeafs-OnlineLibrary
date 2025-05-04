package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Librarian;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.*;
import com.onlinelibrary.steadyleafs.service.BookService;
import com.onlinelibrary.steadyleafs.service.MemberService;
import com.onlinelibrary.steadyleafs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
//		User currentUser = userService.getLoggedInUser(authentication);
		Librarian currentLibrarian = getLoggedInLibrarian(authentication);

		List<BookReturnDto> bookList = bookService.getAllBooks();

		List<BookReturnDto> bookReturnDtoList;
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

		model.addAttribute("bookList", bookList);
		model.addAttribute("filter", filter);
//		model.addAttribute("currentUser", currentUser);
		model.addAttribute("currentLibrarian", currentLibrarian);
		model.addAttribute("activePage", "home");

		return "librarians/home";
	}

//	@GetMapping("/books")
//	public String getAllBooks(Model model) {
//		List<Book> bookList = bookService.getAllBooks();
//		model.addAttribute("bookList", bookList);
//		return "librarians/books/allBooks";
//	}

//	@GetMapping("/books/available")
//	public String getAvailableBooks(Model model) {
//		List<Book> bookList = bookService.getAvailableBooks();
//		model.addAttribute("availableBookList", bookList);
//		return "librarians/books/availableBooks";
//	}

//	@GetMapping("/books/loaned")
//	public String getLoanedBooks(Model model) {
//		List<Book> bookList = bookService.getLoanedBooks();
//		model.addAttribute("loanedBookList", bookList);
//		return "librarians/books/loanedBooks";
//	}

	@GetMapping("/books/create")
	public String getCreateBookForm(Model model, Authentication authentication) {
		Librarian currentLibrarian = getLoggedInLibrarian(authentication);

		model.addAttribute("currentLibrarian", currentLibrarian);
		model.addAttribute("book", new BookCreateDto());
		model.addAttribute("activePage", "createBook");
		return "librarians/books/createBookForm";
	}

	@PostMapping()
	public String createBook(Model model, @ModelAttribute BookCreateDto bookCreateDto) {
		bookService.createBook(bookCreateDto.mapToBook(bookCreateDto));
		model.addAttribute("bookList", bookService.getAllBooks());
		return "redirect:/librarianHome";
	}

	@GetMapping("/books/updateForm")
	public String getUpdateBookForm(Model model, @RequestParam int id, Authentication authentication) {
		Librarian currentLibrarian = getLoggedInLibrarian(authentication);

		BookReturnDto book = bookService.getBookById(id);

		model.addAttribute("currentLibrarian", currentLibrarian);
		model.addAttribute("book", book);
		model.addAttribute("userId", id);
		return "librarians/books/updateBookForm";
	}

	@PostMapping("/books/update")
	public String updateBook(@RequestParam int id, @ModelAttribute BookUpdateDto bookUpdateDto) {
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

		int all = bookService.getNumberOfBooks();
		int loaned = bookService.getNumberOfLoanedBooks();
		int available = bookService.getNumberOfAvailableBooks();

		model.addAttribute("currentLibrarian", currentLibrarian);
		model.addAttribute("quantityAll", all);
		model.addAttribute("quantityLoaned", loaned);
		model.addAttribute("quantityAvailable", available);
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
		List<Book> borrowedBooks = bookService.getBooksByBorrowedBy(id);

		model.addAttribute("currentLibrarian", currentLibrarian);
		model.addAttribute("member", member);
		model.addAttribute("borrowedBooks", borrowedBooks);

		return "librarians/members/borrowedBooks";
	}
}
