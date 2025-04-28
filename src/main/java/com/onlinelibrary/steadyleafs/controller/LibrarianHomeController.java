package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Librarian;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.model.dto.MemberReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.MemberUpdateDto;
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
	public String getLibrarianHomePage(Model model, Authentication authentication) {
		User currentUser = userService.getLoggedInUser(authentication);
		Librarian currentLibrarian = getLoggedInLibrarian(authentication);

		List<Book> bookList = bookService.getAllBooks();
		model.addAttribute("bookList", bookList);

		model.addAttribute("currentUser", currentUser);
		model.addAttribute("currentLibrarian", currentLibrarian);

		return "librarians/home";
	}

//	@GetMapping("/books")
//	public String getAllBooks(Model model) {
//		List<Book> bookList = bookService.getAllBooks();
//		model.addAttribute("bookList", bookList);
//		return "librarians/books/allBooks";
//	}

	@GetMapping("/books/available")
	public String getAvailableBooks(Model model) {
		List<Book> bookList = bookService.getAvailableBooks();
		model.addAttribute("availableBookList", bookList);
		return "librarians/books/availableBooks";
	}

	@GetMapping("/books/loaned")
	public String getLoanedBooks(Model model) {
		List<Book> bookList = bookService.getLoanedBooks();
		model.addAttribute("loanedBookList", bookList);
		return "librarians/books/loanedBooks";
	}

	@GetMapping("/books/create")
	public String getCreateBookForm(Model model) {
		model.addAttribute("book", new Book());
		return "librarians/books/createBookForm";
	}

	@PostMapping()
	public String createBook(Model model, @ModelAttribute Book book) {
		bookService.createBook(book);
		model.addAttribute("bookList", bookService.getAllBooks());
		return "redirect:/librarianHome";
	}

	@GetMapping("/books/updateForm")
	public String getUpdateBookForm(Model model, @RequestParam int id) {
		Book book = bookService.getBookById(id);
		model.addAttribute("book", book);
		model.addAttribute("userId", id);
		return "librarians/books/updateBookForm";
	}

	@PostMapping("/books/update")
	public String updateBook(@RequestParam int id, @ModelAttribute Book book) {
		Book bookToUpdate = bookService.getBookById(id);
		bookToUpdate = bookService.updateBook(book);

		return "redirect:/librarianHome";
	}

	@PostMapping("/books/delete")
	public String deleteBook(@RequestParam int id) {
		bookService.deleteBook(id);

		return "redirect:/librarianHome";
	}

	@GetMapping("/members")
	public String getAllMembers(Model model) {
		List<MemberReturnDto> memberList = memberService.getAllMembers();
		model.addAttribute("memberList", memberList);

		return "librarians/members/memberList";
	}

	@GetMapping("/members/updateForm")
	public String getUpdateMemberForm(Model model, @RequestParam int id) {
		MemberReturnDto memberReturnDto = memberService.getMemberById(id);

//		MemberUpdateDto memberUpdateDto = MemberUpdateDto.mapFromMember(memberReturnDto);

		model.addAttribute("memberUpdateDto", memberReturnDto);
		model.addAttribute("memberId", id);

		return "librarians/members/updateMemberForm";
	}

	@PostMapping("/members/update")
	public String updateMember(@ModelAttribute MemberUpdateDto memberUpdateDto, BindingResult bindingResult) {
//		MemberUpdateDto memberToUpdate = MemberUpdateDto.mapFromMember(memberService.getMemberById(memberUpdateDto.getId()));
		memberService.updateMember(memberUpdateDto);

		return "redirect:/librarianHome/members";
	}

	@PostMapping("/members/delete")
	public String deleteMember(@RequestParam int id) {
		memberService.deleteMember(id);

		return "redirect:/librarianHome/members";
	}
}
