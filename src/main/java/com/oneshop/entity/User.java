package com.oneshop.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;
@Entity @Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false, length = 100)
  private String username;

  @Column(unique = true, nullable = false, length = 150)
  private String email;

  @Column(nullable = false)
  private String password; // BCrypt
//  private boolean emailVerified = false;
  private boolean enabled; // true sau khi verify email
  @Column(name = "full_name", length = 150)
  private String fullName;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles;
}
