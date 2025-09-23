package inf.akligo.auth.authConfiguration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import inf.akligo.auth.gestionDesBiens.entity.Immeuble;

import jakarta.persistence.Column;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import jakarta.persistence.OneToMany;

import inf.akligo.auth.authConfiguration.entity.Roles;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.util.List;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.security.Principal;
import java.util.stream.Collectors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;



@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@ToString(exclude = {"immeubles", "roles"})
@JsonIgnoreProperties({"immeubles"})
@EntityListeners(AuditingEntityListener.class)
public class Utilisateurs implements UserDetails, Principal{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenoms;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @Column(unique = true)
    private String email;
    private String telephone;
    private String adresse;

    @OneToMany(mappedBy = "utilisateur")
    private List<Immeuble> immeubles;

    @ManyToMany(fetch= FetchType.EAGER)
    private List<Roles> roles;

    @CreatedDate
    @Column(name = "created_at", nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    private boolean accountLocked;
    private boolean enabled;

    public List<Roles> getRoles() {
    return roles;
    }

    @Override
    public String getName(){
        return email;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return this.roles
                    .stream()
                    .map(r-> new SimpleGrantedAuthority(r.getName()))
                    .collect(Collectors.toList());
    }
    
    @Override
    public String getPassword() {
        return password; 
    }

    @Override
    public String getUsername() {
        return email; 
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; 
    }

    @Override
    public boolean isEnabled() {
        return enabled; 
    }

    public String getFullName(){
        return nom + " " + prenoms;
    }
    
    public String getEmail() {
        return email;
    }

     public boolean isAccountLocked() {
        return accountLocked;
    }








}