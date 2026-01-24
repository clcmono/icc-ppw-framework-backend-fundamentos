package ec.edu.ups.icc.fundamentos01.security.models;

import ec.edu.ups.icc.fundamentos01.core.entities.BaseModel; // Corrige esto según tu paquete real
import ec.edu.ups.icc.fundamentos01.users.models.UserEntity;
import jakarta.persistence.*;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "roles")
public class RoleEntity extends BaseModel {

    @Column(nullable = false, unique = true, length = 50)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @Column(length = 200)
    private String description;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Set<UserEntity> users = new HashSet<>();

    // ================= CONSTRUCTORES =================
    public RoleEntity() {}

    public RoleEntity(RoleName name) {
        this.name = name;
    }

    public RoleEntity(RoleName name, String description) {
        this.name = name;
        this.description = description;
    }

    // ================= GETTERS =================
    public RoleName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    // ================= SETTERS =================
    public void setName(RoleName name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    // ================= MÉTODOS HELPER =================
    public void addUser(UserEntity user) {
        this.users.add(user);
        user.getRoles().add(this);
    }

    public void removeUser(UserEntity user) {
        this.users.remove(user);
        user.getRoles().remove(this);
    }
}
