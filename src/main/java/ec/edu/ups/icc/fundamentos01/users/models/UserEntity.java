package ec.edu.ups.icc.fundamentos01.users.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.persistence.*;

import ec.edu.ups.icc.fundamentos01.core.entities.BaseModel;
import ec.edu.ups.icc.fundamentos01.products.models.ProductEntity;
import ec.edu.ups.icc.fundamentos01.security.models.RoleEntity;
import ec.edu.ups.icc.fundamentos01.security.models.RoleName;


@Entity
@Table(name = "users")
public class UserEntity extends BaseModel {

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(nullable = false)
    private String password;

    /**
     * Relación One-to-Many con Product
     * Un usuario puede tener múltiples productos
     */
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products = new ArrayList<>();

    /**
     * Relación Many-to-Many con Role
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    // ================= CONSTRUCTORES =================

    public UserEntity() {}

    public UserEntity(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // ================= GETTERS =================

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    // ================= SETTERS =================

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

    public void setRoles(Set<RoleEntity> roles) {
        this.roles = roles;
    }

    // ================= MÉTODOS HELPER =================

    /**
     * Agrega un producto al usuario
     */
    public void addProduct(ProductEntity product) {
        this.products.add(product);
        product.setOwner(this);
    }

    /**
     * Elimina un producto del usuario
     */
    public void removeProduct(ProductEntity product) {
        this.products.remove(product);
        product.setOwner(null);
    }

    /**
     * Agrega un rol al usuario
     */
    public void addRole(RoleEntity role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    /**
     * Elimina un rol del usuario
     */
    public void removeRole(RoleEntity role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    /**
     * Verifica si el usuario tiene un rol específico
     */
    public boolean hasRole(RoleName roleName) {
        return this.roles.stream()
            .anyMatch(role -> role.getName().equals(roleName));
    }
}
