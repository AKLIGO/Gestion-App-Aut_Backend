package inf.akligo.auth.authConfiguration.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import jakarta.persistence.Column;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import inf.akligo.auth.authConfiguration.entity.Roles;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import java.util.List;
import java.util.Collection;
import com.fasterxml.jackson.annotation.JsonProperty;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class Utilisateurs{
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


}