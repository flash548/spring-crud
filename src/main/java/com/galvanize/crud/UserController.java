package com.galvanize.crud;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RestController
public class UserController {

    private UserRepository repository;
    public UserController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    public Iterable<User> getUsers() {
        return this.repository.findAll();
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User newUser, HttpServletResponse resp) {
        if (newUser == null) {
            resp.setStatus(500);
            return null;
        }

        if (newUser.getEmail() != null && newUser.getPassword() != null) {
            return this.repository.save(newUser);
        }
        else {
            resp.setStatus(500);
            return null;
        }
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id, HttpServletResponse resp) {
        try {
            Optional<User> ret = this.repository.findById(id);
            if (ret.isPresent()) { return ret.get(); }
            else {
                resp.setStatus(500);
                return null;
            }
        }
        catch (Exception e) {
            resp.setStatus(500);
            return null;
        }
    }

    @PatchMapping("/users/{id}")
    public User changeUser(@PathVariable Long id, @RequestBody User record, HttpServletResponse resp) {
        try {
            Optional<User> rec = this.repository.findById(id);
            if (rec.isPresent() && record.getEmail() != null) {
                User update = rec.get();
                update.setEmail(record.getEmail());
                if (record.getPassword() != null) update.setPassword(record.getPassword());
                return this.repository.save(update);
            }
            else {
                resp.setStatus(500);
                return null;
            }
        } catch (Exception e) {
            resp.setStatus(500);
            return null;
        }
    }

    public static class UserCount {
        private int count = 0;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class AuthRecord {
        private boolean authenticated = false;
        private User user = null;

        public boolean isAuthenticated() {
            return authenticated;
        }

        public void setAuthenticated(boolean authenticated) {
            this.authenticated = authenticated;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }

    @DeleteMapping("/users/{id}")
    public UserCount deleteUser(@PathVariable Long id, HttpServletResponse resp) {
        try {
            this.repository.deleteById(id);
            UserCount retVal = new UserCount();
            retVal.setCount(this.repository.getUserCountTotal());
            return retVal;
        }
        catch (Exception e) {
            resp.setStatus(500);
            return null;
        }
    }

    @PostMapping("/users/authenticate")
    public AuthRecord authUser(@RequestBody User rec, HttpServletResponse resp) {
        try {
            if (rec.getEmail() != null && rec.getPassword() != null) {
                User person = this.repository.getUserByEmail(rec.getEmail());
                String pass = rec.getPassword();
                if (pass.equals(person.getPassword())) {
                    AuthRecord ret = new AuthRecord();
                    ret.setAuthenticated(true);
                    ret.setUser(person);
                    return ret;
                }
                else {
                    return new AuthRecord();
                }
            }
            else {
                return new AuthRecord();
            }
        } catch (Exception e) {
            resp.setStatus(500);
            return null;
        }
    }

}
