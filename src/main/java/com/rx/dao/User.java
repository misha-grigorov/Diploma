package com.rx.dao;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String login;

    private String password;

    private String lastName;

    private String firstName;

    private String middleName;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Document> teachingLoads;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "users_disciplines", joinColumns = @JoinColumn(name = "discipline", referencedColumnName = "id"))
    private Set<Discipline> disciplines;


    protected User() {
    }

    private User(UserBuilder builder) {
        this.login = builder.login;
        this.password = builder.password;
        this.lastName = builder.lastName;
        this.firstName = builder.firstName;
        this.middleName = builder.middleName;
        this.userRole = builder.userRole;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public Set<Discipline> getDisciplines() {
        return disciplines;
    }

    public Set<Document> getTeachingLoads() {
        return teachingLoads;
    }

    public static UserBuilder builder() {
        return new UserBuilder();
    }

    public static class UserBuilder {

        private String login;

        private String password;

        private String lastName;

        private String firstName;

        private String middleName;

        private UserRole userRole;

        private Set<Document> teachingLoads = new HashSet<>();

        private Set<Discipline> disciplines = new HashSet<>();

        private UserBuilder() {
        }

        public UserBuilder withLogin(String login) {
            this.login = login;
            return this;
        }

        public UserBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public UserBuilder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder withMiddleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public UserBuilder withUserRole(UserRole userRole) {
            this.userRole = userRole;
            return this;
        }

        public UserBuilder withDiscipline(Discipline discipline) {
            this.disciplines.add(discipline);
            return this;
        }

        public UserBuilder withTeachingLoad(Document document) {
            this.teachingLoads.add(document);
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}