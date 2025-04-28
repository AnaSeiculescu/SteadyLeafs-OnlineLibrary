package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.Book;
import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.User;
import com.onlinelibrary.steadyleafs.service.BookService;
import com.onlinelibrary.steadyleafs.service.MemberHomeService;
import com.onlinelibrary.steadyleafs.service.MyUserDetails;
import com.onlinelibrary.steadyleafs.service.UserService;
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
	private final UserService userService;

//	private User getLoggedInUser(Authentication authentication) {
//		MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
//		User currentUser = userDetails.getUser();
//		return currentUser;
//	}

	private Member getLoggedInMember(Authentication authentication) {
		User currentUser = userService.getLoggedInUser(authentication);
		Member member = currentUser.getMember();
		Member currentMember = memberHomeService.getMemberWithBorrowedBooks(member.getId());
		return currentMember;
	}

	@GetMapping()
	public String getMemberHomePage(Model model, Authentication authentication) {
//		User currentUser = userService.getLoggedInUser(authentication);
		Member currentMember = getLoggedInMember(authentication);

//		model.addAttribute("currentUser", currentUser);
		model.addAttribute("currentMember", currentMember);

		return "members/home";
	}

	@GetMapping("/seeAllBooks")
	public String getAllBooks(Model model) {
		List<Book> bookList = bookService.getAllBooks();
		model.addAttribute("bookList", bookList);

		return "members/books/allBooksForMembers";
	}

	@PostMapping("/add")
	public String BorrowBook(Model model, @ModelAttribute Book book, Authentication authentication) {
		Member currentMember = getLoggedInMember(authentication);
		memberHomeService.borrowBook(book, currentMember);
		model.addAttribute("bookList", currentMember.getBorrowedBooks());

		return "redirect:/memberHome";
	}

	@PostMapping("/delete")
	public String returnMyBook(@ModelAttribute Book book, Authentication authentication) {
		Member currentMember = getLoggedInMember(authentication);
		memberHomeService.returnMyBook(book.getId());

		return "redirect:/memberHome";
	}

}
