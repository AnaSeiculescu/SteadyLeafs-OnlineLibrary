//package com.onlinelibrary.steadyleafs.controller;
//
//import com.onlinelibrary.steadyleafs.model.Book;
//import com.onlinelibrary.steadyleafs.service.BookService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/books")
//@RequiredArgsConstructor
//public class BookController {
//	private final BookService bookService;
//
//	@GetMapping()
//	public String getAllBooks(Model model) {
//		List<Book> bookList = bookService.getAllBooks();
//		model.addAttribute("bookList", bookList);
//		return "librarians/books/allBooks";
//	}
//
//	@GetMapping("/available")
//	public String getAvailableBooks(Model model) {
//		List<Book> bookList = bookService.getAvailableBooks();
//		model.addAttribute("availableBookList", bookList);
//		return "librarians/books/availableBooks";
//	}
//
//	@GetMapping("/loaned")
//	public String getLoanedBooks(Model model) {
//		List<Book> bookList = bookService.getLoanedBooks();
//		model.addAttribute("loanedBookList", bookList);
//		return "librarians/books/loanedBooks";
//	}
//
//	@GetMapping("/create")
//	public String getCreateBookForm(Model model) {
//		model.addAttribute("book", new Book());
//		return "librarians/books/createBookForm";
//	}
//
//	@PostMapping()
//	public String createBook(Model model, @ModelAttribute Book book) {
//		bookService.createBook(book);
//		model.addAttribute("bookList", bookService.getAllBooks());
//		return "redirect:/books";
//	}
//
//	@GetMapping("/updateForm")
//	public String getUpdateBookForm(Model model, @RequestParam int id) {
//		Book book = bookService.getBookById(id);
//		model.addAttribute("book", book);
//		model.addAttribute("userId", id);
//		return "librarians/books/updateBookForm";
//	}
//
//	@PostMapping("/update")
//	public String updateBook(@RequestParam int id, @ModelAttribute Book book) {
//		Book bookToUpdate = bookService.getBookById(id);
//		bookToUpdate = bookService.updateBook(book);
//
//		return "redirect:/books";
//	}
//
//	@PostMapping("/delete")
//	public String deleteBook(@RequestParam int id) {
//		bookService.deleteBook(id);
//
//		return "redirect:/books";
//	}
//
//}
