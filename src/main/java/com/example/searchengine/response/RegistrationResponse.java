package com.example.searchengine.response;

import com.example.searchengine.entity.User;
import lombok.*;

@Data
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class RegistrationResponse extends Response {
    private User user;
    public RegistrationResponse(ResponseStatus status, String message, User user){
        super(status,message);
        this.user = user;
    }
}
