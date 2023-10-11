package br.com.gomes.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/user")
public class UserController {
    
    @Autowired
    private IUserRepository repository;
 
    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        var user = repository.findByUserName(userModel.getUserName());

        if(user != null) {
            return  ResponseEntity.status(HttpStatus.LOCKED).body("Usuario já existe!");
        }

        var passwordHashred =  BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        userModel.setPassword(passwordHashred);
        var userCreated = repository.save(userModel);
        return ResponseEntity.ok(userCreated);
    }
}
