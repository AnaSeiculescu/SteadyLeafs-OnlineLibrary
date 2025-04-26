package com.onlinelibrary.steadyleafs.controller;

import com.onlinelibrary.steadyleafs.model.Member;
import com.onlinelibrary.steadyleafs.model.dto.*;
import com.onlinelibrary.steadyleafs.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping()
	public String getAllMembers(Model model) {
		List<MemberReturnDto> memberList = memberService.getAllMembers();
		model.addAttribute("memberList", memberList);

		return "librarians/members/memberList";
	}

	@GetMapping("/updateForm")
	public String getUpdateMemberForm(Model model, @RequestParam int id) {
		Member member = memberService.getMemberById(id);
		MemberUpdateDto memberUpdateDto = MemberUpdateDto.mapFromMember(member);
		model.addAttribute("memberUpdateDto", memberUpdateDto);
		model.addAttribute("memberId", id);

		return "librarians/members/updateMemberForm";
	}

	@PostMapping("/update")
	public String updateMember(@ModelAttribute MemberUpdateDto memberUpdateDto, BindingResult bindingResult) {
		MemberUpdateDto userToUpdate = MemberUpdateDto.mapFromMember(memberService.getMemberById(memberUpdateDto.getId()));
		memberService.updateMember(memberUpdateDto);

		return "redirect:/members";
	}

	@PostMapping("/delete")
	public String deleteMember(@RequestParam int id) {
		memberService.deleteMember(id);

		return "redirect:/members";
	}

}
