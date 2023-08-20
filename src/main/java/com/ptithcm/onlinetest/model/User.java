package com.ptithcm.onlinetest.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ptithcm.onlinetest.model.audit.DateAudit;
import lombok.*;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Builder
//@Data
@AllArgsConstructor
//@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "username"
        }),
        @UniqueConstraint(columnNames = {
                "email"
        })
})
public class User extends DateAudit {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @NotBlank
        @Size(max = 15)
        @Column(name = "username")
        private String username;

        @NaturalId
        @NotBlank
        @Size(max = 40)
        @Email
        @Column(name = "email")
        private String email;

        @NotBlank
        @Size(max = 100)
        @Column(name = "password")
        private String password;

        @Column(name = "enabled")
        private  boolean enabled;

        @Column(name = "firstname")
        private String firstName;

        @Column(name = "lastname")
        private String lastName;

        @Column(name = "host")
        private int host;

        private Instant lastLogin;


        @ManyToMany(fetch = FetchType.LAZY)
        @JsonIgnore
        @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id")
        )
        private Set<Role> roles = new HashSet<>();

        public boolean getEnable() {
                return enabled;
        }
        public User() {
                this.enabled = false;
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        public boolean isEnabled() {
                return enabled;
        }

        public void setEnabled(boolean enabled) {
                this.enabled = enabled;
        }

        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(String firstName) {
                this.firstName = firstName;
        }

        public String getLastName() {
                return lastName;
        }

        public void setLastName(String lastName) {
                this.lastName = lastName;
        }

        public int getHost() {
                return host;
        }

        public void setHost(int host) {
                this.host = host;
        }

        public Instant getLastLogin() {
                return lastLogin;
        }

        public void setLastLogin(Instant lastLogin) {
                this.lastLogin = lastLogin;
        }

        public Set<Role> getRoles() {
                return roles;
        }

        public void setRoles(Set<Role> roles) {
                this.roles = roles;
        }
}
