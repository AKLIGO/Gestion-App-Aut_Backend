package inf.akligo.auth.authConfiguration.datas;
import inf.akligo.auth.authConfiguration.entity.Roles;

import java.util.List;


public record UserDto(  Long id,
        String nom,
        String prenoms,
        String email,
        List<Roles> roles){
    
}