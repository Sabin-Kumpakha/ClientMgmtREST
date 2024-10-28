package com.connect.ClientMgmtREST.dto.request;

import com.connect.ClientMgmtREST.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  private String name;
  private String email;
  private String phoneNumber;
  private String password;
  private Role role= Role.USER;
}
