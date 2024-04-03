package com.example.demo.InputDto;

import com.example.demo.Models.UserManagement.Management.Status;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatUser {
    @Id
    private String username;
    private String fullName;
    private Status status;
}
