package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.service.BookService;
import com.onlinelibrary.steadyleafs.service.MemberHomeService;
import lombok.RequiredArgsConstructor;
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
	public String getMemberHomePage(Model model) {
		List<Book> borrowedBooks = memberHomeService.getAllMyBooks();
		model.addAttribute("borrowedBooks", borrowedBooks);
		return "members/memberHome";
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
