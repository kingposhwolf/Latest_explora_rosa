package com.example.demo.Controllers.GroupChatController;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Components.GlobalValidationFormatter.GlobalValidationFormatter;
import com.example.demo.InputDto.AddGroupMembersDto;
import com.example.demo.InputDto.NewGroupDto;
import com.example.demo.Services.GroupChatService.GroupChatService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/groups")
@AllArgsConstructor
public class GroupChatController {
    private GlobalValidationFormatter globalValidationFormatter;
    
    private final GroupChatService groupChatService;

    @PostMapping("/new-group")
    public ResponseEntity<Object> createGroup(@RequestBody @Valid NewGroupDto newGroupDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return groupChatService.createGroup(newGroupDto);
    }

    @PostMapping("/add-member")
    public ResponseEntity<Object> addMemberToGroup(@RequestBody @Valid AddGroupMembersDto addGroupMembersDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return groupChatService.addMemberToGroup(addGroupMembersDto);
    }

    @PostMapping("/remove-member")
    public ResponseEntity<Object> removeMemberFromGroup(@RequestBody @Valid AddGroupMembersDto addGroupMembersDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return globalValidationFormatter.validationFormatter(bindingResult);
        }
        return groupChatService.removeMemberFromGroup(addGroupMembersDto);
    }
}
