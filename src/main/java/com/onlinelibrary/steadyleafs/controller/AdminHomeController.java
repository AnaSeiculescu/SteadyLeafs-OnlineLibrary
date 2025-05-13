package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.Librarian;
import com.onlinelibrary.steadyleafs.model.dto.LibrarianReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.UserReturnDto;
import com.onlinelibrary.steadyleafs.model.dto.UserUpdateDto;
import com.onlinelibrary.steadyleafs.service.LibrarianService;
import com.onlinelibrary.steadyleafs.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/adminHome")
@RequiredArgsConstructor
public class AdminHomeController {

	private final UserService userService;
	private final LibrarianService librarianService;

	@GetMapping()
	public String getAdminHomePage(Model model) {
		List<UserReturnDto> userList = userService.getAllUsers();

		model.addAttribute("userList", userList);
		model.addAttribute("activePage", "home");

		return "admin/home";
	}

	@GetMapping("/users/updateForm")
	public String getUpdateUserForm(Model model, @RequestParam int id) {
		UserReturnDto userReturnDto = userService.getUserById(id);

		model.addAttribute("userUpdateDto", userReturnDto);
		model.addAttribute("userId", id);

		return "admin/users/updateUserForm";
	}

	@PostMapping("/users/update")
	public String updateUser(@ModelAttribute UserUpdateDto userUpdateDto, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "admin/home";
		}

		userService.updateUser(userUpdateDto);

		return "redirect:/adminHome";
	}

	@PostMapping("/users/delete")
	public String deleteUser(@RequestParam int id) {
		userService.deleteUser(id);

		return "redirect:/adminHome";
	}

	@GetMapping("/librarians")
	public String getAllLibrarians(Model model) {
		List<LibrarianReturnDto> librarianList = librarianService.getAllLibrarians();

		model.addAttribute("librarianList", librarianList);
		model.addAttribute("activePage", "allLibrarians");

		return "admin/librarians/librarianList";
	}

	@PostMapping("/librarians/delete")
	public String deleteLibrarian(@RequestParam int id) {
		librarianService.deleteLibrarian(id);

		return "redirect:/adminHome/librarians";
	}
}
