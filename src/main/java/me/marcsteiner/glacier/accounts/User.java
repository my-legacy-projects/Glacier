package me.marcsteiner.glacier.accounts;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import me.marcsteiner.glacier.Glacier;
import org.mindrot.jbcrypt.BCrypt;
import ro.pippo.core.FileItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@EqualsAndHashCode(of = { "uuid" })
public class User {

    @Getter
    private UUID uuid;

    @Getter @Setter
    private String displayName;

    @Getter @Setter
    private String eMail;

    @Getter
    private final String salt;

    @Getter
    private final String password; // Hashed with bcrypt and salt (above), not plaintext

    @Getter @Setter
    private File avatar;

    // Constructor creates a new user
    public User(String displayName, String eMail, String password, FileItem avatar) {
        this.uuid = UUID.randomUUID();
        this.displayName = displayName;
        this.eMail = eMail;
        this.salt = BCrypt.gensalt(16);
        this.password = BCrypt.hashpw(password, this.salt);
        this.avatar = new File(Paths.get(".").toAbsolutePath().normalize() + "/avatars", this.uuid + ".png");
        try {
            avatar.write(this.avatar);
        } catch (IOException ex) {
            Glacier.getInstance().getLogger().error("Unable to write avatar to file.", ex);
            this.avatar = null; // Will display default avatar
        }

        Glacier.getInstance().getApi().registerUser(this);
    }

    // Constructor creates User from existing Database user
    public User(UUID uuid, String displayName, String eMail, String salt, String password) {
        this.uuid = uuid;
        this.displayName = displayName;
        this.eMail = eMail;
        this.salt = salt;
        this.password = password;
        this.avatar = new File(Paths.get(".").toAbsolutePath().normalize() + "/avatars", this.uuid + ".png");
    }

    public boolean auth(String password) {
        if (password == null || password.isEmpty())
            return false;

        return BCrypt.checkpw(password, this.password);
    }

}
