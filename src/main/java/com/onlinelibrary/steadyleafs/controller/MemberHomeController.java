package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.service.BookService;
import com.onlinelibrary.steadyleafs.service.MemberHomeService;
import com.onlinelibrary.steadyleafs.service.MyUserDetails;
import com.onlinelibrary.steadyleafs.service.MyUserDetailsService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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

	@GetMapping()
	@Transactional
	public String getMemberHomePage(Model model, Authentication authentication) {
//		List<Book> borrowedBooks = memberHomeService.getAllMyBooks();
//		model.addAttribute("borrowedBooks", borrowedBooks);

		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
		User currentUser = userDetails.getUser();
		Member currentMember = currentUser.getMember();

		model.addAttribute("currentUser", currentUser);
		model.addAttribute("currentMember", currentMember);

		return "members/home";
	}

	@GetMapping("/seeAllBooks")
	public String getAllBooks(Model model) {
		List<Book> bookList = bookService.getAllBooks();
		model.addAttribute("bookList", bookList);
		return "books/allBooksForMembers";
	}

	@PostMapping("/add")
	public String BorrowBook(Model model, @ModelAttribute Book book) {
		memberHomeService.borrowBook(book);

		return "redirect:/memberHome";
	}

	@PostMapping("/delete")
	public String returnMyBook(@RequestParam int id) {
		memberHomeService.returnMyBook(id);

		return "redirect:/memberHome";
	}

}
