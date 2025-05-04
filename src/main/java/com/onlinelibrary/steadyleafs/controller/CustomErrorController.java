//package com.onlinelibrary.steadyleafs.controller;
//
//import jakarta.servlet.RequestDispatcher;
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Controller
//public class CustomErrorController {
//
//	@RequestMapping("/custom-error")
//	public String handleError(HttpServletRequest request, Model model) {
//		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
//		System.out.println("WAAAAAAAAAAAT?!?!?");
//
//		if (status != null) {
//			int statusCode = Integer.parseInt(status.toString());
//			if (statusCode == HttpStatus.FORBIDDEN.value()) {
//				model.addAttribute("message", "You do not have permission to access this page.");
//				return "error/403";
//			} else if (statusCode == HttpStatus.NOT_FOUND.value()) {
//				model.addAttribute("message", "The page you are looking for cannot be found.");
//				return "error/404";
//			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//				model.addAttribute("message", "An unexpected error occurred on the server.");
//				return "error/500";
//			}
//		}
//
//		return null;
//	}
//}
