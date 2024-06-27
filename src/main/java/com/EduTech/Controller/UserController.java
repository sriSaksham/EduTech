package com.EduTech.Controller;

import com.EduTech.Model.User;
import com.EduTech.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/users")
public class UserController
{

    @Autowired
    private UserRepo repo;

    @GetMapping("/getAll")
    public List <User> getAllUsers()
    {
        return repo.findAll();
    }


    @PostMapping("/register")
    public String registerUser(@RequestBody User user)
    {
        if (repo.findByUserName(user.getUserName()) != null) {
            return "Username is already taken";
        }

        if (repo.findByEmail(user.getEmail()) != null) {
            return "Email is already in use";
        }

        if (!isValidEmail(user.getEmail())) {
            return "Invalid email format";
        }

        if (!isValidPassword(user.getPassword())) {
            return "Password does not meet the requirements";
        }
        user.setScore(0);

        repo.save(user);
        return "User registered successfully";
    }

    @PostMapping("/login/{email}/{password}")
    public String login(@PathVariable String email, @PathVariable String password) {
        User P1 = repo.findByEmail(email);
        if (P1 != null && P1.getPassword().equals(password)) {
            return "Login Successful";
        } else {
            return "Login Failed";
        }
    }

    @PutMapping("/updateScore/{email}/{score}")
    public String updateScore(@PathVariable String email, @PathVariable int score)
    {
        User P1 = repo.findByEmail(email);
        if(P1 != null)
        {
            P1.setScore(score);
            repo.save(P1);
            return "Score of user " +P1.getEmail()+ " is now " +P1.getScore() ;
        }
        else
        {
            return "User not found with email";
        }
    }


    @DeleteMapping("/delete/{email}")
    public String deleteUserByEmail(@PathVariable String email)
    {
        User P1 = repo.findByEmail(email);
        if(P1==null)
        {
            return "User not found with the mail";
        }
        else
        {
            repo.deleteById(P1.getId());
        }
        return "User associated with " + email + " is now deleted";
    }


    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return false;
        }
        return true;
    }
    private boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return email != null && pat.matcher(email).matches();
    }
    //this is new
}
