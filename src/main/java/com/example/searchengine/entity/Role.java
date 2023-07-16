package com.example.searchengine.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
@Table(name = "role", schema = "public")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Role implements GrantedAuthority{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "name", unique = true, nullable = false)
  private String name;

  @Override
  public String getAuthority() {
    return name;
  }
}
