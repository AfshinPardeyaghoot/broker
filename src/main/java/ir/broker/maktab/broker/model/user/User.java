package ir.broker.maktab.broker.model.user;

import ir.broker.maktab.broker.model.request.Request;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.*;


@Entity
@Setter
@Getter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(unique = true)
    @Size(min = 6, max = 50)
    private String username;

    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastName;

    @NotBlank
    private String password;


    @NotBlank
    @Column(unique = true)
    @Size(min = 11, max = 11)
    private String phoneNumber;

    @NotBlank
    @Column(unique = true)
    @Email(message = "email should be valid")
    private String email;

    @NotBlank
    @Column(unique = true)
    @Size(min = 10, max = 10)
    private String nationalId;

    private Boolean isEnable;

    private Boolean isNonLocked;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    @OneToMany(mappedBy = "user")
    private Set<Request> requests = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.toString()));
        });
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }

}
